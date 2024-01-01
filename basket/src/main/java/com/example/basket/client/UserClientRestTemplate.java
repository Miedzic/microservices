package com.example.basket.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserClientRestTemplate {
    private final RestTemplate restTemplate;

    public Map method(Long id) {
        return restTemplate.getForObject("http://user-service/api/users/" + id, Map.class);
    }
}
