package com.example.weather.service;

import com.example.weather.model.WeatherData;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import java.util.Optional;

@ApplicationScoped
public class WeatherServiceSelector {

    @Inject
    WeatherService realWeatherService;

    @Inject
    MockWeatherService mockWeatherService;

    @ConfigProperty(name = "weather.service.use-mock", defaultValue = "true")
    boolean useMockService;

    public Optional<WeatherData> getWeatherByCityCode(String cityCode) {
        if (useMockService) {
            return mockWeatherService.getWeatherByCityCode(cityCode);
        } else {
            return realWeatherService.getWeatherByCityCode(cityCode);
        }
    }
}