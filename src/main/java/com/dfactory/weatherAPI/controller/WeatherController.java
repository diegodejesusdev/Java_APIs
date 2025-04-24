package com.dfactory.weatherAPI.controller;

import com.dfactory.weatherAPI.model.WeatherResponse;
import com.dfactory.weatherAPI.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherResponse getWeatherByCity(String city) {
        return weatherService.getWeatherByCity(city);
    }
}
