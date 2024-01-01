package com.example.api.controller;

import com.example.api.model.dto.TestDto;
import com.example.api.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GraphQlExampleController {
    private final TestService testService;

    @QueryMapping
    public TestDto findById(@Argument Integer id) {
        return testService.findById(id);
    }

    @QueryMapping
    public List<TestDto> findAll() {
        return testService.findAll();
    }

    @MutationMapping
    public TestDto save(@Argument String name) {
        return testService.save(name);
    }
}
