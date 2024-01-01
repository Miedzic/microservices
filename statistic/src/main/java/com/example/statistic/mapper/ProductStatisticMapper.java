package com.example.statistic.mapper;

import com.example.common.kafka.ProductKafka;
import com.example.statistic.model.ProductStatistic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductStatisticMapper {
    @Mapping(source = "id", target = "productId")
    @Mapping(target = "id", ignore = true)
    ProductStatistic productKafkaToProductStatistic(ProductKafka productKafka);
}
