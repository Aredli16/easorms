package fr.aredli.easorms.registration.controller;

import fr.aredli.easorms.registration.exception.ErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.NoSuchElementException;

@ControllerAdvice
@Slf4j
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
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorHandler> handleIllegalArgumentException(IllegalArgumentException exception) {
		ErrorHandler errorHandler = ErrorHandler.builder()
				.timestamp(new Date())
				.message(exception.getMessage())
				.details("The request is invalid.")
				.status(HttpStatus.BAD_REQUEST)
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.build();
		
		return new ResponseEntity<>(errorHandler, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorHandler> handleAccessDeniedException(AccessDeniedException exception) {
		ErrorHandler errorHandler = ErrorHandler.builder()
				.timestamp(new Date())
				.message(exception.getMessage())
				.details("Access denied.")
				.status(HttpStatus.FORBIDDEN)
				.statusCode(HttpStatus.FORBIDDEN.value())
				.build();
		
		return new ResponseEntity<>(errorHandler, HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorHandler> handleException(Exception exception) {
		ErrorHandler errorHandler = ErrorHandler.builder()
				.timestamp(new Date())
				.message(exception.getMessage())
				.details("Resource not found.")
				.status(HttpStatus.BAD_REQUEST)
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.build();
		
		log.error(exception.getMessage(), exception);
		return new ResponseEntity<>(errorHandler, HttpStatus.BAD_REQUEST);
	}
}
