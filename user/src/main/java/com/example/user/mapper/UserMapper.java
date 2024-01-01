package com.example.user.mapper;

import com.example.common.dto.UserDto;
import com.example.user.domain.dao.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "password", ignore = true )
    UserDto userToUserDto(User user);

    User userDtoToUser(UserDto user);

}
