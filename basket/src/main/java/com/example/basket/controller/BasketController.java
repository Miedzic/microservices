package com.example.basket.controller;

import com.example.basket.mapper.ProductMapper;
import com.example.basket.service.BasketService;
import com.example.common.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/baskets")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class BasketController {
    private final BasketService basketService;
    private final ProductMapper productMapper;


    @PostMapping
    public void addProduct(@RequestBody @Valid ProductDto productDto, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        System.out.println("cosik");
        basketService.addProduct(productDto.getId(), productDto.getQuantity(),token);
    }

    @DeleteMapping("/{id}")
    public void removeProduct(@PathVariable Long productId,@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        basketService.removeProduct(productId,token);
    }

    @GetMapping
    public List<ProductDto> getProducts(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        return productMapper.productListToProductDtoList(basketService.getProducts(token));
    }

}
