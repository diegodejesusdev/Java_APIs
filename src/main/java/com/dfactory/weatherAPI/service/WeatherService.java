package com.dfactory.weatherAPI.service;

import com.dfactory.weatherAPI.model.WeatherResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WebClient weatherWebClient;

    @Value("${weather.api.key}")
    private String apiKey;

    public WeatherResponse getWeatherByCity(String city) {
        return weatherWebClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/{city}")
                    .queryParam("unitGroup", "metric")
                    .queryParam("key", apiKey).build(city)
                ).retrieve().bodyToMono(WeatherResponse.class).block();
    }
}
