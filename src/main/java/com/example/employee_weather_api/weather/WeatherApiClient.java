package com.example.employee_weather_api.weather;

import com.example.employee_weather_api.weather.dto.WeatherTemperatureResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherApiClient {
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

	public WeatherTemperatureResponse getForecast(String location) {
		String url = properties.getBaseUrl()
				+ properties.getForecast()
				+ "?key={key}&q={q}";

		return restTemplate.getForObject(url, WeatherTemperatureResponse.class,  properties.getKey(), location);
	}
}
