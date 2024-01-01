package com.example.basket.client;

import com.example.common.dto.ProductDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("product-service")
public interface ProductClient {
    //, fallbackMethod = "fallbackGetProductById"
    @GetMapping("/api/products/{id}")
    //@CircuitBreaker(name = "product")
    @RateLimiter(name = "product")
    ProductDto getProductById(@PathVariable Long id);

   default ProductDto fallbackGetProductById(Long id, Exception e){
        return new ProductDto();
    }


}