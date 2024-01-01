package com.example.api.service;

import com.example.api.model.dto.TestDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TestService {
    private final List<TestDto> testDtos = new ArrayList<>();
    private final AtomicInteger nextId = new AtomicInteger();
    public TestDto findById(Integer id){
        return testDtos.stream()
                .filter(testDto -> id.equals(testDto.getId()))
                .findFirst()
                .orElseThrow(()->new RuntimeException("not found " + id));
    }
    public List<TestDto> findAll(){
        return testDtos;
    }
    public TestDto save(String name){
        TestDto testDto = new TestDto(nextId.getAndIncrement(),name);
        testDtos.add(testDto);
        return testDto;
    }
}
