package com.example.employee_weather_api.weather.dto;

import lombok.Getter;

@Getter
public class WeatherSummary {
	private final String locationName;
	private final double tempC;

	public WeatherSummary(String locationName, double tempC) {
		this.locationName = locationName;
		this.tempC = tempC;
	}

}
