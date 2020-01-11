package com.griso.shop.service;

import com.griso.shop.dto.UserDto;
import com.griso.shop.entities.UserDB;
import com.griso.shop.mapper.UserMapper;
import com.griso.shop.model.User;
import com.griso.shop.model.UserSecurity;
import com.griso.shop.repository.IUserRepo;
import com.griso.shop.util.EmailUtil;
import com.griso.shop.util.FileUtil;
import com.griso.shop.util.JwtUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepo userRepo;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${url.path}")
    private String URL_PATH;

    protected static final Log LOG = LogFactory.getLog(UserServiceImpl.class.getName());

    @Override
    public User findUserByUsername(String username) {
        return userMapper.toUser(userRepo.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + username + " not found")));
    }

    @Override
    public void newUser(User user) {
        if(userRepo.findByUsername(user.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User registered already");
        }

        UserDto userDto = checkNewUser(user);
        userDto = save(userDto);

        final UserDetails userDetails = new UserSecurity(userDto);
        final String token = jwtUtil.generateToken(userDetails);

        sendVerificationEmail(userDto, token);
    }

    @Override
    public void validateUser(String id, String key) {
        UserDB userDB = userRepo.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if(!jwtUtil.isTokenExpired(key) && jwtUtil.extractUsername(key).equals(userDB.getUsername())) {
            userDB.setActive(true);
            userRepo.save(userDB);
        }
    }

    @Override
    public User updateUser(UserSecurity userSecurity, User user) {
        if(user.getName() != null) {
            userSecurity.getUser().setName(user.getName());
        }
        if(user.getSurname() != null) {
            userSecurity.getUser().setSurname(user.getSurname());
        }
        if(user.getBirthday() != null) {
            userSecurity.getUser().setBirthday(user.getBirthday());
        }
        if(user.getPassword() != null && !user.getPassword().equals(userSecurity.getUser().getPassword())) {
            userSecurity.getUser().setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        }

        return userMapper.toUser(userRepo.save(userMapper.toUserDB(userSecurity.getUser())));
    }

    @Override
    public void inactivateUser(UserSecurity userSecurity) {
        userSecurity.getUser().setActive(false);
        userRepo.save(userMapper.toUserDB(userSecurity.getUser()));
    }

    @Override
    public UserDto findUserDtoByUsername(String username) {
        return userMapper.toUserDto(userRepo.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + username + " not found")));
    }

    private UserDto checkNewUser(User user) {
        UserDto userDto = userMapper.toUserDto(user);
        if(!EmailUtil.isValid(user.getUsername())) {
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
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(userDto.getUsername());
            File template = FileUtil.getFileFromResources(getClass().getClassLoader(),"template/welcomeEmailTemplate.html");
            String content = String.format(FileUtil.fileToString(template), userDto.getId(), token);
            msg.setSubject("Validate email");
            msg.setText(content);

            LOG.info("Sending email:\nTo: " + userDto.getUsername() + "\nContent: " + content);
            // FIXME : Afegir un compte amb permisos per enviar o canviar a relay
            //javaMailSender.send(msg);
        } catch (Exception e) {
            LOG.error("Cannot send email");
        }
    }

    public UserDto save(UserDto userDto) {
        return userMapper.toUserDto(userRepo.save(userMapper.toUserDB(userDto)));
    }

}
