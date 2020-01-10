package com.griso.shop.service;

import com.griso.shop.dto.UserDto;
import com.griso.shop.mapper.UserMapper;
import com.griso.shop.repository.IUserRepo;
import com.griso.shop.util.EmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepo repository;
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDto findByUsername(String username) {
        return userMapper.toUserDto(repository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + username + " not found")));
    }

    @Override
    public UserDto newUser(UserDto user) {
        if(repository.findByUsername(user.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User registered already");
        }

        checkNewUser(user);
        return save(user);
    }

    @Override
    public UserDto save(UserDto user) {
        return userMapper.toUserDto(repository.save(userMapper.toUser(user)));
    }

    private UserDto checkNewUser(UserDto user) {
        if(!EmailUtils.isValid(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email address");
        }
        if(user.getName() == null || user.getName().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User name is required");
        }
        if(user.getPassword() == null || user.getPassword().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User password is required");
        }

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRoles("");
        user.setPermissions("");
        user.setActive(false);
        return user;
    }


}
