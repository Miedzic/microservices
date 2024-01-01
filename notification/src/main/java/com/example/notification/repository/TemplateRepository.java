package com.example.notification.repository;

import com.example.notification.domain.dao.Template;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TemplateRepository extends JpaRepository<Template,Long> {
    Optional<Template> findByName(String name);
}
