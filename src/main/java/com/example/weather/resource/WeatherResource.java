package com.example.weather.resource;

import com.example.weather.model.WeatherData;
import com.example.weather.service.MockWeatherService;
import com.example.weather.service.CityService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Optional;

@Path("/weather")
@Produces(MediaType.APPLICATION_JSON)
public class WeatherResource {

    @Inject
    MockWeatherService weatherService;

    @Inject
    CityService cityService;

    @GET
    @Path("/{cityCode}")
    public Response getWeatherByCity(@PathParam("cityCode") String cityCode) {
        Optional<WeatherData> weatherData = weatherService.getWeatherByCityCode(cityCode);
        
        if (weatherData.isPresent()) {
            return Response.ok(weatherData.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"City not found: " + cityCode + "\"}")
                    .build();
        }
    }

    @GET
    @Path("/cities")
    public Response getAllCities() {
        return Response.ok(cityService.getAllCities()).build();
    }
}