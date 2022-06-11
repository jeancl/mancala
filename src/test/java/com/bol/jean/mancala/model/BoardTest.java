package com.bol.jean.mancala.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BoardTest {

	private static final int PIT_POSITION_2 = 2;
	
	private static final int SEED_AMOUNT_0 = 0;
	private static final int SEED_AMOUNT_6 = 6;
	
	private static final int SEED_AMOUNT_7 = 7;
	
	private Board board;
	
	@BeforeEach
	public void beforeEach() {
		this.board = new Board();
	}
	
	@Test
	public void getPlayer1PitsTest() {
		assertArrayEquals(new int[]{SEED_AMOUNT_0,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6}, board.getPlayer1Pits());
	}
	
	@Test
	public void getPlayer2PitsTest() {
		assertArrayEquals(new int[]{SEED_AMOUNT_0,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6}, board.getPlayer2Pits());
	}
	
	@Test
	public void addSeedTest() {
		board.addSeed(board.getPlayer1Pits(), PIT_POSITION_2);
		assertEquals(SEED_AMOUNT_7, board.getPlayer1Pits()[PIT_POSITION_2]);
	}
	
	@Test
	public void removeSeedTest() {
		assertEquals(SEED_AMOUNT_6, board.removeSeeds(board.getPlayer1Pits(), PIT_POSITION_2));
		assertEquals(SEED_AMOUNT_0, board.getPlayer1Pits()[PIT_POSITION_2]);
	}
	
	@Test
	public void getPlayerTurnTest() {
		assertEquals(PlayerEnum.PLAYER_1, board.getPlayerTurn());
	}
	
	@Test
	public void setPlayerTurnTest() {
		board.setPlayerTurn(PlayerEnum.PLAYER_2);
		assertEquals(PlayerEnum.PLAYER_2, board.getPlayerTurn());
	}
	
	@Test
	public void getSeedsAmountTest() {
		assertEquals(SEED_AMOUNT_6,board.getSeedsAmount(board.getPlayer1Pits(), PIT_POSITION_2));
	}
	
	@Test
	public void addSeedToBigPitTest() {
		board.addSeedToBigPit(board.getPlayer1Pits(), SEED_AMOUNT_6);
		assertEquals(SEED_AMOUNT_6, board.getSeedsAmount(board.getPlayer1Pits(), 0));
	}
	
	@Test
	public void checkEndGameFalseTest() {
		assertFalse(board.checkEndGame());
	}
	
	@Test
	public void checkEndGamePlayer1TrueTest() {
		//Remove all seeds to test player 1 end game
		board.removeSeeds(board.getPlayer1Pits(), 1);
		board.removeSeeds(board.getPlayer1Pits(), 2);
		board.removeSeeds(board.getPlayer1Pits(), 3);
		board.removeSeeds(board.getPlayer1Pits(), 4);
		board.removeSeeds(board.getPlayer1Pits(), 5);
		board.removeSeeds(board.getPlayer1Pits(), 6);
		assertTrue(board.checkEndGame());
	}
	
	@Test
	public void checkEndGamePlayer2TrueTest() {
		//Remove all seeds to test player 2 end game
		board.removeSeeds(board.getPlayer2Pits(), 1);
		board.removeSeeds(board.getPlayer2Pits(), 2);
		board.removeSeeds(board.getPlayer2Pits(), 3);
		board.removeSeeds(board.getPlayer2Pits(), 4);
		board.removeSeeds(board.getPlayer2Pits(), 5);
		board.removeSeeds(board.getPlayer2Pits(), 6);
		assertTrue(board.checkEndGame());
	}
	
	@Test
	public void getPlayer1ScoreTest() {
		assertEquals(0,board.getPlayer1Score());
	}
	
	@Test
	public void getPlayer2ScoreTest() {
		assertEquals(0,board.getPlayer2Score());
	}
	
	@Test
	public void getLastUpdatedDateTest() {
		LocalDateTime currentDate = LocalDateTime.now();
		board.setLastUpdatedDate(currentDate);
		assertEquals(currentDate,board.getLastUpdatedDate());
	}
	
	@Test
	public void getCreatedDateTest() {
		assertNotNull(board.getCreatedDate());
	}
	
	@Test
	public void getGameIdTest() {
		assertNotNull(board.getGameId());
	}
}
