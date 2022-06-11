package com.bol.jean.mancala;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.bol.jean.mancala.dtos.BoardResponseDTO;
import com.bol.jean.mancala.dtos.BoardUpdateRequestDTO;
import com.bol.jean.mancala.dtos.FinalScoreResponseDTO;
import com.bol.jean.mancala.dtos.GameOverResponseDTO;
import com.bol.jean.mancala.exceptions.GameNotFoundException;
import com.bol.jean.mancala.model.Board;
import com.bol.jean.mancala.model.PlayerEnum;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class GameControllerTests {

	private static final String PIT_POSITION_2 = "2";
	private static final String PIT_POSITION_7 = "7";
	
	private static final String START_PATH = "/start/";
	private static final String UPDATE_PATH = "/update/";
	private static final String STATUS_PATH = "/status/";
	private static final String FINAL_SCORE_PATH = "/finalscore";
	private static final String DELETE_PATH = "/delete/";
	
	private MockMvc mvc;
	
	@InjectMocks
	private GameController gameController;
	
	@Mock
	private GameService gameServiceMock;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@BeforeEach
	public void setup() {
		this.mvc = MockMvcBuilders.standaloneSetup(gameController).build();
	}
	
	@Test
	void postStartTest() throws Exception {
		Board board = new Board();
		
		BoardResponseDTO response = generateBoardReponseDTO(board);
		
		when(gameServiceMock.gameStart()).thenReturn(response);
		
		mvc.perform(MockMvcRequestBuilders.post(START_PATH).accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isCreated())
		.andExpect(content().string(equalTo(mapper.writeValueAsString(response))));
	}

	@Test
	void putUpdateTest() throws Exception {
		Board board = new Board();
		
		BoardUpdateRequestDTO updateRequest = new BoardUpdateRequestDTO();
		updateRequest.setGameId(UUID.randomUUID().toString());
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_1);
		updateRequest.setPit(PIT_POSITION_2);
		
		BoardResponseDTO response = generateBoardReponseDTO(board);
		response.setGameId(UUID.fromString(updateRequest.getGameId()));
		
		when(gameServiceMock.moveSeeds(updateRequest)).thenReturn(response);
		
		mvc.perform(MockMvcRequestBuilders.put(UPDATE_PATH)
		.contentType(MediaType.APPLICATION_JSON_VALUE)
		.content(mapper.writeValueAsString(updateRequest)).accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isOk())
		.andExpect(content().string(equalTo(mapper.writeValueAsString(response))));
	}
	
	@Test
	void putUpdateBadRequestTest() throws Exception {
		BoardUpdateRequestDTO updateRequest = new BoardUpdateRequestDTO();
		updateRequest.setGameId(UUID.randomUUID().toString());
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_1);
		updateRequest.setPit(PIT_POSITION_7);
		
		mvc.perform(MockMvcRequestBuilders.put(UPDATE_PATH)
		.contentType(MediaType.APPLICATION_JSON_VALUE)
		.content(mapper.writeValueAsString(updateRequest)).accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	void putUpdateNotFoundTest() throws Exception {
		BoardUpdateRequestDTO updateRequest = new BoardUpdateRequestDTO();
		updateRequest.setGameId(UUID.randomUUID().toString());
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_1);
		updateRequest.setPit(PIT_POSITION_2);
		
		when(gameServiceMock.moveSeeds(updateRequest)).thenThrow(new GameNotFoundException(Constants.GAME_BOARD_NOT_FOUND));
		
		mvc.perform(MockMvcRequestBuilders.put(UPDATE_PATH)
		.contentType(MediaType.APPLICATION_JSON_VALUE)
		.content(mapper.writeValueAsString(updateRequest)).accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isNotFound());
	}
	
	@Test
	void getGameOverStatusTest() throws Exception {
		BoardUpdateRequestDTO updateRequest = new BoardUpdateRequestDTO();
		updateRequest.setGameId(UUID.randomUUID().toString());
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_1);
		updateRequest.setPit(PIT_POSITION_2);
		
		GameOverResponseDTO response = new GameOverResponseDTO();
		response.setGameOver(true);
		
		when(gameServiceMock.gameOverStatus(anyString())).thenReturn(response);
		
		mvc.perform(MockMvcRequestBuilders.get(STATUS_PATH+updateRequest.getGameId()))
		.andExpect(status().isOk())
		.andExpect(content().string(equalTo(mapper.writeValueAsString(response))));
	}
	
	@Test
	void updateGameFinalScoreTest() throws Exception {
		BoardUpdateRequestDTO updateRequest = new BoardUpdateRequestDTO();
		updateRequest.setGameId(UUID.randomUUID().toString());
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_1);
		updateRequest.setPit(PIT_POSITION_2);
		
		FinalScoreResponseDTO response = new FinalScoreResponseDTO();
		response.setPlayer1FinalScore("10");
		response.setPlayer2FinalScore("20");
		
		when(gameServiceMock.updateGameFinalScore(anyString())).thenReturn(response);
		
		mvc.perform(MockMvcRequestBuilders.put(UPDATE_PATH+updateRequest.getGameId()+FINAL_SCORE_PATH))
		.andExpect(status().isOk())
		.andExpect(content().string(equalTo(mapper.writeValueAsString(response))));
	}
	
	@Test
	void deleteGameTest() throws Exception {
		BoardUpdateRequestDTO updateRequest = new BoardUpdateRequestDTO();
		updateRequest.setGameId(UUID.randomUUID().toString());
		updateRequest.setPlayerTurn(PlayerEnum.PLAYER_1);
		updateRequest.setPit(PIT_POSITION_2);
		
		when(gameServiceMock.deleteGame(anyString())).thenReturn(true);
		
		mvc.perform(MockMvcRequestBuilders.delete(DELETE_PATH+updateRequest.getGameId()))
		.andExpect(status().isOk())
		.andExpect(content().string(equalTo(String.valueOf(true))));
	}
	
	private BoardResponseDTO generateBoardReponseDTO(Board board) {
		BoardResponseDTO response = new BoardResponseDTO();
		response.setGameId(UUID.randomUUID());
		response.setPlayer1Pits(board.getPlayer1Pits());
		response.setPlayer2Pits(board.getPlayer2Pits());
		response.setPlayerTurn(PlayerEnum.PLAYER_1);
		return response;
	}

}
