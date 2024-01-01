package com.example.notification.service;

import com.example.notification.domain.dao.Template;
import com.example.notification.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class TemplateService {
    private final TemplateRepository templateRepository;


    public Template save(final Template template) {
        return templateRepository.save(template);
    }

    public Template getTemplateByName(final String name) {
        return templateRepository.findByName(name).orElseThrow();
    }

    @Transactional
    public Template update(final Template template, Long id) {
        Template templateDB = getById(id);
        templateDB.setName(template.getName());
        templateDB.setSubject(template.getSubject());
        templateDB.setBody(template.getBody());
        return templateDB;
    }

    public Template getById(final Long id) {
        return templateRepository.getById(id);
    }

    public void deleteById(final Long id) {
        templateRepository.deleteById(id);
    }
}
