package com.example.basket.client;

import com.example.common.dto.UserDto;
import jdk.jfr.Name;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("user-service")
public interface UserClient {
    @GetMapping("/api/users/{id}")
    UserDto getUserById(@PathVariable Long id);

    @GetMapping("/api/users/current")
    UserDto getCurrentUser(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token);
}
