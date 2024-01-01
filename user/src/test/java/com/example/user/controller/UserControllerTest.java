package com.example.user.controller;

import com.example.common.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.ContentPattern;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.shaded.org.apache.commons.io.Charsets;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.nio.charset.Charset;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@EnableDiscoveryClient(autoRegister = false)
public class UserControllerTest {
    private static final MySQLContainer CONTAINER = new MySQLContainer("mysql:8.0");
    private static final WireMockServer NOTIFICATION_SERVICE = new WireMockServer(8754);
    static {
        CONTAINER.withUsername("root").withPassword("matim198").withDatabaseName("user-service");
        CONTAINER.start();
    }
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ResourceLoader resourceLoader;
    @DynamicPropertySource
    public static void setDatabaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", CONTAINER::getJdbcUrl);
    }
    @BeforeAll
    public static void setUp(){
        NOTIFICATION_SERVICE.start();
    }
    @AfterAll
    public static void destroy(){
        NOTIFICATION_SERVICE.stop();
    }
    @Test
    void shouldSaveUser() throws Exception {
        String notificationJsonRequestBody = IOUtils.toString(resourceLoader.getResource("classpath:json/notificationOkRequest.json").getInputStream(), Charset.defaultCharset());
        String json = String.format(notificationJsonRequestBody, "stefek2137@gmail.com");
        NOTIFICATION_SERVICE.stubFor(WireMock.post("/api/notifications")
                .withRequestBody(equalToJson(json))
                .willReturn(ok()));
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(UserDto.builder()
                                .name("Stefan")
                                .email("stefek2137@gmail.com")
                                .password("1234")
                                .build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Stefan"))
                .andExpect(jsonPath("$.email").value("stefek2137@gmail.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
//        NOTIFICATION_SERVICE.verify(postRequestedFor(urlEqualTo("/api/notifications"))
//                .withRequestBody(equalToJson("{\"email\":\"stefek2137@gmail.com\", \"template name\":\"user register\",\"variables\":\"Stefan\"}")));
    }
    @Test
    void shouldNotSaveUserWithIncorrectEmail() throws Exception {
        String notificationJsonRequestBody = IOUtils.toString(resourceLoader.getResource("classpath:json/notificationOkRequest.json").getInputStream(), Charset.defaultCharset());
        String json = String.format(notificationJsonRequestBody, "stefek2137gmail.com");
        NOTIFICATION_SERVICE.stubFor(WireMock.post("/api/notifications")
                .withRequestBody(equalToJson(json))
                .willReturn(badRequest()));
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(UserDto.builder()
                        .name("Stefan")
                        .email("stefek2137gmail.com")
                        .password("1234")
                        .build())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").doesNotExist());
    }
}
