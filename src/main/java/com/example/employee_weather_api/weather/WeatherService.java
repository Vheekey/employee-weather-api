package com.example.employee_weather_api.weather;

import com.example.employee_weather_api.weather.dto.ForecastDayDto;
import com.example.employee_weather_api.weather.dto.WeatherSummary;
import com.example.employee_weather_api.weather.dto.WeatherHistoryResponse;
import com.example.employee_weather_api.weather.dto.WeatherTemperatureResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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

	public WeatherSummary getWeatherForecast(String location, int days) {
		this.validateLocation(location);
		this.validateForecastDays(days);

		WeatherHistoryResponse response = weatherApiClient.getForecast(location, days);
		this.validateWeatherHistoryResponse(response);

		return this.getWeatherForecastSummary(response);
	}

	public WeatherSummary getWeatherHistory(String location, LocalDate date) {
		this.validateLocation(location);

		WeatherHistoryResponse response = weatherApiClient.getHistory(location, date);
		this.validateWeatherHistoryResponse(response);

		return this.getWeatherHistorySummary(response);
	}

	private void validateLocation(String location) {
		if (location == null || location.isBlank()) {
			throw new IllegalArgumentException("location is required");
		}
	}

	private void validateForecastDays(int days) {
		if (days < 1) {
			throw new IllegalArgumentException("days must be greater than 0");
		}
	}

	private void validateWeatherResponse(WeatherTemperatureResponse response) {
		if (response == null || response.getLocation() == null || response.getCurrent() == null) {
			throw new IllegalStateException("weather response missing required data");
		}
	}

	private void validateWeatherHistoryResponse(WeatherHistoryResponse response) {
		if (response == null || response.getLocation() == null || response.getForecast() == null
				|| response.getForecast().days().isEmpty()) {
			throw new IllegalStateException("weather history response missing required data");
		}
		ForecastDayDto firstForecastDay = response.getForecast().days().getFirst();
		if (firstForecastDay == null || firstForecastDay.getDay() == null) {
			throw new IllegalStateException("weather history response missing required day data");
		}
	}

	private WeatherSummary getWeatherSummary(WeatherTemperatureResponse response) {
		return new WeatherSummary(
				response.getLocation().getName(),
				response.getCurrent().getTempC());
	}

	private WeatherSummary getWeatherForecastSummary(WeatherHistoryResponse response) {
		List<ForecastDayDto> forecastDays = response.getForecast().days();

		return new WeatherSummary(
				response.getLocation().getName(),
				forecastDays);
	}

	private WeatherSummary getWeatherHistorySummary(WeatherHistoryResponse response) {
		List<ForecastDayDto> historyDays = response.getForecast().days();
		return new WeatherSummary(
				response.getLocation().getName(),
				historyDays);
	}
}
