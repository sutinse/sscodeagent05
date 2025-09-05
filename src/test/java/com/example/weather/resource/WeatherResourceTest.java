package com.example.weather.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class WeatherResourceTest {

    @Test
    public void testGetCitiesEndpoint() {
        given()
          .when().get("/weather/cities")
          .then()
             .statusCode(200)
             .body("helsinki", notNullValue())
             .body("helsinki.name", is("Helsinki"))
             .body("helsinki.latitude", is(60.1699f))
             .body("helsinki.longitude", is(24.9384f));
    }

    @Test
    public void testGetWeatherForValidCity() {
        // This test uses the mock weather service
        given()
          .when().get("/weather/helsinki")
          .then()
             .statusCode(200)
             .body("cityName", is("Helsinki"))
             .body("latitude", is(60.1699f))
             .body("longitude", is(24.9384f))
             .body("hourlyWeather", notNullValue())
             .body("hourlyWeather.size()", is(24)); // 24 hours of data
    }

    @Test
    public void testGetWeatherForInvalidCity() {
        given()
          .when().get("/weather/nonexistentcity")
          .then()
             .statusCode(404);
    }
}