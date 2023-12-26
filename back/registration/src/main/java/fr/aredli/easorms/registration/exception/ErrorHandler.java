package fr.aredli.easorms.registration.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@Builder
public class ErrorHandler {
	private Date timestamp;
	private String message;
	private String details;
	private HttpStatus status;
	private int statusCode;
}
