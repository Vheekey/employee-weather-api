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
- Errors are handled by global exception advice and return a structured body:
```json
{
  "timestamp": "2026-02-20T18:42:10.911Z",
  "status": 400,
  "error": "Bad Request",
  "message": "days must be greater than 0",
  "path": "/api/v1/weather/forecast/London/0"
}
```
- This endpoint is covered by an integration test that hits the real Weather API when `weather.api.key` is set. Run `./mvnw verify` to execute it.

## Forecast Weather
Returns the location name and a list of forecast days from WeatherAPI data.

Method: `GET`

Path: `/api/v1/weather/forecast/{location}` or `/api/v1/weather/forecast/{location}/{days}`

Path Parameters:
- `location` string. City name or query accepted by WeatherAPI.
- `days` integer (optional). Must be greater than `0`. Defaults to `1` when omitted.

Example Request:
```bash
curl http://localhost:9091/api/v1/weather/forecast/Stockholm
```

Example Response:
```json
{
  "locationName": "Stockholm",
  "forecastDays": [
    {
      "date": "2025-01-20",
      "dateEpoch": 1737331200,
      "day": {
        "avgTempC": 7.1,
        "maxTempC": 9.0,
        "minTempC": 4.5
      },
      "hour": [
        {
          "time": "2025-01-20 00:00",
          "tempC": 6.3
        }
      ]
    }
  ]
}
```

Notes:
- Errors now return the same structured error body as shown above.
- This endpoint is covered by an integration test that hits the real Weather API when `weather.api.key` is set. Run `./mvnw verify` to execute it.

## History Weather
Returns the location name and historical day data for the requested date.

Method: `GET`

Path: `/api/v1/weather/history/{location}/{date}`

Path Parameters:
- `location` string. City name or query accepted by WeatherAPI.
- `date` string in strict `yyyy-MM-dd` format.

Example Request:
```bash
curl http://localhost:9091/api/v1/weather/history/Stockholm/2025-01-20
```

Example Response:
```json
{
  "locationName": "Stockholm",
  "forecastDays": [
    {
      "date": "2025-01-20",
      "dateEpoch": 1737331200,
      "day": {
        "avgTempC": 1.3,
        "maxTempC": 3.2,
        "minTempC": -1.1
      }
    }
  ]
}
```

Validation Notes:
- `date` must be a valid calendar date in `yyyy-MM-dd` format.
- Invalid dates return `400 Bad Request` with the same structured error body.

## Planned / In Progress
These are placeholders for upcoming work so the docs stay aligned as you add more APIs.

- Employee sentiment endpoint (based on `SentimentApiClient`)
- Employee-specific endpoints (TBD)

## Error Handling
The API uses global exception handling to standardize errors across endpoints.

Common mappings:
- `IllegalArgumentException` -> `400 Bad Request`
- `IllegalStateException` -> `500 Internal Server Error`
- `ResponseStatusException` -> uses provided status code
- Unhandled exceptions -> `500 Internal Server Error`

## Authentication
Spring Security is on the classpath. By default, endpoints require basic auth unless configured otherwise.

## Environment Configuration
The API reads settings from `application.properties` or environment variables.

- `weather.api.key`
- `weather.api.base-url`
- `weather.api.current`
- `weather.api.forecast`
- `weather.api.history`
