package com.example.notification.controller;

import com.example.common.dto.NotificationDto;
import com.example.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Validated
public class NotificationController {
    private final NotificationService notificationService;
    @PostMapping
    public void sendEmailNotification(@RequestBody NotificationDto<Map<String,Object>> notificationDto){
        System.out.println("ddd");
           notificationService.sendEmail(notificationDto.getEmail(),notificationDto.getTemplateName(),notificationDto.getVariables());

    }

}
