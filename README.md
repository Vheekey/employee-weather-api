# Employee Weather API

A Spring Boot service that fetches weather data and exposes a simple REST API for employee-facing use cases.

## Status
- Implemented: current weather, forecast weather, and history weather endpoints.
- Implemented: global exception handling with a consistent JSON error response body.
- Planned: additional employee and sentiment APIs (see API docs).

## Requirements
- Java 17+
- Maven (wrapper included)
- Optional: Redis (if you enable caching)

## Quick Start
1. Configure the Weather API key.
2. Run the app.
3. Call the API.

### Configure
The service reads Weather API configuration from `application.properties` or environment variables.

- File: `/employee-weather-api/src/main/resources/application.properties`
- Env override example: `WEATHER_API_KEY` maps to `weather.api.key`

Required properties:
- `weather.api.key`
- `weather.api.base-url`
- `weather.api.current`

### Run
```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=9091
```

### Call the API
```bash
curl http://localhost:9091/api/v1/weather/current/London
```

Error responses now use a shared format:
```json
{
  "timestamp": "2026-02-20T18:42:10.911Z",
  "status": 400,
  "error": "Bad Request",
  "message": "days must be greater than 0",
  "path": "/api/v1/weather/forecast/London/0"
}
```

## Development Notes
- Hot reload is enabled via `spring-boot-devtools`.
- When running with `spring-boot:run`, trigger a restart by compiling in a second terminal:
```bash
./mvnw -DskipTests compile
```

## Testing
- Unit tests only:
```bash
./mvnw test
```
- Integration tests (includes real Weather API call):
```bash
./mvnw verify
```

Tests run with an in-memory H2 datasource (`src/test/resources/application.properties`) so local PostgreSQL is not required for test execution.

## Security
`spring-boot-starter-security` is on the classpath. By default, Spring secures all endpoints and uses basic auth.
- Username: `user`
- Password: printed in the app logs on startup

If you want open access locally, add a `SecurityFilterChain` to permit `/api/**` or remove the dependency.

## API Docs
See `/employee-weather-api/docs/API.md`.

## Project Layout
- `src/main/java/com/example/employee_weather_api/weather` weather feature
- `src/main/resources/application.properties` configuration
- `HELP.md` Spring Initializr notes
