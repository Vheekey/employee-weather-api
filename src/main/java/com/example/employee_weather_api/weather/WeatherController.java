package com.example.employee_weather_api.weather;

import com.example.employee_weather_api.weather.dto.WeatherSummary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {
    private static final int DEFAULT_FORECAST_DAYS = 1;
    private static final DateTimeFormatter HISTORY_DATE_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd")
            .withResolverStyle(ResolverStyle.STRICT);
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/current/{location}")
    public ResponseEntity<WeatherSummary> getCurrentWeather(@PathVariable String location) {
        WeatherSummary response = weatherService.getCurrentWeather(location);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping({"/forecast/{location}", "/forecast/{location}/{days}"})
    public ResponseEntity<WeatherSummary> getWeatherForecast(
            @PathVariable String location,
            @PathVariable(required = false) Integer days
    ) {
        int forecastDays = days == null ? DEFAULT_FORECAST_DAYS : days;
        this.validateForecastDays(forecastDays);
        WeatherSummary response = weatherService.getWeatherForecast(location, forecastDays);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/history/{location}/{date}")
    public ResponseEntity<WeatherSummary> getWeatherHistory(@PathVariable String location, @PathVariable String date) {
        LocalDate parsedDate = this.parseHistoryDate(date);
        WeatherSummary response = weatherService.getWeatherHistory(location, parsedDate);

        return ResponseEntity.ok().body(response);
    }

    private LocalDate parseHistoryDate(String date) {
        try {
            return LocalDate.parse(date, HISTORY_DATE_FORMATTER);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("date must be in yyyy-MM-dd format");
        }
    }

    private void validateForecastDays(int days) {
        if (days < 1) {
            throw new IllegalArgumentException("days must be greater than 0");
        }
    }
}
