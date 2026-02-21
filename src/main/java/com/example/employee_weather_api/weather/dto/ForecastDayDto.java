package com.example.employee_weather_api.weather.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Setter
@Getter
public class ForecastDayDto {
	private String date;
	@JsonAlias("date_epoch")
	private long dateEpoch;
	private HistoryDayDto day;
	private List<ForecastHourDto> hour;

	public List<ForecastHourDto> hours() {
		return hour == null ? Collections.emptyList() : hour;
	}
}
