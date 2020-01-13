package com.griso.shop.service;

import com.griso.shop.dto.UserDto;
import com.griso.shop.model.DeleteResponse;
import com.griso.shop.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserService {

    User findUserByUsername(String username);
    void newUser(User user);
    void validateUser(String id, String key);
    User updateUser(UserDto userSecurity, User user);
    void inactivateUser(UserDto userSecurity);
    void resetUserPassword(String username);

    UserDto findUserDtoByUsername(String username);
    UserDto findUserDtoById(String id);
    Page<UserDto> findUserDtoAll(Pageable pageable);
    List<UserDto> findUserDtoAll();
    DeleteResponse deleteUserById(String id);
    UserDto newUser(UserDto userDto);
    UserDto updateUser(UserDto userDto);

}
