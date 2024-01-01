//package com.example.basket.controller;
//
//import com.netflix.loadbalancer.Server;
//import com.netflix.loadbalancer.ServerList;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.cloud.netflix.ribbon.StaticServerList;
//import org.springframework.context.annotation.Bean;
//
//@TestConfiguration
//public class LocalRibbonClientConfiguration {
//    @Bean
//    public ServerList<?> serverList() {
//        return new StaticServerList<>(new Server("localhost", 8762), new Server("localhost", 8764));
//    }
//}
