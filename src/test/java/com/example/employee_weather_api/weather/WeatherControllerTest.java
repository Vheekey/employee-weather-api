package com.example.employee_weather_api.weather;

import com.example.employee_weather_api.exception.GlobalExceptionHandler;
import com.example.employee_weather_api.weather.dto.WeatherSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WeatherControllerTest {
	private StubWeatherService weatherService;
	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		weatherService = new StubWeatherService();
		WeatherController controller = new WeatherController(weatherService);
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
				.setControllerAdvice(new GlobalExceptionHandler())
				.build();
	}

	@Test
	void getWeatherForecast_usesDefaultDaysWhenNotProvided() throws Exception {
		weatherService.response = new WeatherSummary("London", 6.1);

		mockMvc.perform(get("/api/v1/weather/forecast/London"))
				.andExpect(status().isOk());

		org.assertj.core.api.Assertions.assertThat(weatherService.forecastCalled).isTrue();
		org.assertj.core.api.Assertions.assertThat(weatherService.lastLocation).isEqualTo("London");
		org.assertj.core.api.Assertions.assertThat(weatherService.lastForecastDays).isEqualTo(1);
	}

	@Test
	void getWeatherForecast_usesProvidedDays() throws Exception {
		weatherService.response = new WeatherSummary("London", 6.5);

		mockMvc.perform(get("/api/v1/weather/forecast/London/3"))
				.andExpect(status().isOk());

		org.assertj.core.api.Assertions.assertThat(weatherService.forecastCalled).isTrue();
		org.assertj.core.api.Assertions.assertThat(weatherService.lastLocation).isEqualTo("London");
		org.assertj.core.api.Assertions.assertThat(weatherService.lastForecastDays).isEqualTo(3);
	}

	@Test
	void getWeatherForecast_rejectsInvalidDays() throws Exception {
		mockMvc.perform(get("/api/v1/weather/forecast/London/0"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.error").value("Bad Request"))
				.andExpect(jsonPath("$.message").value("days must be greater than 0"))
				.andExpect(jsonPath("$.path").value("/api/v1/weather/forecast/London/0"));

		org.assertj.core.api.Assertions.assertThat(weatherService.forecastCalled).isFalse();
	}

	@Test
	void getWeatherHistory_acceptsIsoDate() throws Exception {
		weatherService.response = new WeatherSummary("London", 5.2);

		mockMvc.perform(get("/api/v1/weather/history/London/2025-01-20"))
				.andExpect(status().isOk());

		org.assertj.core.api.Assertions.assertThat(weatherService.called).isTrue();
		org.assertj.core.api.Assertions.assertThat(weatherService.lastLocation).isEqualTo("London");
		org.assertj.core.api.Assertions.assertThat(weatherService.lastDate).isEqualTo(LocalDate.of(2025, 1, 20));
	}

	@Test
	void getWeatherHistory_rejectsInvalidDateFormat() throws Exception {
		mockMvc.perform(get("/api/v1/weather/history/London/20-01-2025"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.error").value("Bad Request"))
				.andExpect(jsonPath("$.message").value("date must be in yyyy-MM-dd format"))
				.andExpect(jsonPath("$.path").value("/api/v1/weather/history/London/20-01-2025"));

		org.assertj.core.api.Assertions.assertThat(weatherService.called).isFalse();
	}

	@Test
	void getWeatherForecast_returnsInternalServerErrorWhenServiceFails() throws Exception {
		weatherService.throwIllegalState = true;

		mockMvc.perform(get("/api/v1/weather/forecast/London/2"))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status").value(500))
				.andExpect(jsonPath("$.error").value("Internal Server Error"))
				.andExpect(jsonPath("$.message").value("upstream weather service failed"))
				.andExpect(jsonPath("$.path").value("/api/v1/weather/forecast/London/2"));
	}

	private static class StubWeatherService extends WeatherService {
		private boolean called;
		private boolean forecastCalled;
		private String lastLocation;
		private LocalDate lastDate;
		private Integer lastForecastDays;
		private WeatherSummary response;
		private boolean throwIllegalState;

		private StubWeatherService() {
			super(null);
		}

		@Override
		public WeatherSummary getWeatherHistory(String location, LocalDate date) {
			called = true;
			lastLocation = location;
			lastDate = date;
			return response;
		}

		@Override
		public WeatherSummary getWeatherForecast(String location, int days) {
			if (throwIllegalState) {
				throw new IllegalStateException("upstream weather service failed");
			}
			forecastCalled = true;
			lastLocation = location;
			lastForecastDays = days;
			return response;
		}
	}
}
