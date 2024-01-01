package com.example.basket.service;

import com.example.basket.model.Product;

import java.util.List;

public interface BasketService {
    void addProduct(Long productId, Long quantity, String token);
    void removeProduct(Long productId, String token);
    List<Product> getProducts(String token);
}
