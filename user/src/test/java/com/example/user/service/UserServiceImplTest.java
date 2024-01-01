package com.example.user.service;

import com.example.user.client.NotificationClient;
import com.example.user.repository.UserRepository;
import com.example.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private NotificationClient notificationClient;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void shouldGetUserById(){
        //given
        var id = 1L;

        //when
        userService.getById(id);

        //then
        Mockito.verify(userRepository,Mockito.times(1)).getReferenceById(id);
    }
    // dwa znaki większości w spocku odpowiedniku mockito // ale najpierw integracyjne
}
