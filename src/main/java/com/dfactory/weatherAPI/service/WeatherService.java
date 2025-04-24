package com.dfactory.weatherAPI.service;

import com.dfactory.weatherAPI.model.WeatherResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WebClient weatherWebClient;
    private final RedisTemplate<String, WeatherResponse> redisTemplate;

    @Value("${weather.api.key}")
    private String apiKey;

    public WeatherResponse getWeatherByCity(String city) {

        String cacheKey = "weather:" + city.toLowerCase();

        WeatherResponse cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            return cached;
        }

        WeatherResponse fresh = weatherWebClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/{city}")
                    .queryParam("unitGroup", "metric")
                    .queryParam("key", apiKey).build(city)
                ).retrieve().bodyToMono(WeatherResponse.class).block();

        redisTemplate.opsForValue().set(cacheKey, fresh, Duration.ofHours(1));

        return fresh;
    }
}
