package com.example.employee_weather_api.weather;

import com.example.employee_weather_api.weather.dto.ForecastDayDto;
import com.example.employee_weather_api.weather.dto.ForecastDto;
import com.example.employee_weather_api.weather.dto.HistoryDayDto;
import com.example.employee_weather_api.weather.dto.LocationDto;
import com.example.employee_weather_api.weather.dto.WeatherHistoryResponse;
import com.example.employee_weather_api.weather.dto.WeatherSummary;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class WeatherServiceTest {

	@Test
	void getWeatherForecast_usesNextDayAverageTemperatureWhenAvailable() {
		StubWeatherApiClient weatherApiClient = new StubWeatherApiClient();
		weatherApiClient.forecastResponse = forecastResponse("London", 5.1, 6.5);

		WeatherService service = new WeatherService(weatherApiClient);
		WeatherSummary summary = service.getWeatherForecast("London", 2);

		assertThat(summary.locationName()).isEqualTo("London");
		assertThat(summary.tempC()).isEqualTo(6.5);
		assertThat(summary.forecastDays()).hasSize(2);
		assertThat(weatherApiClient.lastLocation).isEqualTo("London");
		assertThat(weatherApiClient.lastForecastDays).isEqualTo(2);
	}

	@Test
	void getWeatherHistory_usesAverageDailyTemperature() {
		LocalDate requestedDate = LocalDate.of(2025, 1, 20);
		StubWeatherApiClient weatherApiClient = new StubWeatherApiClient();
		weatherApiClient.historyResponse = historyResponse("London", -2.4);

		WeatherService service = new WeatherService(weatherApiClient);
		WeatherSummary summary = service.getWeatherHistory("London", requestedDate);

		assertThat(summary.locationName()).isEqualTo("London");
		assertThat(summary.tempC()).isEqualTo(-2.4);
		assertThat(summary.forecastDays()).isNull();
		assertThat(weatherApiClient.lastLocation).isEqualTo("London");
		assertThat(weatherApiClient.lastDate).isEqualTo(requestedDate);
	}

	private WeatherHistoryResponse forecastResponse(String locationName, double dayOneAvgTempC, double dayTwoAvgTempC) {
		LocationDto location = new LocationDto();
		location.setName(locationName);

		HistoryDayDto dayOne = new HistoryDayDto();
		dayOne.setAvgTempC(dayOneAvgTempC);
		ForecastDayDto forecastDayOne = new ForecastDayDto();
		forecastDayOne.setDate("2025-01-20");
		forecastDayOne.setDay(dayOne);

		HistoryDayDto dayTwo = new HistoryDayDto();
		dayTwo.setAvgTempC(dayTwoAvgTempC);
		ForecastDayDto forecastDayTwo = new ForecastDayDto();
		forecastDayTwo.setDate("2025-01-21");
		forecastDayTwo.setDay(dayTwo);

		ForecastDto forecast = new ForecastDto();
		forecast.setForecastDays(List.of(forecastDayOne, forecastDayTwo));

		WeatherHistoryResponse response = new WeatherHistoryResponse();
		response.setLocation(location);
		response.setForecast(forecast);
		return response;
	}

	private WeatherHistoryResponse historyResponse(String locationName, double avgTempC) {
		LocationDto location = new LocationDto();
		location.setName(locationName);

		HistoryDayDto day = new HistoryDayDto();
		day.setAvgTempC(avgTempC);

		ForecastDayDto forecastDay = new ForecastDayDto();
		forecastDay.setDate("2025-01-20");
		forecastDay.setDay(day);

		ForecastDto forecast = new ForecastDto();
		forecast.setForecastDays(List.of(forecastDay));

		WeatherHistoryResponse response = new WeatherHistoryResponse();
		response.setLocation(location);
		response.setForecast(forecast);
		return response;
	}

	private static class StubWeatherApiClient extends WeatherApiClient {
		private String lastLocation;
		private LocalDate lastDate;
		private Integer lastForecastDays;
		private WeatherHistoryResponse forecastResponse;
		private WeatherHistoryResponse historyResponse;

		private StubWeatherApiClient() {
			super(null, null);
		}

		@Override
		public WeatherHistoryResponse getHistory(String location, LocalDate dt) {
			lastLocation = location;
			lastDate = dt;
			return historyResponse;
		}

		@Override
		public WeatherHistoryResponse getForecast(String location, int days) {
			lastLocation = location;
			lastForecastDays = days;
			return forecastResponse;
		}
	}
}
