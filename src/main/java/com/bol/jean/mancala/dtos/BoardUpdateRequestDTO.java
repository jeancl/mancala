package com.bol.jean.mancala.dtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.bol.jean.mancala.annotation.UUID;
import com.bol.jean.mancala.model.PlayerEnum;

import lombok.Data;

@Data
public class BoardUpdateRequestDTO {

	@NotNull(message = "gameId field required")
	@UUID(message = "Invalid gameId format")
	private String gameId;
	
	@NotNull(message = "playerTurn required")
	private PlayerEnum playerTurn;
	
	@NotNull(message = "pit required")
	@Pattern(regexp = "[1-6]", message = "pit value invalid")
	private String pit;
}
