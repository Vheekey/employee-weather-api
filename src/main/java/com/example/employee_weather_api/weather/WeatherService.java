package com.example.employee_weather_api.weather;

import com.example.employee_weather_api.weather.dto.WeatherSummary;
import com.example.employee_weather_api.weather.dto.WeatherTemperatureResponse;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {
	private final WeatherApiClient weatherApiClient;

	public WeatherService(WeatherApiClient weatherApiClient) {
		this.weatherApiClient = weatherApiClient;
	}

	public WeatherSummary getCurrentWeather(String location) {
		this.validateLocation(location);

		WeatherTemperatureResponse response = weatherApiClient.getCurrent(location);
		this.validateWeatherResponse(response);

		return this.getWeatherSummary(response);
	}

	public WeatherSummary getWeatherForecast(String location) {
		this.validateLocation(location);

		WeatherTemperatureResponse response = weatherApiClient.getForecast(location);
		this.validateWeatherResponse(response);

		return this.getWeatherSummary(response);
	}

	private void validateLocation(String location) {
		if (location == null || location.isBlank()) {
			throw new IllegalArgumentException("location is required");
		}
	}

	private void validateWeatherResponse(WeatherTemperatureResponse response) {
		if (response == null || response.getLocation() == null || response.getCurrent() == null) {
			throw new IllegalStateException("weather response missing required data");
		}
	}

	private WeatherSummary getWeatherSummary(WeatherTemperatureResponse response) {
		return new WeatherSummary(
				response.getLocation().getName(),
				response.getCurrent().getTempC());
	}
}
