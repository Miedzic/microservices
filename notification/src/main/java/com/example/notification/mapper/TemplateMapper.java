package com.example.notification.mapper;

import com.example.notification.domain.dao.Template;
import com.example.notification.domain.dto.TemplateDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TemplateMapper {
    Template templateDtoToTemplate(TemplateDto dto);

    TemplateDto templateToTemplateDTO(Template template);
}
