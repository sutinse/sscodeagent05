package com.example.weather.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

/**
 * Immutable weather data using Java 14+ Record pattern.
 * Demonstrates modern Java practices with nested records and validation.
 */
public record WeatherData(
    @JsonProperty("cityName") String cityName,
    @JsonProperty("latitude") double latitude,
    @JsonProperty("longitude") double longitude,
    @JsonProperty("hourlyWeather") List<HourlyWeather> hourlyWeather
) {
    
    /**
     * Compact constructor with validation.
     */
    public WeatherData {
        if (cityName == null || cityName.isBlank()) {
            throw new IllegalArgumentException("City name cannot be null or blank");
        }
        if (hourlyWeather == null) {
            hourlyWeather = List.of(); // Use immutable empty list
        } else {
            hourlyWeather = List.copyOf(hourlyWeather); // Create immutable copy
        }
    }
    
    /**
     * Nested record for hourly weather data.
     * Demonstrates modern Java record patterns with validation and utility methods.
     */
    public record HourlyWeather(
        @JsonProperty("time") String time,
        @JsonProperty("temperature") double temperature,
        @JsonProperty("weatherCode") int weatherCode
    ) {
        
        /**
         * Compact constructor with validation and formatting.
         */
        public HourlyWeather {
            if (time == null || time.isBlank()) {
                throw new IllegalArgumentException("Time cannot be null or blank");
            }
            if (weatherCode < 0 || weatherCode > 99) {
                throw new IllegalArgumentException("Weather code must be between 0 and 99");
            }
        }
        
        /**
         * Utility method to get formatted time using modern Java time API.
         */
        public LocalDateTime getLocalDateTime() {
            return LocalDateTime.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
        
        /**
         * Enhanced method using modern switch expressions (Java 14+).
         * Demonstrates modern control flow patterns.
         */
        public String getWeatherDescription() {
            return switch (weatherCode) {
                case 0 -> "Clear sky";
                case 1, 2, 3 -> "Partly cloudy";
                case 45, 48 -> "Foggy";
                default -> {
                    if (weatherCode >= 51 && weatherCode <= 67) {
                        yield "Rainy";
                    } else if (weatherCode >= 71 && weatherCode <= 77) {
                        yield "Snow";
                    } else if (weatherCode >= 80 && weatherCode <= 99) {
                        yield "Thunderstorm";
                    } else {
                        yield "Unknown weather condition";
                    }
                }
            };
        }
        
        /**
         * Formatted temperature string using modern String methods.
         */
        public String getFormattedTemperature() {
            return "%.1f°C".formatted(temperature);
        }
    }
    
    /**
     * Convenience method to get current weather (first hour).
     * Uses modern Optional patterns.
     */
    public Optional<HourlyWeather> getCurrentWeather() {
        return hourlyWeather.isEmpty() ? 
            Optional.empty() : 
            Optional.of(hourlyWeather.getFirst()); // Java 21 List.getFirst()
    }
    
    /**
     * Get average temperature using modern Stream API.
     */
    public OptionalDouble getAverageTemperature() {
        return hourlyWeather.stream()
                .mapToDouble(HourlyWeather::temperature)
                .average();
    }
}