package com.bol.jean.mancala;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.bol.jean.mancala.dtos.BoardUpdateRequestDTO;
import com.bol.jean.mancala.model.Board;
import com.bol.jean.mancala.model.PlayerEnum;

public class GameLogicHelperTest {

	private static final String PIT_POSITION_1 = "1";
	private static final String PIT_POSITION_2 = "2";
	private static final String PIT_POSITION_3 = "3";
	private static final String PIT_POSITION_4 = "4";
	private static final String PIT_POSITION_5 = "5";
	private static final String PIT_POSITION_6 = "6";
	
	private static final int SEED_AMOUNT_0 = 0;
	private static final int SEED_AMOUNT_1 = 1;
	private static final int SEED_AMOUNT_2 = 2;
	private static final int SEED_AMOUNT_3 = 3;
	private static final int SEED_AMOUNT_6 = 6;
	private static final int SEED_AMOUNT_7 = 7;
	private static final int SEED_AMOUNT_8 = 8;
	private static final int SEED_AMOUNT_9 = 9;
	private static final int SEED_AMOUNT_10 = 10;
	private static final int SEED_AMOUNT_14 = 14;
	private static final int SEED_AMOUNT_36 = 36;
	
	private GameLogicHelper gameLogicHelperMock = new GameLogicHelper();
	
	
	@Test
	public void checkPlayerTurnAllowedTest() {
		Board board = new Board();
		
		//Create updateRequest
		BoardUpdateRequestDTO updateRequest = new BoardUpdateRequestDTO();
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_1);
		
		assertFalse(gameLogicHelperMock.checkPlayerTurnNotAllowed(updateRequest, board));
	}
	
	@Test
	public void checkPlayerTurnNotAllowedTest() {
		Board board = new Board();
		
		//Create updateRequest
		BoardUpdateRequestDTO updateRequest = new BoardUpdateRequestDTO();
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_2);
		
		assertTrue(gameLogicHelperMock.checkPlayerTurnNotAllowed(updateRequest, board));
	}
	
	@Test
	public void moveSeedLogicPlayer1Test() {
		Board board = new Board();
		
		//Create updateRequest
		BoardUpdateRequestDTO updateRequest = new BoardUpdateRequestDTO();
		updateRequest.setPit(PIT_POSITION_2);
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_1);
		
		gameLogicHelperMock.moveSeedLogic(updateRequest, board);
		
		assertArrayEquals(new int[]{SEED_AMOUNT_1,SEED_AMOUNT_7,SEED_AMOUNT_0,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6}, board.getPlayer1Pits());
		assertArrayEquals(new int[]{SEED_AMOUNT_0,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_7,SEED_AMOUNT_7,SEED_AMOUNT_7,SEED_AMOUNT_7}, board.getPlayer2Pits());
		assertEquals(PlayerEnum.PLAYER_2, board.getPlayerTurn());
	}
	
	@Test
	public void moveSeedLogicPlayer2Test() {
		Board board = new Board();
		
		//Create updateRequest
		BoardUpdateRequestDTO updateRequest = new BoardUpdateRequestDTO();
		updateRequest.setPit(PIT_POSITION_2);
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_2);
		
		gameLogicHelperMock.moveSeedLogic(updateRequest, board);
		
		assertArrayEquals(new int[]{SEED_AMOUNT_1,SEED_AMOUNT_7,SEED_AMOUNT_0,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6}, board.getPlayer2Pits());
		assertArrayEquals(new int[]{SEED_AMOUNT_0,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_7,SEED_AMOUNT_7,SEED_AMOUNT_7,SEED_AMOUNT_7}, board.getPlayer1Pits());
		assertEquals(PlayerEnum.PLAYER_1, board.getPlayerTurn());
	}
	
	@Test
	public void moveSeedLogicRepeatPlayer1TurnTest() {
		Board board = new Board();
		
		//Create updateRequest
		BoardUpdateRequestDTO updateRequest = new BoardUpdateRequestDTO();
		updateRequest.setPit(PIT_POSITION_6);
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_1);
		
		gameLogicHelperMock.moveSeedLogic(updateRequest, board);
		
		assertArrayEquals(new int[]{SEED_AMOUNT_1,SEED_AMOUNT_7,SEED_AMOUNT_7,SEED_AMOUNT_7,SEED_AMOUNT_7,SEED_AMOUNT_7,SEED_AMOUNT_0}, board.getPlayer1Pits());
		assertArrayEquals(new int[]{SEED_AMOUNT_0,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6}, board.getPlayer2Pits());
		assertEquals(PlayerEnum.PLAYER_1, board.getPlayerTurn());
	}
	
	@Test
	public void moveSeedLogicRepeatPlayer2TurnTest() {
		Board board = new Board();
		
		//Create updateRequest
		BoardUpdateRequestDTO updateRequest = new BoardUpdateRequestDTO();
		updateRequest.setPit(PIT_POSITION_6);
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_2);
		
		gameLogicHelperMock.moveSeedLogic(updateRequest, board);
		
		assertArrayEquals(new int[]{SEED_AMOUNT_1,SEED_AMOUNT_7,SEED_AMOUNT_7,SEED_AMOUNT_7,SEED_AMOUNT_7,SEED_AMOUNT_7,SEED_AMOUNT_0}, board.getPlayer2Pits());
		assertArrayEquals(new int[]{SEED_AMOUNT_0,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6}, board.getPlayer1Pits());
		assertEquals(PlayerEnum.PLAYER_2, board.getPlayerTurn());
	}
	
	@Test
	public void updateBoardToGetFinalScoreTest() {
		Board board = new Board();
		
		gameLogicHelperMock.updateBoardToGetFinalScore(board);
		
		assertArrayEquals(new int[]{SEED_AMOUNT_36,SEED_AMOUNT_0,SEED_AMOUNT_0,SEED_AMOUNT_0,SEED_AMOUNT_0,SEED_AMOUNT_0,SEED_AMOUNT_0}, board.getPlayer2Pits());
		assertArrayEquals(new int[]{SEED_AMOUNT_36,SEED_AMOUNT_0,SEED_AMOUNT_0,SEED_AMOUNT_0,SEED_AMOUNT_0,SEED_AMOUNT_0,SEED_AMOUNT_0}, board.getPlayer1Pits());
	}
	
	@Test
	public void moveSeedLogicSubsequentMovesTest() {
		Board board = new Board();
		BoardUpdateRequestDTO updateRequest = new BoardUpdateRequestDTO();
		
		//------First Move----------
		updateRequest.setPit(PIT_POSITION_6);
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_1);
		
		gameLogicHelperMock.moveSeedLogic(updateRequest, board);
		
		assertArrayEquals(new int[]{SEED_AMOUNT_1,SEED_AMOUNT_7,SEED_AMOUNT_7,SEED_AMOUNT_7,SEED_AMOUNT_7,SEED_AMOUNT_7,SEED_AMOUNT_0}, board.getPlayer1Pits());
		assertArrayEquals(new int[]{SEED_AMOUNT_0,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6}, board.getPlayer2Pits());
		assertEquals(PlayerEnum.PLAYER_1, board.getPlayerTurn());
		
		//------Second Move----------
		updateRequest.setPit(PIT_POSITION_5);
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_1);
		
		gameLogicHelperMock.moveSeedLogic(updateRequest, board);
		
		assertArrayEquals(new int[]{SEED_AMOUNT_2,SEED_AMOUNT_8,SEED_AMOUNT_8,SEED_AMOUNT_8,SEED_AMOUNT_8,SEED_AMOUNT_0,SEED_AMOUNT_0}, board.getPlayer1Pits());
		assertArrayEquals(new int[]{SEED_AMOUNT_0,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_7,SEED_AMOUNT_7}, board.getPlayer2Pits());
		assertEquals(PlayerEnum.PLAYER_2, board.getPlayerTurn());
		
		//------Third Move----------
		updateRequest.setPit(PIT_POSITION_1);
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_2);
		
		gameLogicHelperMock.moveSeedLogic(updateRequest, board);
		
		assertArrayEquals(new int[]{SEED_AMOUNT_2,SEED_AMOUNT_8,SEED_AMOUNT_9,SEED_AMOUNT_9,SEED_AMOUNT_9,SEED_AMOUNT_1,SEED_AMOUNT_1}, board.getPlayer1Pits());
		assertArrayEquals(new int[]{SEED_AMOUNT_1,SEED_AMOUNT_0,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_7,SEED_AMOUNT_7}, board.getPlayer2Pits());
		assertEquals(PlayerEnum.PLAYER_1, board.getPlayerTurn());
		
		//------Forth Move----------
		updateRequest.setPit(PIT_POSITION_1);
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_1);
		
		gameLogicHelperMock.moveSeedLogic(updateRequest, board);
		
		assertArrayEquals(new int[]{SEED_AMOUNT_3,SEED_AMOUNT_0,SEED_AMOUNT_9,SEED_AMOUNT_9,SEED_AMOUNT_9,SEED_AMOUNT_1,SEED_AMOUNT_2}, board.getPlayer1Pits());
		assertArrayEquals(new int[]{SEED_AMOUNT_1,SEED_AMOUNT_1,SEED_AMOUNT_7,SEED_AMOUNT_7,SEED_AMOUNT_7,SEED_AMOUNT_8,SEED_AMOUNT_8}, board.getPlayer2Pits());
		assertEquals(PlayerEnum.PLAYER_2, board.getPlayerTurn());
	}
	
	@Test
	public void checkPlayer1CaptureSeedsTest() {
		Board board = new Board();
		BoardUpdateRequestDTO updateRequest = new BoardUpdateRequestDTO();
		
		//------First Move----------
		updateRequest.setPit(PIT_POSITION_3);
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_1);
		
		gameLogicHelperMock.moveSeedLogic(updateRequest, board);
		
		//------Second Move----------
		updateRequest.setPit(PIT_POSITION_3);
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_2);
		
		gameLogicHelperMock.moveSeedLogic(updateRequest, board);
		
		//------Third Move----------
		updateRequest.setPit(PIT_POSITION_4);
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_1);
		
		gameLogicHelperMock.moveSeedLogic(updateRequest, board);
		
		//------Forth Move----------
		updateRequest.setPit(PIT_POSITION_6);
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_2);
		
		gameLogicHelperMock.moveSeedLogic(updateRequest, board);
		
		//------Fifth Move----------
		updateRequest.setPit(PIT_POSITION_2);
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_1);
		
		gameLogicHelperMock.moveSeedLogic(updateRequest, board);
		
		//------Sixth Move----------
		updateRequest.setPit(PIT_POSITION_6);
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_2);
		
		gameLogicHelperMock.moveSeedLogic(updateRequest, board);
		
		//------Seventh Move----------
		updateRequest.setPit(PIT_POSITION_3);
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_1);
		
		gameLogicHelperMock.moveSeedLogic(updateRequest, board);
		
		//Check Player 1 success capture
		assertArrayEquals(new int[]{SEED_AMOUNT_14,SEED_AMOUNT_9,SEED_AMOUNT_1,SEED_AMOUNT_0,SEED_AMOUNT_0,SEED_AMOUNT_8,SEED_AMOUNT_8}, board.getPlayer1Pits());
		assertArrayEquals(new int[]{SEED_AMOUNT_2,SEED_AMOUNT_9,SEED_AMOUNT_9,SEED_AMOUNT_2,SEED_AMOUNT_10,SEED_AMOUNT_0,SEED_AMOUNT_0}, board.getPlayer2Pits());
		assertEquals(PlayerEnum.PLAYER_2, board.getPlayerTurn());
	}
	
	@Test
	public void checkPlayer2CaptureSeedsTest() {
		Board board = new Board();
		BoardUpdateRequestDTO updateRequest = new BoardUpdateRequestDTO();
		
		//------First Move----------
		updateRequest.setPit(PIT_POSITION_3);
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_1);
		
		gameLogicHelperMock.moveSeedLogic(updateRequest, board);
		
		//------Second Move----------
		updateRequest.setPit(PIT_POSITION_3);
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_2);
		
		gameLogicHelperMock.moveSeedLogic(updateRequest, board);
		
		//------Third Move----------
		updateRequest.setPit(PIT_POSITION_6);
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_1);
		
		gameLogicHelperMock.moveSeedLogic(updateRequest, board);
		
		//------Forth Move----------
		updateRequest.setPit(PIT_POSITION_4);
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_2);
		
		gameLogicHelperMock.moveSeedLogic(updateRequest, board);
		
		//------Fifth Move----------
		updateRequest.setPit(PIT_POSITION_6);
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_1);
		
		gameLogicHelperMock.moveSeedLogic(updateRequest, board);
		
		//------Sixth Move----------
		
		updateRequest.setPit(PIT_POSITION_2);
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_2);
		
		gameLogicHelperMock.moveSeedLogic(updateRequest, board);
		
		//------Seventh Move----------
		updateRequest.setPit(PIT_POSITION_3);
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_1);
		
		gameLogicHelperMock.moveSeedLogic(updateRequest, board);
		
		//------Eighth Move----------
		updateRequest.setPit(PIT_POSITION_3);
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_2);
		
		gameLogicHelperMock.moveSeedLogic(updateRequest, board);
		
		//Check Player 1 success capture
		assertArrayEquals(new int[]{SEED_AMOUNT_2,SEED_AMOUNT_10,SEED_AMOUNT_10,SEED_AMOUNT_0,SEED_AMOUNT_10,SEED_AMOUNT_0,SEED_AMOUNT_1}, board.getPlayer1Pits());
		assertArrayEquals(new int[]{SEED_AMOUNT_14,SEED_AMOUNT_9,SEED_AMOUNT_1,SEED_AMOUNT_0,SEED_AMOUNT_0,SEED_AMOUNT_7,SEED_AMOUNT_8}, board.getPlayer2Pits());
		assertEquals(PlayerEnum.PLAYER_1, board.getPlayerTurn());
	}
}
