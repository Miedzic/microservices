package com.example.user.controller;

import com.example.common.dto.UserDto;
import com.example.user.group.Create;
import com.example.user.mapper.UserMapper;
import com.example.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;


    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
       log.info("getting user by id {}", id);
        return userMapper.userToUserDto(userService.getById(id));
    }


    @PostMapping
    @Validated(Create.class)
    public UserDto saveUser(@RequestBody @Valid UserDto user) {
        return userMapper.userToUserDto(userService.save(userMapper.userDtoToUser(user)));
    }


    // hasAnyRole / isAnonymous
    @PutMapping("/{id}")
    public UserDto updateUser(@RequestBody @Valid UserDto user, @PathVariable Long id) {
        return userMapper.userToUserDto(userService.update(userMapper.userDtoToUser(user), id));
    }


    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteById(id);
    }


    @GetMapping
    public Page<UserDto> getUserPage(@RequestParam int page, @RequestParam int size) {
        return userService.getPage(PageRequest.of(page, size)).map(userMapper::userToUserDto);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/current")
    UserDto getCurrentUser(){
        return userMapper.userToUserDto(userService.getCurrentUser());
    }
}
