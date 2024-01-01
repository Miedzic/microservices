package com.example.user.service;

import com.example.user.domain.dto.TokenDto;

public interface LoginService {
    TokenDto login(String email, String password);
}
