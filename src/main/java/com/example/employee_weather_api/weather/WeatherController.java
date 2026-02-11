package com.example.employee_weather_api.weather;

import com.example.employee_weather_api.weather.dto.WeatherSummary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/current/{location}")
    public ResponseEntity<WeatherSummary> getCurrentWeather(@PathVariable String location) {
        WeatherSummary response = weatherService.getCurrentWeather(location);

        return ResponseEntity.ok().body(response);
    }
}
