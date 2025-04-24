package com.dfactory.weatherAPI.model;


import lombok.Data;

import java.util.List;

@Data
public class WeatherResponse {
    private String resolvedAddress;
    private List<Day> days;

    @Data
    public static class Day {
        private String datetime;
        private double tempmax;
        private double tempmin;
        private double temp;
        private String description;
    }
}
