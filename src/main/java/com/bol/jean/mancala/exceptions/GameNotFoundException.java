package com.bol.jean.mancala.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class GameNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -1062772137716237752L;

	public GameNotFoundException(String message) {
		super(message);
	}
}
