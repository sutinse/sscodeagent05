package com.example.weather.service;

import com.example.weather.model.City;
import com.example.weather.model.WeatherData;
import com.example.weather.model.WeatherResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        
        Optional<City> cityOpt = cityService.findCityByCode(cityCode);
        
        if (cityOpt.isEmpty()) {
            LOG.warnf("City not found: %s", cityCode);
            return Optional.empty();
        }
        
        City city = cityOpt.get();
        LOG.infof("Found city: %s at coordinates (%f, %f)", city.getName(), city.getLatitude(), city.getLongitude());
        
        try {
            LOG.infof("Calling OpenMeteo API for coordinates: lat=%f, lon=%f", city.getLatitude(), city.getLongitude());
            WeatherResponse response = openMeteoClient.getWeatherData(
                city.getLatitude(),
                city.getLongitude(),
                "temperature_2m,weather_code",
                "auto"
            );
            
            LOG.infof("Successfully received weather data from OpenMeteo API");
            return Optional.of(convertToWeatherData(city, response));
        } catch (Exception e) {
            LOG.errorf(e, "Error fetching weather data for city %s", cityCode);
            return Optional.empty();
        }
    }

    private WeatherData convertToWeatherData(City city, WeatherResponse response) {
        List<WeatherData.HourlyWeather> hourlyWeatherList = new ArrayList<>();
        
        if (response.getHourly() != null) {
            List<String> times = response.getHourly().getTime();
            List<Double> temperatures = response.getHourly().getTemperature2m();
            List<Integer> weatherCodes = response.getHourly().getWeatherCode();
            
            int size = Math.min(Math.min(times.size(), temperatures.size()), weatherCodes.size());
            
            for (int i = 0; i < size; i++) {
                hourlyWeatherList.add(new WeatherData.HourlyWeather(
                    times.get(i),
                    temperatures.get(i),
                    weatherCodes.get(i)
                ));
            }
        }
        
        return new WeatherData(
            city.getName(),
            city.getLatitude(),
            city.getLongitude(),
            hourlyWeatherList
        );
    }
}