package com.example.notification.service;

import com.example.notification.domain.dao.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final ITemplateEngine iTemplateEngine;
    private final TemplateService templateService;
    private final JavaMailSender javaMailSender;

    public void sendEmail(String email, String templateName, Map<String, Object> variables) {
        Template templateByName = templateService.getTemplateByName(templateName);
        Context context = new Context(Locale.getDefault(), variables);
        String body = iTemplateEngine.process(templateByName.getBody(), context);

        javaMailSender.send(mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(templateByName.getSubject());
            mimeMessageHelper.setText(body, true);
        });
    }
}
