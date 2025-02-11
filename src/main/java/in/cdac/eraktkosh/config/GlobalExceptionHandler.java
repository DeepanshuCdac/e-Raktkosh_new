package in.cdac.eraktkosh.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import in.cdac.eraktkosh.dto.ErrorResponse;
import in.cdac.eraktkosh.filter.JwtAuthenticationException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(JwtAuthenticationException.class)
	public ResponseEntity<ErrorResponse> handleJwtAuthenticationException(JwtAuthenticationException ex) {

		ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
		return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
	}

}