package com.example.weather.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class WeatherData {
    private String cityName;
    private double latitude;
    private double longitude;
    private List<HourlyWeather> hourlyWeather;

    public WeatherData() {}

    public WeatherData(String cityName, double latitude, double longitude, List<HourlyWeather> hourlyWeather) {
        this.cityName = cityName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.hourlyWeather = hourlyWeather;
    }

    @JsonProperty("cityName")
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @JsonProperty("latitude")
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @JsonProperty("longitude")
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @JsonProperty("hourlyWeather")
    public List<HourlyWeather> getHourlyWeather() {
        return hourlyWeather;
    }

    public void setHourlyWeather(List<HourlyWeather> hourlyWeather) {
        this.hourlyWeather = hourlyWeather;
    }

    public static class HourlyWeather {
        private String time;
        private double temperature;
        private int weatherCode;

        public HourlyWeather() {}

        public HourlyWeather(String time, double temperature, int weatherCode) {
            this.time = time;
            this.temperature = temperature;
            this.weatherCode = weatherCode;
        }

        @JsonProperty("time")
        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        @JsonProperty("temperature")
        public double getTemperature() {
            return temperature;
        }

        public void setTemperature(double temperature) {
            this.temperature = temperature;
        }

        @JsonProperty("weatherCode")
        public int getWeatherCode() {
            return weatherCode;
        }

        public void setWeatherCode(int weatherCode) {
            this.weatherCode = weatherCode;
        }
    }
}