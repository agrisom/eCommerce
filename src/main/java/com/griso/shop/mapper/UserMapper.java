package com.griso.shop.mapper;

import com.griso.shop.dto.UserDto;
import com.griso.shop.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);
    List<UserDto> toUserDtoList(List<User> users);
    User toUser(UserDto userDto);

}
