package com.example.user.mapper;

import com.example.common.dto.UserDto;
import com.example.user.domain.dao.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserMapperTest {

    private final UserMapper userMapper = new UserMapperImpl();

    @Test
    public void shouldConvertUserToUserDto() {
        //given
        var user = new User(5L, "Mati", "matim98@tlen.pl", "123456");

        //when
        var result = userMapper.userToUserDto(user);

        //then
        Assertions.assertThat(result.getId()).isEqualTo(user.getId());
        Assertions.assertThat(result.getName()).isEqualTo(user.getName());
        Assertions.assertThat(result.getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(result.getPassword()).isNull();
    }

    @Test
    public void shouldConvertUserDtoToUser(){
        //given
        var userDto = new UserDto(5L, "Mati", "matim98@tlen.pl", "123456");

        //when
        var result = userMapper.userDtoToUser(userDto);

        //then
        Assertions.assertThat(result.getId()).isEqualTo(userDto.getId());
        Assertions.assertThat(result.getName()).isEqualTo(userDto.getName());
        Assertions.assertThat(result.getEmail()).isEqualTo(userDto.getEmail());
        Assertions.assertThat(result.getPassword()).isEqualTo(userDto.getPassword());
    }


}
