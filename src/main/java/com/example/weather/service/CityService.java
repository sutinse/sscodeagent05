package com.example.weather.service;

import com.example.weather.model.City;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class CityService {
    
    private static final Logger LOG = Logger.getLogger(CityService.class);
    private final Map<String, City> cities = new HashMap<>();
    
    public CityService() {
        initializeCities();
    }
    
    private void initializeCities() {
        cities.put("helsinki", new City("Helsinki", 60.1699, 24.9384));
        cities.put("espoo", new City("Espoo", 60.2055, 24.6559));
        cities.put("vantaa", new City("Vantaa", 60.2934, 25.0378));
        cities.put("turku", new City("Turku", 60.4518, 22.2666));
        cities.put("tampere", new City("Tampere", 61.4978, 23.7610));
        cities.put("jyväskylä", new City("Jyväskylä", 62.2415, 25.7209));
        cities.put("kuopio", new City("Kuopio", 62.8924, 27.6770));
        cities.put("oulu", new City("Oulu", 65.0121, 25.4651));
        
        LOG.infof("Initialized %d cities", cities.size());
    }
    
    public Optional<City> findCityByCode(String cityCode) {
        String lowerCaseCode = cityCode.toLowerCase();
        LOG.infof("Looking for city with code: '%s' (normalized: '%s')", cityCode, lowerCaseCode);
        City city = cities.get(lowerCaseCode);
        if (city != null) {
            LOG.infof("Found city: %s", city.getName());
            return Optional.of(city);
        } else {
            LOG.warnf("City not found for code: %s", cityCode);
            return Optional.empty();
        }
    }
    
    public Map<String, City> getAllCities() {
        return new HashMap<>(cities);
    }
}