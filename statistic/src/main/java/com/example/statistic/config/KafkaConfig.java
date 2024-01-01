package com.example.statistic.config;

import com.example.common.kafka.ProductKafka;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonContainerStoppingErrorHandler;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    @Bean("productKafkaListenerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, ProductKafka> kafkaListenerContainerFactory(
            @Value("${spring.kafka.properties.schema.registry.url}") String schemaRegistryUrl, KafkaProperties kafkaProperties) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, ProductKafka>();
        factory.setConsumerFactory(consumerFactory(kafkaProperties, schemaRegistryUrl));
        factory.setCommonErrorHandler(new CommonContainerStoppingErrorHandler());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, ProductKafka> consumerFactory(KafkaProperties kafkaProperties, String schemaRegistryUrl) {
        final var consumerConfig = getConsumerConfig(kafkaProperties, schemaRegistryUrl);
        return new DefaultKafkaConsumerFactory<>(consumerConfig);
    }

    private Map<String, Object> getConsumerConfig(KafkaProperties kafkaProperties, String schemaRegistryUrl) {
        final Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        properties.put(ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG, "5000");
        properties.put(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, "5000");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        properties.put("schema.registry.url", schemaRegistryUrl);
        properties.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");

        return properties;
    }
}
