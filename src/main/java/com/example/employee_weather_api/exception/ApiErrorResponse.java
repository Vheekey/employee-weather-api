package com.example.employee_weather_api.exception;

import java.time.Instant;

public record ApiErrorResponse(
		Instant timestamp,
		int status,
		String error,
		String message,
		String path
) {
}
