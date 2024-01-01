package com.example.user.service.impl;

import com.example.user.client.NotificationClient;
import com.example.common.dto.NotificationDto;
import com.example.user.domain.dao.User;
import com.example.user.domain.dto.UserRegisterNotificationDto;
import com.example.user.repository.UserRepository;
import com.example.user.security.SecurityUtils;
import com.example.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationClient notificationClient;

    @Override
    public User save(final User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        NotificationDto<UserRegisterNotificationDto> notificationDto = new NotificationDto<>(user.getEmail(),"user register", new UserRegisterNotificationDto(user.getName()));
        notificationClient.sendNotification(notificationDto);
        return user;
    }

    @Override
    public User update(final User user, final Long id) {
        User userDb = getById(id);
        userDb.setName(user.getName());
        userDb.setEmail(user.getEmail());
        return userDb;
    }

    @Override
    public User getById(final Long id) {
        return userRepository.getReferenceById(id);
    }

    @Override
    public void deleteById(final Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Page<User> getPage(final Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User getCurrentUser() {
        return userRepository.findByEmail(SecurityUtils.getCurrentUserEmail()).orElseThrow(EntityNotFoundException::new);
    }
}
