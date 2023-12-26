package fr.aredli.easorms.registration.controller;

import fr.aredli.easorms.registration.exception.ErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ControllerExceptionAdvice {
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<ErrorHandler> handleNoSuchElementException(NoSuchElementException exception) {
		ErrorHandler errorHandler = ErrorHandler.builder()
				.timestamp(new Date())
				.message(exception.getMessage())
				.details("The requested resource was not found.")
				.status(HttpStatus.NOT_FOUND)
				.statusCode(HttpStatus.NOT_FOUND.value())
				.build();
		
		return new ResponseEntity<>(errorHandler, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorHandler> handleException(Exception exception) {
		ErrorHandler errorHandler = ErrorHandler.builder()
				.timestamp(new Date())
				.message(exception.getMessage())
				.details("An unexpected error occurred.")
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.build();
		
		return new ResponseEntity<>(errorHandler, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
