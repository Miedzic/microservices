package com.example.statistic.repository;

import com.example.common.kafka.ProductKafka;
import com.example.statistic.model.ProductStatistic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductStatisticRepository extends ElasticsearchRepository<ProductStatistic,String> {
}
