package com.example.basket.controller;

import com.example.basket.client.UserClient;
import com.example.basket.client.UserClientRestTemplate;
import com.example.basket.facade.UserClientFacade;
import com.example.common.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {
    private final UserClient userClient;
    private final UserClientRestTemplate userClientRestTemplate;
    private final UserClientFacade userClientFacade;
    @GetMapping("/{id}")
    public UserDto getId(@PathVariable Long id){
      // return userClientRestTemplate.method(id);
       return userClientFacade.getUserById(id);
    }
}
