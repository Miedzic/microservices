package com.example.basket.service.impl;

import com.example.basket.client.ProductClient;
import com.example.basket.client.UserClient;
import com.example.basket.exception.BasketNotFoundException;
import com.example.basket.mapper.ProductMapper;
import com.example.basket.model.Basket;
import com.example.basket.model.Product;
import com.example.basket.repository.BasketRepository;
import com.example.basket.service.BasketService;
import com.example.common.dto.ProductDto;
import com.example.common.dto.UserDto;
import com.example.common.kafka.ProductKafka;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasketServiceImpl implements BasketService {
    private final KafkaTemplate<String, ProductKafka> kafkaTemplate;
    private final BasketRepository basketRepository;
    private final UserClient userClient;
    private final ProductClient productClient;
    private final ProductMapper productMapper;

    @Override
    public void addProduct(final Long productId, final Long quantity, final String token) {
        System.out.println("coś");
        UserDto currentUser = userClient.getCurrentUser(token);
        System.out.println("userClient");
        Optional<Basket> optionalBasket = basketRepository.findByUserId(currentUser.getId());
        ProductDto productDto = productClient.getProductById(productId);
        System.out.println("productClient");
        Product product = productMapper.productDtoToProduct(productDto);
        Basket basket = optionalBasket.orElseGet(() -> Basket.builder()
                .products(new ArrayList<>())
                .userId(currentUser.getId())
                .build());
        basket.getProducts().add(product);
        basketRepository.save(basket);
/*        kafkaTemplate.send("basket-statistic-3", ProductKafka.newBuilder()
                .setId(product.getId())
                .setName(product.getName())
                .setPrize(product.getPrize().doubleValue())
                .setQuantity(quantity)
                .build());*/
    }

    @Override
    public void removeProduct(final Long productId, String token) {
        UserDto currentUser = userClient.getCurrentUser(token);
        Basket basket = basketRepository.findByUserId(currentUser.getId()).orElseThrow(BasketNotFoundException::new);
        basket.getProducts().removeIf(product -> product.getId().equals(productId));
        basketRepository.save(basket);
    }

    @Override
    public List<Product> getProducts(String token) {
        UserDto currentUser = userClient.getCurrentUser(token);
        Basket basket = basketRepository.findByUserId(currentUser.getId()).orElseThrow(BasketNotFoundException::new);
        return basket.getProducts();
    }
}
