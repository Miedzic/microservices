package com.example.user.client;

import com.example.common.dto.NotificationDto;
import com.example.user.domain.dto.UserRegisterNotificationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("notification-service")
public interface NotificationClient {
    @PostMapping("/api/notifications")
    void sendNotification(@RequestBody NotificationDto<UserRegisterNotificationDto> notificationDto);

}
