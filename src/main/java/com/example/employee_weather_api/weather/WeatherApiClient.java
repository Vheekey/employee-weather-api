package com.example.employee_weather_api.weather;

import com.example.employee_weather_api.weather.dto.WeatherHistoryResponse;
import com.example.employee_weather_api.weather.dto.WeatherTemperatureResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class WeatherApiClient {
	private static final DateTimeFormatter HISTORY_DATE_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd");
	private final RestTemplate restTemplate;
	private final WeatherApiProperties properties;

	public WeatherApiClient(RestTemplate restTemplate, WeatherApiProperties properties) {
		this.restTemplate = restTemplate;
		this.properties = properties;
	}

	public WeatherTemperatureResponse getCurrent(String location) {
		String url = properties.getBaseUrl()
				+ properties.getCurrent()
				+ "?key={key}&q={q}";
		return restTemplate.getForObject(url, WeatherTemperatureResponse.class, properties.getKey(), location);
	}

	public WeatherHistoryResponse getForecast(String location, int days) {
		String url = properties.getBaseUrl()
				+ properties.getForecast()
				+ "?key={key}&q={q}&days={days}";

		return restTemplate.getForObject(url, WeatherHistoryResponse.class,  properties.getKey(), location, days);
	}

	public WeatherHistoryResponse getHistory(String location, LocalDate dt) {
		String url = properties.getBaseUrl()
				+properties.getHistory()
				+ "?key={key}&q={q}&dt={dt}";

		return restTemplate.getForObject(
				url,
				WeatherHistoryResponse.class,
				properties.getKey(),
				location,
				dt.format(HISTORY_DATE_FORMATTER)
		);
	}
}
