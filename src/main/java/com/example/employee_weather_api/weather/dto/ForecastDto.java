package com.example.employee_weather_api.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Setter
@Getter
public class ForecastDto {
	@JsonProperty("forecastday")
	private List<ForecastDayDto> forecastDays;

	public List<ForecastDayDto> days() {
		return forecastDays == null ? Collections.emptyList() : forecastDays;
	}
}
