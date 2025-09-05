package com.example.weather.service;

import com.example.weather.model.City;
import com.example.weather.model.WeatherData;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@ApplicationScoped
public class MockWeatherService {

    private static final Logger LOG = Logger.getLogger(MockWeatherService.class);
    private final Random random = new Random();

    @Inject
    CityService cityService;

    public Optional<WeatherData> getWeatherByCityCode(String cityCode) {
        LOG.infof("Looking for weather data for city code: %s", cityCode);
        
        Optional<City> cityOpt = cityService.findCityByCode(cityCode);
        
        if (cityOpt.isEmpty()) {
            LOG.warnf("City not found: %s", cityCode);
            return Optional.empty();
        }
        
        City city = cityOpt.get();
        LOG.infof("Found city: %s at coordinates (%f, %f)", city.getName(), city.getLatitude(), city.getLongitude());
        
        // Generate mock weather data
        WeatherData mockWeatherData = generateMockWeatherData(city);
        LOG.infof("Generated mock weather data for city %s", city.getName());
        
        return Optional.of(mockWeatherData);
    }

    private WeatherData generateMockWeatherData(City city) {
        List<WeatherData.HourlyWeather> hourlyWeatherList = new ArrayList<>();
        
        // Generate weather data for next 24 hours
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        
        for (int i = 0; i < 24; i++) {
            LocalDateTime time = now.plusHours(i);
            String timeStr = time.format(formatter);
            
            // Generate realistic temperature based on latitude and season
            double baseTemp = generateBaseTemperature(city.getLatitude());
            double hourlyVariation = (random.nextDouble() - 0.5) * 6; // ±3°C variation
            double temperature = baseTemp + hourlyVariation;
            
            // Generate weather code (0-3 clear, 45-48 fog, 51-67 rain, 71-77 snow, 80-99 storms)
            int weatherCode = generateWeatherCode();
            
            hourlyWeatherList.add(new WeatherData.HourlyWeather(timeStr, temperature, weatherCode));
        }
        
        return new WeatherData(
            city.getName(),
            city.getLatitude(),
            city.getLongitude(),
            hourlyWeatherList
        );
    }

    private double generateBaseTemperature(double latitude) {
        // Base temperature calculation based on latitude and current season
        // Simplified calculation: colder further north
        double baseTemp = 20 - (latitude - 60) * 2; // Base around 20°C for Helsinki area
        
        // Add some seasonal variation (simplified)
        int monthValue = LocalDateTime.now().getMonthValue();
        if (monthValue >= 12 || monthValue <= 2) {
            // Winter
            baseTemp -= 15;
        } else if (monthValue >= 3 && monthValue <= 5) {
            // Spring
            baseTemp -= 5;
        } else if (monthValue >= 6 && monthValue <= 8) {
            // Summer
            baseTemp += 5;
        } else {
            // Autumn
            baseTemp -= 2;
        }
        
        return baseTemp;
    }

    private int generateWeatherCode() {
        // Generate realistic weather codes
        double rand = random.nextDouble();
        
        if (rand < 0.4) {
            return random.nextInt(4); // Clear to partly cloudy (0-3)
        } else if (rand < 0.6) {
            return 45 + random.nextInt(4); // Fog (45-48)
        } else if (rand < 0.8) {
            return 51 + random.nextInt(17); // Rain (51-67)
        } else if (rand < 0.9) {
            return 71 + random.nextInt(7); // Snow (71-77)
        } else {
            return 80 + random.nextInt(20); // Thunderstorms (80-99)
        }
    }
}