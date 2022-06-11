package com.bol.jean.mancala.model;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

//Model Class to represent a Mancala board
public class Board {

	private UUID gameId;
	
	private int player1Pits[];
	
	private int player2Pits[];
	
	private PlayerEnum playerTurn;
	
	private LocalDateTime createdDate;
	
	private LocalDateTime lastUpdatedDate;
	
	//Initializing board setup
	public Board() {
		this.gameId = UUID.randomUUID();
		
		this.player1Pits = new int[]{0,6,6,6,6,6,6};
		
		this.player2Pits = new int[]{0,6,6,6,6,6,6};
		
		this.playerTurn = PlayerEnum.PLAYER_1;
		
		this.createdDate = LocalDateTime.now();
		
		this.lastUpdatedDate = LocalDateTime.now();
	}
	
	//Add 1 seed to chosen player pit
	public void addSeed(int playerPit[], int position) {
		playerPit[position] += 1;
	}
	
	//Remove all seeds from chosen player pit and return value removed
	public int removeSeeds(int playerPit[], int position) {
		int seedAmount = playerPit[position];
		playerPit[position] = 0;
		return seedAmount;
	}
	
	//Return seed amount
	public int getSeedsAmount(int playerPit[], int position) {
		return playerPit[position];
	}
	
	//Add seeds to big pit
	public void addSeedToBigPit(int playerPit[], int seedAmount) {
		playerPit[0] += seedAmount;
	}
	
	//Check end game
	public boolean checkEndGame() {
		return Arrays.stream(player1Pits).skip(1).allMatch(Integer.valueOf(0)::equals) || 
			   Arrays.stream(player2Pits).skip(1).allMatch(Integer.valueOf(0)::equals);
	}
	
	public int getPlayer1Score() {
		return player1Pits[0];
	}
	
	public int getPlayer2Score() {
		return player2Pits[0];
	}

	public int[] getPlayer1Pits() {
		return player1Pits;
	}

	public int[] getPlayer2Pits() {
		return player2Pits;
	}
	
	public PlayerEnum getPlayerTurn() {
		return playerTurn;
	}

	public void setPlayerTurn(PlayerEnum playerTurn) {
		this.playerTurn = playerTurn;
	}

	public LocalDateTime getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public UUID getGameId() {
		return gameId;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}
	
}
