package com.bol.jean.mancala.dtos;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ErrorDTO {

	private LocalDateTime timestamp;
	
	private int status;
	
	private String error;
	
	private String message;
}
