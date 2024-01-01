package com.example.user.controller;

import com.example.user.domain.dto.LoginDto;
import com.example.user.domain.dto.TokenDto;
import com.example.user.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    @PostMapping
    public TokenDto Login(@RequestBody LoginDto loginDto){
        return loginService.login(loginDto.getEmail(),loginDto.getPassword());
    }

}
