package com.example.employee_weather_api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.example.employee_weather_api.weather.dto.WeatherHistoryResponse;
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

import java.time.LocalDate;

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
		double expectedCurrentTemperature = 7.1;

		String expectedUrl = properties.getBaseUrl()
				+ properties.getCurrent()
				+ "?key=" + properties.getKey()
				+ "&q=" + location;

		server.expect(requestTo(expectedUrl))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

		WeatherTemperatureResponse body = client.getCurrent(location);

		assertThat(body.getLocation().getName()).isEqualTo(location);
		assertThat(body.getCurrent().getTempC()).isEqualTo(expectedCurrentTemperature);
		server.verify();
	}

	@Test
	void getForecastWeather_usesConfigAndReturnsResult() {
		String location = "Stockholm";
		String json = """
                 {
                 "location": { "name": "Stockholm" },
                 "forecast": {
                   "forecastday": [
                     {
                       "date": "2026-02-15",
                       "day": { "avgtemp_c": 6.1 }
                     },
                     {
                       "date": "2026-02-16",
                       "day": { "avgtemp_c": 7.1 }
                     }
                   ]
                 }
                 }
                """;
		double expectedTempC = 7.1;

		String expectedUrl = properties.getBaseUrl()
				+ properties.getForecast()
				+ "?key=" + properties.getKey()
				+ "&q=" + location
				+ "&days=2";
		server.expect(requestTo(expectedUrl))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

		WeatherHistoryResponse body = client.getForecast(location, 2);
		assertThat(body.getLocation().getName()).isEqualTo(location);
		assertThat(body.getForecast().days()).hasSize(2);
		assertThat(body.getForecast().days().get(1).getDay().getAvgTempC()).isEqualTo(expectedTempC);

		server.verify();
	}

	@Test
	void getHistoryWeather_usesIsoDateInQueryString() {
		String location = "Oslo";
		LocalDate date = LocalDate.of(2025, 1, 20);
		String json = """
			{
			  "location": { "name": "Oslo" },
			  "forecast": {
			    "forecastday": [
			      {
			        "date": "2025-01-20",
			        "day": { "avgtemp_c": -2.4 }
			      }
			    ]
			  }
			}
			""";
		double expectedTempC = -2.4;

		String expectedUrl = properties.getBaseUrl()
				+ properties.getHistory()
				+ "?key=" + properties.getKey()
				+ "&q=" + location
				+ "&dt=2025-01-20";

		server.expect(requestTo(expectedUrl))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

		WeatherHistoryResponse body = client.getHistory(location, date);
		assertThat(body.getLocation().getName()).isEqualTo(location);
		assertThat(body.getForecast().days()).hasSize(1);
		assertThat(body.getForecast().days().getFirst().getDay().getAvgTempC()).isEqualTo(expectedTempC);

		server.verify();
	}
}
