package com.bol.jean.mancala.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MaxNumberOfGamesExceedException extends RuntimeException {

	private static final long serialVersionUID = -1062772137716237752L;

	public MaxNumberOfGamesExceedException(String message) {
		super(message);
	}
	
}
