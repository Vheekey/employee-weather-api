package com.example.employee_weather_api.weather.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ForecastHourDto {
	private String time;
	@JsonAlias("temp_c")
	private double tempC;
}
