package com.dfactory.weatherAPI.model;


import lombok.Data;

import java.util.List;

@Data
public class WeatherResponse {

    private double latitude;
    private double longitude;
    private String resolvedAddress;
    private String address;
    private String timezone;
    private double tzoffset;
    private String description;

    private List<Alert> alerts;
    private List<Day> days;
    private CurrentConditions currentConditions;

    @Data
    public static class Day {
        private String datetime;
        private long datetimeEpoch;
        private double temp;
        private double feelsLike;
        private String source;
        private List<Hour> hours;
    }

    @Data
    public static class Hour {
        private String datetime;
        private double temp;
        private double feelsLike;
    }

    @Data
    public static class Alert {
        private String event;
        private String description;
    }

    @Data
    public static class CurrentConditions {
        private String datetime;
        private long datetimeEpoch;
        private double temp;
        private double feelsLike;
    }
}
