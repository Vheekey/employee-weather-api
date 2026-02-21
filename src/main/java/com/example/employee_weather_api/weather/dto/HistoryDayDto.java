package com.example.employee_weather_api.weather.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HistoryDayDto {
	@JsonAlias("avgtemp_c")
	private double avgTempC;
	@JsonAlias("maxtemp_c")
	private double maxTempC;
	@JsonAlias("mintemp_c")
	private double minTempC;
}
