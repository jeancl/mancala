package com.bol.jean.mancala;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bol.jean.mancala.annotation.UUID;
import com.bol.jean.mancala.dtos.BoardResponseDTO;
import com.bol.jean.mancala.dtos.BoardUpdateRequestDTO;
import com.bol.jean.mancala.dtos.FinalScoreResponseDTO;
import com.bol.jean.mancala.dtos.GameOverResponseDTO;

import lombok.AllArgsConstructor;

//Only enabling CORS to facilitate calling service directly from "Mock" HTML / JS front end 
@CrossOrigin
@RestController
@AllArgsConstructor
public class GameController {

	private final GameService gameService;
	
	@PostMapping(path = "/start", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BoardResponseDTO> startGame() {
		return new ResponseEntity<>(gameService.gameStart(), HttpStatus.CREATED);
	}
	
	@PutMapping(path = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BoardResponseDTO> updateGame(@RequestBody @Valid BoardUpdateRequestDTO boardUpdateRequestDTO) {
		return new ResponseEntity<>(gameService.moveSeeds(boardUpdateRequestDTO), HttpStatus.OK);
	}
	
	@GetMapping(path = "/status/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GameOverResponseDTO> getGameOverStatus(@PathVariable @UUID @Valid String gameId) {
		return new ResponseEntity<>(gameService.gameOverStatus(gameId), HttpStatus.OK);
	}
	
	@PutMapping(path = "/update/{gameId}/finalscore", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FinalScoreResponseDTO> updateGameFinalScore(@PathVariable @UUID @Valid String gameId) {
		return new ResponseEntity<>(gameService.updateGameFinalScore(gameId), HttpStatus.OK);
	}
	
	@DeleteMapping(path = "/delete/{gameId}")
	public ResponseEntity<Boolean> deleteGame(@PathVariable @UUID @Valid String gameId) {
		return new ResponseEntity<>(gameService.deleteGame(gameId), HttpStatus.OK);
	}
}
