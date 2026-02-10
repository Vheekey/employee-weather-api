package com.example.employee_weather_api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.example.employee_weather_api.weather.dto.WeatherTemperatureResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import com.example.employee_weather_api.weather.WeatherApiClient;
import com.example.employee_weather_api.weather.WeatherApiProperties;

@SpringBootTest
class WeatherApiClientTest {

	private MockRestServiceServer server;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private WeatherApiProperties properties;

	@Autowired
	private WeatherApiClient client;

	@BeforeEach
	void setUp() {
		server = MockRestServiceServer.createServer(restTemplate);
	}

	@Test
	void getCurrentWeather_usesConfigAndReturnsResult() {
		String location = "London";
		String json = """
			{
			  "location": { "name": "London" },
			  "current": { "temp_c": 7.1 }
			}
			""";

		String expectedUrl = properties.getBaseUrl()
				+ properties.getCurrent()
				+ "?key=" + properties.getKey()
				+ "&q=" + location;

		server.expect(requestTo(expectedUrl))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

		WeatherTemperatureResponse body = client.getCurrent(location);

		assertThat(body.getLocation().getName()).isEqualTo("London");
		assertThat(body.getCurrent().getTempC()).isEqualTo(7.1);
		server.verify();
	}
}
