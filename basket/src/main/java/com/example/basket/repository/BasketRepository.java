package com.example.basket.repository;

import com.example.basket.model.Basket;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface BasketRepository extends ElasticsearchRepository<Basket, String> {
    Optional<Basket> findByUserId(Long userId);
}
