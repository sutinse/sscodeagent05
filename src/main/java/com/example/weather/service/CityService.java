package com.example.weather.service;

import com.example.weather.model.City;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class CityService {
    
    private static final Logger LOG = Logger.getLogger(CityService.class);
    
    // Using modern Map.of() for immutable map initialization (Java 9+)
    private final Map<String, City> cities = Map.of(
        "helsinki", new City("Helsinki", 60.1699, 24.9384),
        "espoo", new City("Espoo", 60.2055, 24.6559),
        "vantaa", new City("Vantaa", 60.2934, 25.0378),
        "turku", new City("Turku", 60.4518, 22.2666),
        "tampere", new City("Tampere", 61.4978, 23.7610),
        "jyväskylä", new City("Jyväskylä", 62.2415, 25.7209),
        "kuopio", new City("Kuopio", 62.8924, 27.6770),
        "oulu", new City("Oulu", 65.0121, 25.4651)
    );
    
    public CityService() {
        LOG.infof("Initialized %d cities", cities.size());
    }
    
    public Optional<City> findCityByCode(String cityCode) {
        var normalizedCode = cityCode.toLowerCase(); // Using var for clear type inference
        LOG.infof("Looking for city with code: '%s' (normalized: '%s')", cityCode, normalizedCode);
        
        return Optional.ofNullable(cities.get(normalizedCode))
                .map(city -> {
                    LOG.infof("Found city: %s", city.name());
                    return city;
                })
                .or(() -> {
                    LOG.warnf("City not found for code: %s", cityCode);
                    return Optional.empty();
                });
    }
    
    public Map<String, City> getAllCities() {
        return cities; // Return immutable map directly
    }
}