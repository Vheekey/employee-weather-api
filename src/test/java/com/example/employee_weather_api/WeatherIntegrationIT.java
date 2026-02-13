package com.example.employee_weather_api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import com.example.employee_weather_api.weather.WeatherApiProperties;
import com.example.employee_weather_api.weather.dto.WeatherSummary;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		properties = {
				"spring.security.user.name=user",
				"spring.security.user.password=test"
		}
)

//only runs at ./mvnw verify cos of app Speed and FailSafe
class WeatherIntegrationIT {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private WeatherApiProperties properties;

	@Test
	void getCurrentWeather_hitsRealWeatherApi() {
		assumeTrue(properties.getKey() != null && !properties.getKey().isBlank(),
				"Skipping integration test because weather.api.key is not set");
		assumeTrue((properties.getCurrent() != null && !properties.getCurrent().isBlank()),
				"Skipping integration test because weather.api.current is not set");

		String location = "London";
		String url = getWeatherUrl("current", location);

		ResponseEntity<WeatherSummary> response = restTemplate
				.withBasicAuth("user", "test")
				.getForEntity(url, WeatherSummary.class);

		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getLocationName()).isNotBlank();
		assertThat(response.getBody().getTempC()).isBetween(-80.0, 60.0);
	}

	@Test
	void getForecastWeather_hitsRealWeatherApi() {
		assumeTrue(properties.getKey() != null && !properties.getKey().isBlank(),
				"Skipping integration test because weather.api.key is not set");
		assumeTrue((properties.getForecast() != null && !properties.getForecast().isBlank()),
				"Skipping integration test because weather.api.forecast is not set");

		String location = "Stockholm";
		String url = getWeatherUrl("forecast", location);

		ResponseEntity<WeatherSummary> response = restTemplate
				.withBasicAuth("user", "test")
				.getForEntity(url, WeatherSummary.class);

		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getLocationName()).isEqualTo(location);
		assertThat(response.getBody().getTempC()).isBetween(-80.0, 60.0);
	}

	String getWeatherUrl(String type, String location){
		return "http://localhost:" + port + "/api/v1/weather/" + type + "/" + location;
	}

}
