# API Documentation

Base URL: `http://localhost:{port}`

Version prefix: `/api/v1`

## Current Weather
Returns a minimal summary (location name + temperature in Celsius) using WeatherAPI.

Method: `GET`

Path: `/api/v1/weather/current/{location}`

Path Parameters:
- `location` string. City name or query accepted by WeatherAPI.

Example Request:
```bash
curl http://localhost:9091/api/v1/weather/current/Stockholm
```

Example Response:
```json
{
  "locationName": "Stockholm",
  "tempC": 7.1
}
```

Notes:
- Errors from the upstream WeatherAPI or missing config will surface as server errors until custom error handling is added.
- This endpoint is covered by an integration test that hits the real Weather API when `weather.api.key` is set. Run `./mvnw verify` to execute it.

## Forecast Weather
Returns a minimal summary (location name + temperature in Celsius) using WeatherAPI forecast data.

Method: `GET`

Path: `/api/v1/weather/forecast/{location}`

Path Parameters:
- `location` string. City name or query accepted by WeatherAPI.

Example Request:
```bash
curl http://localhost:9091/api/v1/weather/forecast/Stockholm
```

Example Response:
```json
{
  "locationName": "Stockholm",
  "tempC": 7.1
}
```

Notes:
- Errors from the upstream WeatherAPI or missing config will surface as server errors until custom error handling is added.
- This endpoint is covered by an integration test that hits the real Weather API when `weather.api.key` is set. Run `./mvnw verify` to execute it.

## Planned / In Progress
These are placeholders for upcoming work so the docs stay aligned as you add more APIs.

- Weather history endpoint (likely `/api/v1/weather/history/{location}`)
- Employee sentiment endpoint (based on `SentimentApiClient`)
- Employee-specific endpoints (TBD)

## Authentication
Spring Security is on the classpath. By default, endpoints require basic auth unless configured otherwise.

## Environment Configuration
The API reads settings from `application.properties` or environment variables.

- `weather.api.key`
- `weather.api.base-url`
- `weather.api.current`
- `weather.api.forecast`
- `weather.api.history`
