# Java Best Practices & Coding Instructions (JDK 21)

## Project Overview
This document outlines the Java best practices and modern coding patterns implemented in this Quarkus weather application, leveraging features available up to JDK 21.

## Core Java Best Practices Applied

### 1. Records for Immutable Data (Java 14+)

#### **Replace JavaBeans with Records**
```java
// ❌ Old approach - Traditional JavaBean
public class City {
    private String name;
    private double latitude;
    private double longitude;
    // ... getters, setters, equals, hashCode, toString
}

// ✅ Modern approach - Record
public record City(
    @JsonProperty("name") String name,
    @JsonProperty("latitude") double latitude,
    @JsonProperty("longitude") double longitude
) {
    // Automatic equals(), hashCode(), toString(), and accessors
    
    // Compact constructor with validation
    public City {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("City name cannot be null or blank");
        }
        // ... additional validation
    }
}
```

#### **Benefits of Records:**
- **Immutability by default** - Thread-safe and prevents accidental mutations
- **Reduced boilerplate** - No need for getters, setters, equals, hashCode, toString
- **Data transparency** - Clear intent that this is a data carrier
- **Pattern matching support** - Works seamlessly with modern Java features

### 2. Enhanced Optional Usage (Java 8+, improved in 9+)

#### **Functional Optional Chaining**
```java
// ❌ Old approach
public Optional<WeatherData> getWeatherByCityCode(String cityCode) {
    Optional<City> cityOpt = cityService.findCityByCode(cityCode);
    if (cityOpt.isEmpty()) {
        LOG.warn("City not found");
        return Optional.empty();
    }
    City city = cityOpt.get();
    // ... process city
}

// ✅ Modern approach - Functional chaining
public Optional<WeatherData> getWeatherByCityCode(String cityCode) {
    return cityService.findCityByCode(cityCode)
            .map(this::fetchWeatherData)
            .orElseGet(() -> {
                LOG.warnf("City not found: %s", cityCode);
                return Optional.empty();
            });
}
```

#### **Enhanced Optional Methods (Java 9+):**
- `ifPresentOrElse()` - Execute different actions based on presence
- `or()` - Provide alternative Optional if empty
- `stream()` - Convert Optional to Stream for complex processing

### 3. Modern Collections and Stream API (Java 8+, enhanced in 16+)

#### **Immutable Collections (Java 9+)**
```java
// ❌ Old approach
private final Map<String, City> cities = new HashMap<>();
// ... populate map
return new HashMap<>(cities); // Defensive copy

// ✅ Modern approach - Immutable from creation
private final Map<String, City> cities = Map.of(
    "helsinki", new City("Helsinki", 60.1699, 24.9384),
    "espoo", new City("Espoo", 60.2055, 24.6559),
    // ...
);
return cities; // Already immutable
```

#### **Enhanced Stream Processing (Java 16+)**
```java
// ❌ Old approach
List<WeatherData.HourlyWeather> result = new ArrayList<>();
for (int i = 0; i < minSize; i++) {
    result.add(new WeatherData.HourlyWeather(...));
}
return result;

// ✅ Modern approach - Stream with toList()
return IntStream.range(0, minSize)
        .mapToObj(i -> new WeatherData.HourlyWeather(...))
        .toList(); // Java 16+ - returns immutable list
```

### 4. Modern Switch Expressions (Java 14+)

#### **Switch Expressions with Yield**
```java
// ❌ Old approach
String description;
switch (weatherCode) {
    case 0:
        description = "Clear sky";
        break;
    case 1:
    case 2:
    case 3:
        description = "Partly cloudy";
        break;
    default:
        if (weatherCode >= 51 && weatherCode <= 67) {
            description = "Rainy";
        } else {
            description = "Unknown";
        }
}

// ✅ Modern approach - Switch expressions
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
            } else {
                yield "Unknown weather condition";
            }
        }
    };
}
```

### 5. Local Variable Type Inference (Java 10+)

#### **Strategic Use of `var`**
```java
// ✅ Good use - Complex generic types
var response = openMeteoClient.getWeatherData(...);
var hourlyWeatherList = IntStream.range(0, 24)
        .mapToObj(hour -> generateHourlyWeather(...))
        .toList();

// ✅ Good use - Clear context
var normalizedCode = cityCode.toLowerCase();
var now = LocalDateTime.now();

// ❌ Avoid - Loss of clarity
var x = getData(); // What type is x?
var result = process(); // Unclear return type
```

#### **Guidelines for `var` Usage:**
- Use when the type is obvious from the right-hand side
- Use for complex generic types to improve readability
- Avoid when it makes code less clear
- Always prefer explicit types in public APIs

### 6. Enhanced String Processing (Java 11+)

#### **Modern String Methods**
```java
// Java 11+ String enhancements
if (cityName.isBlank()) { ... }     // More precise than isEmpty()
var lines = multiLineString.lines(); // Stream of lines
var stripped = value.strip();        // Unicode-aware whitespace removal
var repeated = pattern.repeat(5);    // String repetition

// Java 15+ Text Blocks for multiline strings
String jsonTemplate = """
    {
        "cityName": "%s",
        "temperature": %.1f,
        "description": "%s"
    }
    """;
```

### 7. Modern Collection Features (Java 21)

#### **New List Methods (Java 21)**
```java
// Java 21 enhancements
var firstElement = list.getFirst();  // Instead of get(0)
var lastElement = list.getLast();    // Instead of get(size()-1)
var reversedList = list.reversed();  // Immutable reversed view
```

### 8. Functional Programming Patterns

#### **Function Composition and Method References**
```java
// ✅ Functional approach with method references
return hourlyWeather.stream()
        .mapToDouble(HourlyWeather::temperature)
        .average();

// ✅ Function composition
return cityService.findCityByCode(cityCode)
        .map(this::fetchWeatherData)
        .flatMap(Optional::stream);
```

### 9. Validation and Error Handling

#### **Fail-Fast Validation in Records**
```java
public record City(String name, double latitude, double longitude) {
    public City {
        // Compact constructor validation - fails fast
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("City name cannot be null or blank");
        }
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        }
        // ... more validations
    }
}
```

#### **Optional-Based Error Handling**
```java
// ✅ Functional error handling
return Optional.ofNullable(response.getHourly())
        .map(this::convertHourlyData)
        .orElse(List.of()); // Safe default
```

## Code Quality Guidelines

### 1. Immutability First
- Prefer immutable data structures (Records, `List.of()`, `Map.of()`)
- Use `final` for variables that shouldn't change
- Create defensive copies when necessary

### 2. Null Safety
- Use `Optional` for values that might be absent
- Validate inputs early (fail-fast principle)
- Use null-safe operations (`Objects.equals()`, `Objects.hashCode()`)

### 3. Clear Intent
- Use descriptive variable names with `var` when type is obvious
- Prefer method references over lambda expressions when possible
- Use modern collection factory methods for clarity

### 4. Performance Considerations
- Use immutable collections from Java 9+ (more memory efficient)
- Prefer Stream API for complex data transformations
- Use `toList()` instead of `collect(Collectors.toList())` for better performance

### 5. Modern Java Adoption Strategy
- Migrate JavaBeans to Records for data classes
- Replace traditional switch statements with switch expressions
- Use text blocks for multiline strings
- Adopt new collection methods for cleaner code

## Testing Best Practices

### Modern Testing Patterns
```java
@Test
void testRecordValidation() {
    // ✅ Test record validation
    assertThrows(IllegalArgumentException.class, 
        () -> new City("", 60.0, 24.0));
}

@Test 
void testOptionalHandling() {
    // ✅ Test Optional-based APIs
    var result = service.getWeatherByCityCode("nonexistent");
    assertTrue(result.isEmpty());
}
```

## Migration Guidelines

### From Legacy Code to Modern Java

1. **Phase 1: Collections and Streams**
   - Replace manual loops with Stream API
   - Use immutable collections (`List.of()`, `Map.of()`)
   - Apply method references where appropriate

2. **Phase 2: Data Classes to Records**
   - Identify simple data classes (DTOs, value objects)
   - Convert to records with validation
   - Update dependent code to use record accessors

3. **Phase 3: Control Flow Modernization**
   - Replace switch statements with switch expressions
   - Use text blocks for multiline strings
   - Apply pattern matching where available

4. **Phase 4: Optional and Functional Patterns**
   - Replace null checks with Optional chains
   - Use functional composition for complex operations
   - Apply modern error handling patterns

## Architecture Benefits

### Why These Patterns Matter

1. **Maintainability**: Immutable data structures prevent bugs
2. **Readability**: Modern syntax expresses intent clearly
3. **Performance**: New collection methods are optimized
4. **Safety**: Compile-time checks catch errors early
5. **Testability**: Pure functions and immutable data are easier to test

### Integration with Quarkus

- Records work seamlessly with Jackson JSON serialization
- CDI dependency injection works with modern Java patterns
- Rest endpoints benefit from type safety and immutability
- Configuration and validation integrate well with records

## Conclusion

This codebase demonstrates how modern Java features up to JDK 21 can significantly improve code quality, maintainability, and developer productivity while maintaining excellent performance and integration with enterprise frameworks like Quarkus.

The key is progressive adoption: start with the most impactful changes (immutable collections, records for data classes) and gradually adopt more advanced features as the team becomes comfortable with the patterns.