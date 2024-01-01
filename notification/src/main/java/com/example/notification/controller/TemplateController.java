package com.example.notification.controller;

import com.example.notification.domain.dto.TemplateDto;
import com.example.notification.mapper.TemplateMapper;
import com.example.notification.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/templates")
@RequiredArgsConstructor
public class TemplateController {
    private final TemplateService templateService;
    private final TemplateMapper templateMapper;
    @GetMapping("/{id}")
    public TemplateDto getTemplateById(@PathVariable Long id){
        return templateMapper.templateToTemplateDTO(templateService.getById(id));
    }
    @PostMapping
    public TemplateDto saveTemplate(@RequestBody TemplateDto template){
        return templateMapper.templateToTemplateDTO(templateService.save(templateMapper.templateDtoToTemplate(template)));
    }
    @PutMapping("/{id}")
    public TemplateDto updateTemplate(@RequestBody TemplateDto template, @PathVariable Long id){
        return templateMapper.templateToTemplateDTO(templateService.update(templateMapper.templateDtoToTemplate(template), id));
    }
    @DeleteMapping("/{id}")
    public void deleteTemplateById(@PathVariable Long id){
        templateService.deleteById(id);
    }
}
