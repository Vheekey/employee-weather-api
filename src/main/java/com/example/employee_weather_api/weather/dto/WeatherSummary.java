package com.example.employee_weather_api.weather.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record WeatherSummary(String locationName, Double tempC, List<ForecastDayDto> forecastDays) {
	public WeatherSummary(String locationName, double tempC) {
		this(locationName, tempC, null);
	}

	public WeatherSummary(String locationName, List<ForecastDayDto> forecastDays) {
		this(locationName, null, forecastDays);
	}

}
