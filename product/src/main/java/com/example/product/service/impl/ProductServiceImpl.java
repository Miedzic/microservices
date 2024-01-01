package com.example.product.service.impl;

import com.example.product.domain.dao.Product;
import com.example.product.repository.ProductRepository;
import com.example.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public Product save(final Product product) {

        return productRepository.save(product);
    }

    public Product getProductById(final Long id) {
        return productRepository.getById(id);
    }

    @Override
    public Product update(final Product product, final Long id) {
        Product productDb = getById(id);
        productDb.setName(product.getName());
        productRepository.save(productDb);
        return productDb;
    }

    @Override
    public void deleteById(final Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product getById(final Long id) {
        return productRepository.getById(id);
    }

    @Override
    public Page<Product> getPage(final Pageable pageable) {
        return productRepository.findAll((pageable));
    }


}
