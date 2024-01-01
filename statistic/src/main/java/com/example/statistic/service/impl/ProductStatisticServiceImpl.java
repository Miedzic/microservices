package com.example.statistic.service.impl;

import com.example.statistic.model.ProductStatistic;
import com.example.statistic.repository.ProductStatisticRepository;
import com.example.statistic.service.ProductStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductStatisticServiceImpl implements ProductStatisticService {
    private final ProductStatisticRepository productStatisticRepository;
    @Override
    public void save(final ProductStatistic productStatistic) {
        productStatisticRepository.save(productStatistic);
    }
}
