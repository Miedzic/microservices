package com.example.basket.facade;

import com.example.basket.client.UserClient;
import com.example.common.dto.UserDto;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;

@Component
@RequiredArgsConstructor
public class UserClientFacade {
    private final UserClient userClient;

    //annotation covered with yaml
   // @Retryable(maxAttempts = 5,backoff = @Backoff(delay = 1200))
    @Retry(name = "user")
    public UserDto getUserById(Long id) {
        return userClient.getUserById(id);
    }

    // UserDto getCurrentUser(String token);

}
