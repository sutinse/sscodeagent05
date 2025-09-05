package com.example.weather.service;

import com.example.weather.model.City;
import com.example.weather.model.WeatherData;
import com.example.weather.model.WeatherResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@ApplicationScoped
public class WeatherService {

    private static final Logger LOG = Logger.getLogger(WeatherService.class);

    @Inject
    CityService cityService;

    @Inject
    @RestClient
    OpenMeteoClient openMeteoClient;

    public Optional<WeatherData> getWeatherByCityCode(String cityCode) {
        LOG.infof("Looking for weather data for city code: %s", cityCode);
        
        return cityService.findCityByCode(cityCode)
                .map(this::fetchWeatherData)
                .orElseGet(() -> {
                    LOG.warnf("City not found: %s", cityCode);
                    return Optional.empty();
                });
    }

    private Optional<WeatherData> fetchWeatherData(City city) {
        LOG.infof("Found city: %s at coordinates (%f, %f)", 
                city.name(), city.latitude(), city.longitude());
        
        try {
            LOG.infof("Calling OpenMeteo API for coordinates: lat=%f, lon=%f", 
                    city.latitude(), city.longitude());
            
            var response = openMeteoClient.getWeatherData(
                city.latitude(),
                city.longitude(),
                "temperature_2m,weather_code",
                "auto"
            );
            
            LOG.infof("Successfully received weather data from OpenMeteo API");
            return Optional.of(convertToWeatherData(city, response));
        } catch (Exception e) {
            LOG.errorf(e, "Error fetching weather data for city %s", city.name());
            return Optional.empty();
        }
    }

    /**
     * Modern conversion method using enhanced Stream API and functional programming.
     * Demonstrates pattern matching and modern collection processing.
     */
    private WeatherData convertToWeatherData(City city, WeatherResponse response) {
        var hourlyWeatherList = Optional.ofNullable(response.getHourly())
                .map(this::convertHourlyData)
                .orElse(List.of());
        
        return new WeatherData(
            city.name(),
            city.latitude(),
            city.longitude(),
            hourlyWeatherList
        );
    }
    
    /**
     * Enhanced method using modern Stream API with zip operation simulation.
     * Demonstrates functional programming patterns available in Java 21.
     */
    private List<WeatherData.HourlyWeather> convertHourlyData(WeatherResponse.HourlyData hourly) {
        var times = Optional.ofNullable(hourly.getTime()).orElse(List.of());
        var temperatures = Optional.ofNullable(hourly.getTemperature2m()).orElse(List.of());
        var weatherCodes = Optional.ofNullable(hourly.getWeatherCode()).orElse(List.of());
        
        // Modern approach using IntStream and minimum size calculation
        int minSize = List.of(times.size(), temperatures.size(), weatherCodes.size())
                .stream()
                .mapToInt(Integer::intValue)
                .min()
                .orElse(0);
        
        return IntStream.range(0, minSize)
                .mapToObj(i -> new WeatherData.HourlyWeather(
                    times.get(i),
                    temperatures.get(i),
                    weatherCodes.get(i)
                ))
                .toList(); // Modern toList() method (Java 16+)
    }
}