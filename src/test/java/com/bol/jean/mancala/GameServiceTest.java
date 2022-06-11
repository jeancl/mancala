package com.bol.jean.mancala;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.bol.jean.mancala.dtos.BoardResponseDTO;
import com.bol.jean.mancala.dtos.BoardUpdateRequestDTO;
import com.bol.jean.mancala.exceptions.GameNotFoundException;
import com.bol.jean.mancala.exceptions.GameNotOverException;
import com.bol.jean.mancala.exceptions.MaxNumberOfGamesExceedException;
import com.bol.jean.mancala.exceptions.PlayerNotAllowedException;
import com.bol.jean.mancala.model.Board;
import com.bol.jean.mancala.model.PlayerEnum;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

	private static final String PIT_POSITION_2 = "2";
	private static final int SEED_AMOUNT_0 = 0;
	private static final int SEED_AMOUNT_6 = 6;
	
	@InjectMocks
	private GameService gameService;
	
	@Mock
	private GameLogicHelper gameLogicHelperMock;
	
	@Mock
	private HashMap<UUID, Board> startedGamesMock;
	
	@Mock
	private Board boardMock;

	@BeforeEach
	public void setup() {
		ReflectionTestUtils.setField(gameService, "maxAmountOfActiveGames", 10);
	}
	
	@Test
	public void gameStartTest() {
		BoardResponseDTO response = gameService.gameStart();
		
		assertNotNull(response.getGameId());
		assertArrayEquals(new int[]{SEED_AMOUNT_0,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6}, response.getPlayer1Pits());
		assertArrayEquals(new int[]{SEED_AMOUNT_0,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6,SEED_AMOUNT_6}, response.getPlayer2Pits());
		assertEquals(PlayerEnum.PLAYER_1, response.getPlayerTurn());
	}
	
	@Test
	public void moveSeedsTest() {
		//Given
		//Create a new Game
		BoardResponseDTO newGame = gameService.gameStart();
		
		//Create updateRequest
		BoardUpdateRequestDTO updateRequest = new BoardUpdateRequestDTO();
		updateRequest.setGameId(newGame.getGameId().toString());
		updateRequest.setPit(PIT_POSITION_2);
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_1);
		
		//When
		//Call move seed
		BoardResponseDTO response = gameService.moveSeeds(updateRequest);
		
		//Then
		assertEquals(newGame.getGameId(), response.getGameId());
		assertArrayEquals(newGame.getPlayer1Pits(), response.getPlayer1Pits());
		assertArrayEquals(newGame.getPlayer2Pits(), response.getPlayer2Pits());
		assertEquals(PlayerEnum.PLAYER_1, response.getPlayerTurn());
		verify(gameLogicHelperMock, times(1)).moveSeedLogic(any(), any());
	}
	
	@Test
	public void moveSeedsGameNotFoundExceptionTest() {
		//Given
		//Create updateRequest
		BoardUpdateRequestDTO updateRequest = new BoardUpdateRequestDTO();
		updateRequest.setGameId(UUID.randomUUID().toString());

		//When
		//Call move seed
		GameNotFoundException exception = Assertions.assertThrows(GameNotFoundException.class, () -> {
			gameService.moveSeeds(updateRequest);
		});
		
		//Then
		assertEquals(Constants.GAME_BOARD_NOT_FOUND, exception.getMessage());
		verify(gameLogicHelperMock, never()).moveSeedLogic(any(), any());
	}
	
	@Test
	public void moveSeedsInvalidPlayerExceptionTest() {
		//Given
		//Create a new Game
		BoardResponseDTO newGame = gameService.gameStart();
		
		//Create updateRequest
		BoardUpdateRequestDTO updateRequest = new BoardUpdateRequestDTO();
		updateRequest.setGameId(newGame.getGameId().toString());
		updateRequest.setPit(PIT_POSITION_2);
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_1);
		
		when(gameLogicHelperMock.checkPlayerTurnNotAllowed(any(), any())).thenReturn(true);

		//When
		//Call move seed
		PlayerNotAllowedException exception = Assertions.assertThrows(PlayerNotAllowedException.class, () -> {
			gameService.moveSeeds(updateRequest);
		});
		
		//Then
		assertEquals(Constants.PLAYER_NOT_ALLOWED, exception.getMessage());
		verify(gameLogicHelperMock, times(1)).checkPlayerTurnNotAllowed(any(), any());
		verify(gameLogicHelperMock, never()).moveSeedLogic(any(), any());
	}
	
	@Test
	public void gameOverStatusTest() {
		BoardResponseDTO newGame = gameService.gameStart();
		
		assertFalse(gameService.gameOverStatus(newGame.getGameId().toString()).isGameOver());
	}
	
	@Test
	public void gameOverStatusExceptionTest() {
		GameNotFoundException exception = Assertions.assertThrows(GameNotFoundException.class, () -> {
			gameService.gameOverStatus(UUID.randomUUID().toString());
		});
		
		assertEquals(Constants.GAME_BOARD_NOT_FOUND, exception.getMessage());
	}
	
	@Test
	public void updateGameFinalScoreTest() {
		Board board = new Board();
		
		ReflectionTestUtils.setField(gameService, "startedGames", startedGamesMock);
		
		when(startedGamesMock.get(any())).thenReturn(boardMock);
		when(boardMock.checkEndGame()).thenReturn(true);
		
		gameService.updateGameFinalScore(board.getGameId().toString());
		
		verify(gameLogicHelperMock, times(1)).updateBoardToGetFinalScore(any());
	}
	
	@Test
	public void updateGameFinalScoreNotFoundExceptionTest() {
		GameNotFoundException exception = Assertions.assertThrows(GameNotFoundException.class, () -> {
			gameService.updateGameFinalScore(UUID.randomUUID().toString());
		});
		
		assertEquals(Constants.GAME_BOARD_NOT_FOUND, exception.getMessage());
	}
	
	@Test
	public void updateGameFinalScoreGameNotOverExceptionTest() {
		BoardResponseDTO newGame = gameService.gameStart();
		
		GameNotOverException exception = Assertions.assertThrows(GameNotOverException.class, () -> {
			gameService.updateGameFinalScore(newGame.getGameId().toString());
		});
		
		assertEquals(Constants.GAME_NOT_OVER, exception.getMessage());
	}
	
	@Test
	public void deleteGameEndedTest() {
		Board board = new Board();
		
		ReflectionTestUtils.setField(gameService, "startedGames", startedGamesMock);
		
		when(startedGamesMock.get(any())).thenReturn(boardMock);
		when(boardMock.checkEndGame()).thenReturn(true);
		
		gameService.deleteGame(board.getGameId().toString());
		
		verify(startedGamesMock, times(1)).remove(any());
	}
	
	@Test
	public void deleteGameNotEndedTest() {
		Board board = new Board();
		
		ReflectionTestUtils.setField(gameService, "startedGames", startedGamesMock);
		ReflectionTestUtils.setField(gameService, "minimumInactiveMinutesToDelete", 0);
		
		when(startedGamesMock.get(any())).thenReturn(boardMock);
		when(boardMock.checkEndGame()).thenReturn(false);
		when(boardMock.getLastUpdatedDate()).thenReturn(LocalDateTime.now());
		
		gameService.deleteGame(board.getGameId().toString());
		
		verify(startedGamesMock, times(1)).remove(any());
	}
	
	@Test
	public void deleteGameNotFoundExceptionTest() {
		GameNotFoundException exception = Assertions.assertThrows(GameNotFoundException.class, () -> {
			gameService.deleteGame(UUID.randomUUID().toString());
		});
		
		assertEquals(Constants.GAME_BOARD_NOT_FOUND, exception.getMessage());
	}
	
	@Test
	public void deleteGameNotOverExceptionTest() {
		BoardResponseDTO newGame = gameService.gameStart();
		
		ReflectionTestUtils.setField(gameService, "minimumInactiveMinutesToDelete", 60);

		GameNotOverException exception = Assertions.assertThrows(GameNotOverException.class, () -> {
			gameService.deleteGame(newGame.getGameId().toString());
		});
		
		assertEquals(Constants.GAME_NOT_OVER, exception.getMessage());
	}
	
	@Test
	public void maxNumberOfGamesExceptionTest() {
		ReflectionTestUtils.setField(gameService, "maxAmountOfActiveGames", 1);

		MaxNumberOfGamesExceedException exception = Assertions.assertThrows(MaxNumberOfGamesExceedException.class, () -> {
			gameService.gameStart();
			gameService.gameStart();
		});
		
		assertEquals(Constants.MAX_NUMBER_OF_GAMES_EXCEED, exception.getMessage());
	}
}
