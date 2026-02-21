package com.example.employee_weather_api.weather.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WeatherHistoryResponse {
	private LocationDto location;
	private ForecastDto forecast;
}
