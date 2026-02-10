package com.example.employee_weather_api.weather;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "weather.api")
public class WeatherApiProperties {
	private String key;
	private String baseUrl;
	private String current;
	private String forecast;
	private String history;
}
