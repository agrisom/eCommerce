package com.griso.shop.service;

import com.griso.shop.dto.UserDto;
import com.griso.shop.model.User;
import com.griso.shop.model.UserSecurity;

public interface IUserService {

    User findUserByUsername(String username);
    void newUser(User user);
    void validateUser(String id, String key);
    User updateUser(UserSecurity userSecurity, User user);
    void inactivateUser(UserSecurity userSecurity);

    UserDto findUserDtoByUsername(String username);

}
