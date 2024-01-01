package com.example.statistic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "product-statistic")
public class ProductStatistic {
    @Id
    private String id;
    private Long productId;
    private String name;
    private Double prize;
    private Long quantity;
}
