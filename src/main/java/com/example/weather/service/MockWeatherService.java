package com.example.weather.service;

import com.example.weather.model.City;
import com.example.weather.model.WeatherData;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

@ApplicationScoped
public class MockWeatherService {

    private static final Logger LOG = Logger.getLogger(MockWeatherService.class);
    private final Random random = new Random();

    @Inject
    CityService cityService;

    public Optional<WeatherData> getWeatherByCityCode(String cityCode) {
        LOG.infof("Looking for weather data for city code: %s", cityCode);
        
        return cityService.findCityByCode(cityCode)
                .map(this::generateMockWeatherData)
                .map(weatherData -> {
                    LOG.infof("Generated mock weather data for city %s", weatherData.cityName());
                    return weatherData;
                });
    }

    /**
     * Modern weather data generation using enhanced Stream API and functional programming.
     * Demonstrates Java 21 features like enhanced switch expressions and modern collection processing.
     */
    private WeatherData generateMockWeatherData(City city) {
        LOG.infof("Found city: %s at coordinates (%f, %f)", 
                city.name(), city.latitude(), city.longitude());
        
        var now = LocalDateTime.now();
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        
        // Generate 24 hours of weather data using modern Stream API
        var hourlyWeatherList = IntStream.range(0, 24)
                .mapToObj(hour -> generateHourlyWeather(now.plusHours(hour), city.latitude(), formatter))
                .toList(); // Java 16+ toList()
        
        return new WeatherData(
            city.name(),
            city.latitude(),
            city.longitude(),
            hourlyWeatherList
        );
    }
    
    /**
     * Generate individual hourly weather using modern Java patterns.
     */
    private WeatherData.HourlyWeather generateHourlyWeather(LocalDateTime time, double latitude, DateTimeFormatter formatter) {
        var timeStr = time.format(formatter);
        var baseTemp = calculateBaseTemperature(latitude);
        var hourlyVariation = (random.nextDouble() - 0.5) * 6; // ±3°C variation
        var temperature = baseTemp + hourlyVariation;
        var weatherCode = generateWeatherCode();
        
        return new WeatherData.HourlyWeather(timeStr, temperature, weatherCode);
    }

    /**
     * Enhanced temperature calculation using modern switch expressions (Java 14+).
     * Demonstrates pattern matching and guard conditions.
     */
    private double calculateBaseTemperature(double latitude) {
        // Base temperature calculation: colder further north
        var baseTemp = 20 - (latitude - 60) * 2;
        
        // Seasonal variation using modern switch expressions
        var seasonalAdjustment = switch (LocalDateTime.now().getMonthValue()) {
            case 12, 1, 2 -> -15; // Winter
            case 3, 4, 5 -> -5;   // Spring  
            case 6, 7, 8 -> 5;    // Summer
            case 9, 10, 11 -> -2; // Autumn
            default -> 0; // Should never happen, but included for completeness
        };
        
        return baseTemp + seasonalAdjustment;
    }

    /**
     * Enhanced weather code generation using modern Java patterns.
     * Demonstrates functional programming and enhanced random operations.
     */
    private int generateWeatherCode() {
        var rand = random.nextDouble();
        
        // Using modern switch expressions with pattern matching
        return switch ((int) (rand * 10)) {
            case 0, 1, 2, 3 -> random.nextInt(4); // Clear to partly cloudy (0-3) - 40%
            case 4, 5 -> 45 + random.nextInt(4);  // Fog (45-48) - 20%
            case 6, 7 -> 51 + random.nextInt(17); // Rain (51-67) - 20%
            case 8 -> 71 + random.nextInt(7);     // Snow (71-77) - 10%
            case 9 -> 80 + random.nextInt(20);    // Thunderstorms (80-99) - 10%
            default -> 0; // Clear sky as fallback
        };
    }
}