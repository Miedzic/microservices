package com.example.product.service;

import com.example.product.domain.dao.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Product save(Product product);

    Product update(Product product, Long id);

    void deleteById(Long id);

    Product getProductById(Long id);

    Product getById(Long productId);

    Page<Product> getPage(Pageable pageable);
}
