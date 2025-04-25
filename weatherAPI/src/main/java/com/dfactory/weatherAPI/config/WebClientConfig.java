package com.dfactory.weatherAPI.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${weather.api.base-url}")
    private String baseUrl;

    @Bean
    public WebClient weatherWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
