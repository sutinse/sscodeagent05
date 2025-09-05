package com.example.weather.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Immutable city data using Java 14+ Record pattern.
 * Records provide automatic equals(), hashCode(), toString(), and accessors.
 */
public record City(
    @JsonProperty("name") String name,
    @JsonProperty("latitude") double latitude,
    @JsonProperty("longitude") double longitude
) {
    
    /**
     * Compact constructor with validation for Java records.
     * Ensures data integrity at construction time.
     */
    public City {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("City name cannot be null or blank");
        }
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        }
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        }
    }
}