package com.griso.shop.service;

import com.griso.shop.dto.UserDto;
import com.griso.shop.entities.UserDB;
import com.griso.shop.mapper.UserMapper;
import com.griso.shop.model.DeleteResponse;
import com.griso.shop.model.User;
import com.griso.shop.model.UserSecurity;
import com.griso.shop.repository.IUserRepo;
import com.griso.shop.util.EmailUtil;
import com.griso.shop.util.FileUtil;
import com.griso.shop.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepo userRepo;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    Environment env;

    @Value("${url.path}")
    private String urlPath;

    protected static final Log LOG = LogFactory.getLog(UserServiceImpl.class.getName());

    @Override
    public void newUser(User user) {
        UserDto userDto = checkNewUser(user);

        if(userRepo.findByUsername(user.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User registered already");
        }

        userDto = save(userDto);

        final UserDetails userDetails = new UserSecurity(userDto);
        final String token = jwtUtil.generateToken(userDetails);

        sendVerificationEmail(userDto, token);
    }

    @Override
    public String validateUser(String id, String key) {
        UserDB userDB = userRepo.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        try {
            if (jwtUtil.extractUsername(key).equals(userDB.getUsername())) {
                userDB.setActive(true);
                userRepo.save(userDB);
            }
        } catch (ExpiredJwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Time has expired");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        return "User validated";
    }

    @Override
    public User updateUser(UserDto loggedUser, User user) {
        if(user.getName() != null) {
            loggedUser.setName(user.getName());
        }
        if(user.getSurname() != null) {
            loggedUser.setSurname(user.getSurname());
        }
        if(user.getBirthday() != null) {
            loggedUser.setBirthday(user.getBirthday());
        }
        if(user.getPassword() != null && !user.getPassword().equals(loggedUser.getPassword())) {
            loggedUser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        }

        return userMapper.toUser(userRepo.save(userMapper.toUserDB(loggedUser)));
    }

    @Override
    public void inactivateUser(UserDto loggedUser) {
        loggedUser.setActive(false);
        userRepo.save(userMapper.toUserDB(loggedUser));
    }

    @Override
    public void sendEmailResetPassword(String username) {
        UserDto userDto = findUserDtoByUsername(username);

        final UserDetails userDetails = new UserSecurity(userDto);
        final String token = jwtUtil.generateToken(userDetails);

        sendResetPasswordEmail(userDto, token);
    }

    @Override
    public String resetUserPassword(String id, String token, String password) {
        UserDB userDB = userRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        try {
            if (jwtUtil.extractUsername(token).equals(userDB.getUsername())) {
                userDB.setPassword(new BCryptPasswordEncoder().encode(password));
                userRepo.save(userDB);
            }
        } catch (ExpiredJwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Time has expired");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        return "Password updated";
    }

    @Override
    public UserDto findUserDtoByUsername(String username) {
        return userMapper.toUserDto(userRepo.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + username + " not found")));
    }

    @Override
    public UserDto findUserDtoById(String id) {
        return userMapper.toUserDto(userRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User id: " + id + " not found")));
    }

    @Override
    public Page<UserDto> findUserDtoAll(Pageable pageable) {
        return userRepo.findAll(pageable).map(userMapper::toUserDto);
    }

    @Override
    public List<UserDto> findUserDtoAll() {
        return userMapper.toUserDtoList(userRepo.findAll());
    }

    @Override
    public DeleteResponse deleteUserById(String id) {
        DeleteResponse response = new DeleteResponse();
        findUserDtoById(id);

        userRepo.deleteById(id);
        response.setDeleted(true);
        return response;
    }

    @Override
    public UserDto newUser(UserDto userDto) {
        if(userDto.getUsername() == null || userDto.getUsername().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
        }
        if(userRepo.findByUsername(userDto.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User registered already");
        }
        userDto.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        return userMapper.toUserDto(userRepo.save(userMapper.toUserDB(userDto)));
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        UserDto userOld = findUserDtoById(userDto.getId());
        if(!userDto.getPassword().equals(userOld.getPassword())) {
            userDto.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        }
        return userMapper.toUserDto(userRepo.save(userMapper.toUserDB(userDto)));
    }

    public UserDto save(UserDto userDto) {
        return userMapper.toUserDto(userRepo.save(userMapper.toUserDB(userDto)));
    }

    private UserDto checkNewUser(User user) {
        UserDto userDto = userMapper.toUserDto(user);
        if(!emailUtil.isValid(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email address");
        }
        if(user.getName() == null || user.getName().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User name is required");
        }
        if(user.getPassword() == null || user.getPassword().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User password is required");
        }

        userDto.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userDto.setRoles("");
        userDto.setPermissions("");
        userDto.setActive(false);
        return userDto;
    }

    private void sendVerificationEmail(UserDto userDto, String token) {
        try {
            String subject = "Validate email";
            String template = FileUtil.getFileFromResources(getClass().getClassLoader(), "template/welcomeEmailTemplate.html");
            String content = String.format(template, env.getProperty("url.path"), userDto.getId(), token);

            emailUtil.send(userDto.getUsername(), subject, content);
        } catch (IOException e) {
            LOG.error("Cannot convert template file to text");
        }
    }

    private void sendResetPasswordEmail(UserDto userDto, String token) {
        try {
            String subject = "Reset Password";
            String template = FileUtil.getFileFromResources(getClass().getClassLoader(),"template/resetPasswordEmailTemplate.html");
            String content = String.format(template, env.getProperty("url.path"), userDto.getId(), token);

            emailUtil.send(userDto.getUsername(), subject, content);
        } catch (IOException e) {
            LOG.error("Cannot convert template file to text");
            LOG.info(e.getMessage());
            LOG.info(e.getStackTrace());
        }
    }

}
