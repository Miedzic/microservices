package com.example.basket.mapper;

import com.example.basket.model.Product;
import com.example.common.dto.ProductDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product productDtoToProduct(ProductDto productDto);
    List<ProductDto> productListToProductDtoList(List<Product> list);
}
