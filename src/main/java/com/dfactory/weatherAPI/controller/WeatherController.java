package com.dfactory.weatherAPI.controller;

import com.dfactory.weatherAPI.model.WeatherResponse;
import com.dfactory.weatherAPI.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {

    @Autowired
    private final WeatherService weatherService;

    @GetMapping("/{city}")
    public WeatherResponse getWeatherByCity(@PathVariable String city) {
        return weatherService.getWeatherByCity(city);
    }
}
