package com.example.basket.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureMockMvc
@ActiveProfiles("test")
@EnableDiscoveryClient(autoRegister = false)
public class ConfigTest {
    private static final ElasticsearchContainer ELASTICSEARCH_CONTAINER = new ElasticsearchContainer(
            DockerImageName
                    .parse("docker.elastic.co/elasticsearch/elasticsearch")
                    .withTag("7.17.6")
    );
    private static final KafkaContainer KAFKA_CONTAINER = new KafkaContainer(
            DockerImageName
                    .parse("confluentinc/cp-kafka")
                    .withTag("7.3.1")
    );
    static {
       KAFKA_CONTAINER.addExposedPorts(9092);
        KAFKA_CONTAINER.start();

        ELASTICSEARCH_CONTAINER.addExposedPorts(9200, 9300);
        ELASTICSEARCH_CONTAINER.setWaitStrategy(
                Wait.forHttp("/")
                        .forPort(9200)
                        .forStatusCode(200)
                        .withStartupTimeout(Duration.ofSeconds(300)));
        ELASTICSEARCH_CONTAINER.start();
    }

    @DynamicPropertySource
    public static void setElasticsearchProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.elasticsearch.rest.uris", () -> "http://localhost:" + ELASTICSEARCH_CONTAINER.getMappedPort(9200));
        registry.add("spring.kafka.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
    }
    @Test
    void shouldBeEmpty(){

    }
}
