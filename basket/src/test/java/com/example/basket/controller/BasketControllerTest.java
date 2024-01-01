package com.example.basket.controller;

import com.example.basket.fixtures.SecurityFixtures;
import com.example.basket.model.Basket;
import com.example.basket.model.Product;
import com.example.basket.repository.BasketRepository;
import com.example.common.dto.ProductDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@EnableDiscoveryClient(autoRegister = false)
public class BasketControllerTest {
    private static final WireMockServer WIRE_MOCK_USER_SERVICE = new WireMockServer(8765);
    private static final WireMockServer WIRE_MOCK_PRODUCT_SERVICE = new WireMockServer(8764);
    private static final WireMockServer WIRE_MOCK_SCHEMA_REGISTRY = new WireMockServer(8081);
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

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BasketRepository basketRepository;

    @BeforeAll
    public static void startUp() {
        WIRE_MOCK_USER_SERVICE.start();
        WIRE_MOCK_PRODUCT_SERVICE.start();
        WIRE_MOCK_SCHEMA_REGISTRY.start();
    }

    @AfterAll
    public static void cleanUp() {
        WIRE_MOCK_USER_SERVICE.stop();
        WIRE_MOCK_PRODUCT_SERVICE.stop();
        WIRE_MOCK_SCHEMA_REGISTRY.stop();
    }

    @Test
    void shouldAddProductToNewBasket() throws Exception {
        File userFile = ResourceUtils.getFile("classpath:responses/currentUser-ok.json");
        File productFile = ResourceUtils.getFile("classpath:responses/getProduct-ok.json");
        String responseCurrentUser = Files.readString(Paths.get(userFile.toURI()));
        String responseProduct = Files.readString(Paths.get(productFile.toURI()));
        //String responseCurrentUser = Files.readString(Paths.get(getClass().getResource("classpath:responses/currentUser-ok.json").toURI()));
        WIRE_MOCK_USER_SERVICE.stubFor(get(urlEqualTo("/api/users/current"))
                .willReturn(okJson(responseCurrentUser)));
        WIRE_MOCK_PRODUCT_SERVICE.stubFor(get(urlEqualTo("/api/products/0"))
                .willReturn(okJson(responseProduct)));
        WIRE_MOCK_SCHEMA_REGISTRY.stubFor(WireMock.post(urlEqualTo("/subjects/basket-statistic-3-value/versions?normalize=false"))
                .willReturn(okJson("{}")));
        mockMvc.perform(post("/api/baskets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + "generateToken(\"ryszard\", List.of(\"user\",\"admin\"))")
                        .content(objectMapper.writeValueAsBytes(ProductDto.builder()
                                .id(0L)
                                .quantity(100L)
                                .build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
        //.andExpect()
    }
    @Test
    void shouldNotAddNotExistingProductToBasket() throws Exception{
        File userFile = ResourceUtils.getFile("classpath:responses/currentUser-ok.json");
        String responseCurrentUser = Files.readString(Paths.get(userFile.toURI()));
        WIRE_MOCK_USER_SERVICE.stubFor(get(urlEqualTo("/api/users/current"))
                .willReturn(okJson(responseCurrentUser)));
        WIRE_MOCK_PRODUCT_SERVICE.stubFor(get(urlEqualTo("/api/products/0"))
                .willReturn(notFound()));
        mockMvc.perform(post("/api/baskets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION,"d")
                        .content(objectMapper.writeValueAsBytes(ProductDto.builder()
                                .id(0L)
                                .quantity(100L)
                                .build())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Couldn't get correct response from service"));
    }
    @Test
    void shouldAddProductToOldBasket() throws Exception {
        File userFile = ResourceUtils.getFile("classpath:responses/currentUser-ok.json");
        File productFile = ResourceUtils.getFile("classpath:responses/getProduct-ok.json");
        String responseCurrentUser = Files.readString(Paths.get(userFile.toURI()));
        String responseProduct = Files.readString(Paths.get(productFile.toURI()));
        WIRE_MOCK_USER_SERVICE.stubFor(get(urlEqualTo("/api/users/current"))
                .willReturn(okJson(responseCurrentUser)));
        WIRE_MOCK_PRODUCT_SERVICE.stubFor(get(urlEqualTo("/api/products/0"))
                .willReturn(okJson(responseProduct)));
        WIRE_MOCK_SCHEMA_REGISTRY.stubFor(WireMock.post(urlEqualTo("/subjects/basket-statistic-3-value/versions?normalize=false"))
                .willReturn(okJson("{}")));
        basketRepository.save(new Basket(null, Collections.singletonList(Product.builder()
                .id(1L)
                .name("wardrobe")
                .quantity(40L)
                .build()), 0L));
        mockMvc.perform(post("/api/baskets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION,"d")
                        .content(objectMapper.writeValueAsBytes(ProductDto.builder()
                                .id(0L)
                                .quantity(100L)
                                .build()
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

    }
}
