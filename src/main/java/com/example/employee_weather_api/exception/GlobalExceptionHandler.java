package com.example.employee_weather_api.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<ApiErrorResponse> handleResponseStatusException(
			ResponseStatusException ex,
			HttpServletRequest request
	) {
		HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
		String message = ex.getReason() != null ? ex.getReason() : status.getReasonPhrase();

		return this.buildError(status, message, request);
	}

	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<ApiErrorResponse> handleHttpClientErrorException(
			HttpClientErrorException ex,
			HttpServletRequest request
	){
		HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
		String message = ex.getMessage();

		return this.buildError(status, message, request);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(
			IllegalArgumentException ex,
			HttpServletRequest request
	) {
		return this.buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<ApiErrorResponse> handleIllegalStateException(
			IllegalStateException ex,
			HttpServletRequest request
	) {
		return this.buildError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorResponse> handleUnexpectedException(HttpServletRequest request) {
		return this.buildError(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected server error", request);
	}

	private ResponseEntity<ApiErrorResponse> buildError(HttpStatus status, String message, HttpServletRequest request) {
		ApiErrorResponse error = new ApiErrorResponse(
				Instant.now(),
				status.value(),
				status.getReasonPhrase(),
				message,
				request.getRequestURI());

		return ResponseEntity.status(status).body(error);
	}
}
