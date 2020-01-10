package com.griso.shop.service;

import com.griso.shop.dto.UserDto;

public interface IUserService {

    UserDto findByUsername(String username);

}
