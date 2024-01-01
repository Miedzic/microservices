package com.example.basket.controller;

import com.example.common.dto.ErrorDto;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AdviceController {
    /* @ExceptionHandler(FeignException.class)
       //@ResponseStatus(HttpStatus.)
    public void handleFeignException(FeignException e){
        log.error("couldn't get correct response from service",e);
        e.status();
    }*/
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorDto> handleFeignException(FeignException e) {
        log.error("couldn't get correct response from service", e);
        return ResponseEntity.status(e.status()).body(new ErrorDto("Couldn't get correct response from service"));
    }
}
