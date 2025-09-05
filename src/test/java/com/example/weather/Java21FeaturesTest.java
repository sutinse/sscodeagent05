package com.example.weather;

import com.example.weather.model.City;
import com.example.weather.model.WeatherData;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class to verify Java 21 features and modern Java best practices
 * implemented in the weather application.
 */
public class Java21FeaturesTest {

    @Test
    public void testJavaVersionIs21() {
        var javaVersion = System.getProperty("java.version");
        assertTrue(javaVersion.startsWith("21"), 
            "Expected Java 21, but found version: " + javaVersion);
    }

    @Test
    public void testTextBlocks() {
        // Text blocks - clean multiline strings (Java 15+)
        var json = """
            {
              "name": "Java 21 Test",
              "version": "21",
              "features": ["text-blocks", "pattern-matching", "records"]
            }
            """;
        
        assertNotNull(json);
        assertTrue(json.contains("Java 21 Test"));
        assertTrue(json.contains("\"version\": \"21\""));
    }

    @Test
    public void testRecordsWithValidation() {
        // Records with compact constructor validation
        record TestRecord(String name, int value) {
            public TestRecord {
                if (name == null || name.isBlank()) {
                    throw new IllegalArgumentException("Name cannot be blank");
                }
                if (value < 0) {
                    throw new IllegalArgumentException("Value must be positive");
                }
            }
        }
        
        var record = new TestRecord("test", 42);
        assertEquals("test", record.name());
        assertEquals(42, record.value());
        
        // Test validation
        assertThrows(IllegalArgumentException.class, 
            () -> new TestRecord("", 42));
        assertThrows(IllegalArgumentException.class, 
            () -> new TestRecord("test", -1));
    }

    @Test
    public void testCityRecordFunctionality() {
        // Test our actual City record
        var city = new City("Helsinki", 60.1699, 24.9384);
        
        assertEquals("Helsinki", city.name());
        assertEquals(60.1699, city.latitude());
        assertEquals(24.9384, city.longitude());
        
        // Test validation
        assertThrows(IllegalArgumentException.class, 
            () -> new City("", 60.0, 24.0));
        assertThrows(IllegalArgumentException.class, 
            () -> new City("Test", 91.0, 24.0)); // Invalid latitude
        assertThrows(IllegalArgumentException.class, 
            () -> new City("Test", 60.0, 181.0)); // Invalid longitude
    }

    @Test
    public void testSwitchExpressions() {
        // Modern switch expressions (Java 14+)
        var dayType = switch (1) {
            case 1, 2, 3, 4, 5 -> "weekday";
            case 6, 7 -> "weekend";
            default -> "unknown";
        };
        
        assertEquals("weekday", dayType);
        
        // Test weather description switch from our model
        var hourly = new WeatherData.HourlyWeather("2025-09-05T12:00", 20.0, 0);
        assertEquals("Clear sky", hourly.getWeatherDescription());
        
        var rainy = new WeatherData.HourlyWeather("2025-09-05T12:00", 15.0, 55);
        assertEquals("Rainy", rainy.getWeatherDescription());
    }

    @Test
    public void testModernCollections() {
        // Immutable collections (Java 9+)
        var cities = Map.of(
            "helsinki", new City("Helsinki", 60.1699, 24.9384),
            "turku", new City("Turku", 60.4518, 22.2666)
        );
        
        assertEquals(2, cities.size());
        assertThrows(UnsupportedOperationException.class, 
            () -> cities.put("oulu", new City("Oulu", 65.0121, 25.4651)));
        
        // Modern list operations (Java 21)
        var numbers = List.of(1, 2, 3, 4, 5);
        assertEquals(1, numbers.getFirst()); // Java 21
        assertEquals(5, numbers.getLast());  // Java 21
        
        var reversed = numbers.reversed();   // Java 21
        assertEquals(List.of(5, 4, 3, 2, 1), reversed);
    }

    @Test
    public void testStreamApiEnhancements() {
        var temperatures = List.of(15.5, 18.2, 22.1, 19.8, 16.3);
        
        // Modern Stream API with toList() (Java 16+)
        var filtered = temperatures.stream()
                .filter(temp -> temp > 17.0)
                .toList();
        
        assertEquals(3, filtered.size());
        assertTrue(filtered.contains(18.2));
        assertTrue(filtered.contains(22.1));
        assertTrue(filtered.contains(19.8));
        
        // Stream to Optional
        var average = temperatures.stream()
                .mapToDouble(Double::doubleValue)
                .average();
        
        assertTrue(average.isPresent());
        assertEquals(18.38, average.getAsDouble(), 0.01);
    }

    @Test
    public void testOptionalEnhancements() {
        // Modern Optional usage patterns
        Optional<String> value = Optional.of("test");
        Optional<String> empty = Optional.empty();
        
        // ifPresentOrElse (Java 9+)
        var result = new StringBuilder();
        value.ifPresentOrElse(
            result::append,
            () -> result.append("default")
        );
        assertEquals("test", result.toString());
        
        // or() method (Java 9+)
        var fallback = empty.or(() -> Optional.of("fallback"));
        assertTrue(fallback.isPresent());
        assertEquals("fallback", fallback.get());
        
        // stream() method (Java 9+)
        var streamResult = List.of(
                Optional.of("hello"),
                Optional.empty(),
                Optional.of("world")
            )
            .stream()
            .flatMap(Optional::stream)
            .toList();
        
        assertEquals(List.of("hello", "world"), streamResult);
    }

    @Test
    public void testStringEnhancements() {
        // Modern String methods (Java 11+)
        assertTrue("   ".isBlank());
        assertFalse("test".isBlank());
        
        var lines = """
            line1
            line2
            line3
            """.lines().toList();
        
        assertEquals(3, lines.size());
        assertEquals("line1", lines.get(0));
        
        // String formatting (Java 15+)
        var formatted = "Temperature: %.1f°C".formatted(18.5);
        assertEquals("Temperature: 18.5°C", formatted);
    }

    @Test
    public void testLocalVariableTypeInference() {
        // var usage for complex types
        var cityMap = Map.of(
            "helsinki", new City("Helsinki", 60.1699, 24.9384)
        );
        
        // Type is inferred as Map<String, City>
        assertTrue(cityMap.containsKey("helsinki"));
        
        var now = LocalDateTime.now();
        // Type is inferred as LocalDateTime
        assertNotNull(now);
        
        var weatherList = List.of(
            new WeatherData.HourlyWeather("2025-09-05T12:00", 20.0, 0),
            new WeatherData.HourlyWeather("2025-09-05T13:00", 22.0, 1)
        );
        
        // Type is inferred as List<WeatherData.HourlyWeather>
        assertEquals(2, weatherList.size());
    }

    @Test
    public void testWeatherDataRecordFeatures() {
        var hourlyData = List.of(
            new WeatherData.HourlyWeather("2025-09-05T12:00", 20.0, 0),
            new WeatherData.HourlyWeather("2025-09-05T13:00", 18.5, 45),
            new WeatherData.HourlyWeather("2025-09-05T14:00", 22.1, 55)
        );
        
        var weatherData = new WeatherData("Helsinki", 60.1699, 24.9384, hourlyData);
        
        // Test record accessors
        assertEquals("Helsinki", weatherData.cityName());
        assertEquals(3, weatherData.hourlyWeather().size());
        
        // Test enhanced methods
        var currentWeather = weatherData.getCurrentWeather();
        assertTrue(currentWeather.isPresent());
        assertEquals(20.0, currentWeather.get().temperature());
        
        var averageTemp = weatherData.getAverageTemperature();
        assertTrue(averageTemp.isPresent());
        assertEquals(20.2, averageTemp.getAsDouble(), 0.1);
        
        // Test immutability
        assertThrows(UnsupportedOperationException.class, 
            () -> weatherData.hourlyWeather().add(
                new WeatherData.HourlyWeather("2025-09-05T15:00", 25.0, 0)
            ));
    }

    @Test
    public void testFunctionalProgrammingPatterns() {
        var cities = List.of(
            new City("Helsinki", 60.1699, 24.9384),
            new City("Stockholm", 59.3293, 18.0686),
            new City("Oslo", 59.9139, 10.7522)
        );
        
        // Functional processing with method references
        var northernCities = cities.stream()
                .filter(city -> city.latitude() > 59.5)
                .map(City::name)
                .toList();
        
        assertEquals(2, northernCities.size());
        assertTrue(northernCities.contains("Helsinki"));
        assertTrue(northernCities.contains("Oslo"));
        
        // Functional composition
        var cityNameLengths = cities.stream()
                .map(City::name)
                .mapToInt(String::length)
                .sum();
        
        assertEquals(21, cityNameLengths); // "Helsinki" + "Stockholm" + "Oslo"
    }
}