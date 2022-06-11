package com.bol.jean.mancala;

import static com.bol.jean.mancala.Constants.GAME_BOARD_NOT_FOUND;
import static com.bol.jean.mancala.Constants.GAME_NOT_OVER;
import static com.bol.jean.mancala.Constants.MAX_NUMBER_OF_GAMES_EXCEED;
import static com.bol.jean.mancala.Constants.PLAYER_NOT_ALLOWED;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bol.jean.mancala.dtos.BoardResponseDTO;
import com.bol.jean.mancala.dtos.BoardUpdateRequestDTO;
import com.bol.jean.mancala.dtos.FinalScoreResponseDTO;
import com.bol.jean.mancala.dtos.GameOverResponseDTO;
import com.bol.jean.mancala.exceptions.GameNotFoundException;
import com.bol.jean.mancala.exceptions.GameNotOverException;
import com.bol.jean.mancala.exceptions.MaxNumberOfGamesExceedException;
import com.bol.jean.mancala.exceptions.PlayerNotAllowedException;
import com.bol.jean.mancala.model.Board;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameService {

	@Value("${min.inactive.minutes:60}")
	private int minimumInactiveMinutesToDelete;
	
	@Value("${max.amount.active.games:10}")
	private int maxAmountOfActiveGames;
	
	//In memory started games
	//It's not recommended to store stuff in memory like this, can cause memory, performance and security issues
	//TODO Change this to use JPA / MongoDB repository instead in next versions
	private HashMap<UUID, Board> startedGames = new HashMap<>();
	
	private final ModelMapper modelMapper = new ModelMapper();
	
	private final GameLogicHelper gameLogicHelper;
	
	/**
	 * This method:
	 * Create a new game;
	 * Add it to in memory game list;
	 * Return the new board setup
	 * 
	 * @return BoardResponseDTO
	 */
	public BoardResponseDTO gameStart() {
		if(maxAmountOfActiveGames <= startedGames.size()) {
			throw new MaxNumberOfGamesExceedException(MAX_NUMBER_OF_GAMES_EXCEED);
		}
		
		Board board = new Board();
		
		BoardResponseDTO boardDTO = modelMapper.map(board, BoardResponseDTO.class);
		
		startedGames.put(boardDTO.getGameId(), board);
		
		return boardDTO;
	}
	
	/**
	 * Method to move seeds based on Board Update request
	 * 
	 * @param moveSeedRequest
	 * @return BoardResponseDTO
	 */
	public BoardResponseDTO moveSeeds(BoardUpdateRequestDTO moveSeedRequest) {
		Board boardToUpdate = startedGames.get(UUID.fromString(moveSeedRequest.getGameId()));
		
		if(null == boardToUpdate) {
			throw new GameNotFoundException(GAME_BOARD_NOT_FOUND);
		}
		
		if(gameLogicHelper.checkPlayerTurnNotAllowed(moveSeedRequest, boardToUpdate)) {
			throw new PlayerNotAllowedException(PLAYER_NOT_ALLOWED);
		}
		
		gameLogicHelper.moveSeedLogic(moveSeedRequest, boardToUpdate);
		boardToUpdate.setLastUpdatedDate(LocalDateTime.now());
		
		BoardResponseDTO boardDTO = modelMapper.map(boardToUpdate, BoardResponseDTO.class);
		
		return boardDTO;
	}
	
	/**
	 * Method to check and return game over status based on gameId
	 * 
	 * @param gameId
	 * @return GameOverResponseDTO
	 */
	public GameOverResponseDTO gameOverStatus(String gameId) {
		Board boardToUpdate = startedGames.get(UUID.fromString(gameId));
		
		if(null == boardToUpdate) {
			throw new GameNotFoundException(GAME_BOARD_NOT_FOUND);
		}
		
		GameOverResponseDTO reponse = new GameOverResponseDTO();
		
		reponse.setGameOver(boardToUpdate.checkEndGame());
		
		return reponse;
	}
	
	/**
	 * Method to update and return final scores based on gameId
	 * 
	 * @param gameId
	 * @return FinalScoreResponseDTO
	 */
	public FinalScoreResponseDTO updateGameFinalScore(String gameId) {
		Board boardToUpdate = startedGames.get(UUID.fromString(gameId));
		
		if(null == boardToUpdate) {
			throw new GameNotFoundException(GAME_BOARD_NOT_FOUND);
		}
		
		if(!boardToUpdate.checkEndGame()) {
			throw new GameNotOverException(GAME_NOT_OVER);
		}
		
		gameLogicHelper.updateBoardToGetFinalScore(boardToUpdate);
		boardToUpdate.setLastUpdatedDate(LocalDateTime.now());
		
		FinalScoreResponseDTO reponse = new FinalScoreResponseDTO();
		reponse.setPlayer1FinalScore(String.valueOf(boardToUpdate.getPlayer1Score()));
		reponse.setPlayer2FinalScore(String.valueOf(boardToUpdate.getPlayer2Score()));
		
		return reponse;
	}
	
	/**
	 * Method to delete game
	 * 
	 * @param gameId
	 * @return FinalScoreResponseDTO
	 */
	public Boolean deleteGame(String gameId) {
		Board boardToDelete = startedGames.get(UUID.fromString(gameId));
		
		if(null == boardToDelete) {
			throw new GameNotFoundException(GAME_BOARD_NOT_FOUND);
		}
		
		//Check if game is over + last updated date bigger than X minutes before allowing to delete game
		if(!boardToDelete.checkEndGame() && minimumInactiveMinutesToDelete > Duration.between(boardToDelete.getLastUpdatedDate(), LocalDateTime.now()).toMinutes()) {
			throw new GameNotOverException(GAME_NOT_OVER);
		}
		
		startedGames.remove(boardToDelete.getGameId());
		
		return true;
	}
}
