package com.example.statistic.listener;

import com.example.common.kafka.ProductKafka;
import com.example.statistic.mapper.ProductStatisticMapper;
import com.example.statistic.repository.ProductStatisticRepository;
import com.example.statistic.service.ProductStatisticService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductStatisticListener {
    private final ObjectMapper objectMapper;

    private final ProductStatisticService productStatisticService;
    private final ProductStatisticMapper productStatisticMapper;

    @KafkaListener(topics = "basket-statistic-3", containerFactory = "productKafkaListenerFactory", groupId = "statisticReader")
    public void handleProductKafkaMessage(ProductKafka productKafka) throws JsonProcessingException {
/*        //  ProductKafka productKafka = SpecificData.get().deepCopy(message.getPayload().getSchema(), message.getPayload());
        String payload = productKafka.getPayload().toString();
        //ProductKafka productKafka = objectMapper.readValue(payload, ProductKafka.class);*/
        log.info("received {}", productKafka);
        productStatisticService.save(productStatisticMapper.productKafkaToProductStatistic(productKafka));
    }
}
