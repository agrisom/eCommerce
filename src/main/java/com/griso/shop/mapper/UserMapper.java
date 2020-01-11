package com.griso.shop.mapper;

import com.griso.shop.dto.UserDto;
import com.griso.shop.entities.UserDB;
import com.griso.shop.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(UserDB userDB);
    UserDto toUserDto(User user);
    UserDB toUserDB(UserDto userDto);
    User toUser(UserDto userDto);
    User toUser(UserDB userDB);
    List<UserDto> toUserDtoList(List<UserDB> userDBS);

}
