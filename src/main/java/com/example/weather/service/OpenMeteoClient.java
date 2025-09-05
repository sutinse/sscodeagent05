package com.example.weather.service;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import com.example.weather.model.WeatherResponse;

@RegisterRestClient(configKey = "openmeteo-api")
public interface OpenMeteoClient {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    WeatherResponse getWeatherData(
        @QueryParam("latitude") double latitude,
        @QueryParam("longitude") double longitude,
        @QueryParam("hourly") String hourly,
        @QueryParam("timezone") String timezone
    );
}