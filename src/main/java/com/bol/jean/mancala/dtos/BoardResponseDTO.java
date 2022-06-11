package com.bol.jean.mancala.dtos;

import java.util.UUID;

import com.bol.jean.mancala.model.PlayerEnum;

import lombok.Data;

@Data
public class BoardResponseDTO {

	private UUID gameId;
	
	private int player1Pits[];
	
	private int player2Pits[];
	
	private PlayerEnum playerTurn;
}
