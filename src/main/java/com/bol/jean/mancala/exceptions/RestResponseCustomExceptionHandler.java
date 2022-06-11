package com.bol.jean.mancala.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.bol.jean.mancala.dtos.ErrorDTO;

@ControllerAdvice
public class RestResponseCustomExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { GameNotOverException.class, MaxNumberOfGamesExceedException.class, PlayerNotAllowedException.class })
    protected ResponseEntity<Object> handleCustomErrorsBadRequest(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, createErrorObject(HttpStatus.BAD_REQUEST, ex), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
	
	@ExceptionHandler(value = { GameNotFoundException.class })
    protected ResponseEntity<Object> handleCustomErrorNotFound(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, createErrorObject(HttpStatus.NOT_FOUND, ex), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
	
	private ErrorDTO createErrorObject(HttpStatus httpStatus, RuntimeException ex) {
		ErrorDTO error = new ErrorDTO();
        error.setError(httpStatus.getReasonPhrase());
        error.setStatus(httpStatus.value());
        error.setMessage(ex.getMessage());
        error.setTimestamp(LocalDateTime.now());
        
        return error;
	}
}
