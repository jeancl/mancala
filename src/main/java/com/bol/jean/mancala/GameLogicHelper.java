package com.bol.jean.mancala;

import org.springframework.stereotype.Component;

import com.bol.jean.mancala.dtos.BoardUpdateRequestDTO;
import com.bol.jean.mancala.model.Board;
import com.bol.jean.mancala.model.PlayerEnum;

/**
 * Helper class with game logic
 */
@Component
public class GameLogicHelper {

	private static final int LAST_SEED_1 = 1;

	/**
	 * Method with logic to move seeds to next pit
	 * 
	 * @param moveSeedRequest
	 * @param boardToUpdate
	 */
	public void moveSeedLogic(BoardUpdateRequestDTO moveSeedRequest, Board boardToUpdate) {
		// Initialize auxiliary local variables 
		int movingSeedAmount;
		int movePit = Integer.valueOf(moveSeedRequest.getPit());
		PlayerEnum currentSeedMovePlayerSide = moveSeedRequest.getPlayerTurn();
		int currentMovingPlayerPits[];
			
		if (PlayerEnum.PLAYER_1.equals(moveSeedRequest.getPlayerTurn())) {
			movingSeedAmount = boardToUpdate.removeSeeds(boardToUpdate.getPlayer1Pits(), movePit);
			currentMovingPlayerPits = boardToUpdate.getPlayer1Pits();
		} else {
			movingSeedAmount = boardToUpdate.removeSeeds(boardToUpdate.getPlayer2Pits(), movePit);
			currentMovingPlayerPits = boardToUpdate.getPlayer2Pits();
		}
		
		// Loop to distribute seeds on pits
		// Complexity O(n)
		for(int i = movingSeedAmount; i > 0; i--) {
			// Go to next pit
			movePit--;
			
			// Check if next pit is smaller than player's bigger pit position 0 and move to other side of the board
			// Also change to next board side if it's opposite player bigger pit position 0
			if(movePit < 0 || (movePit == 0 && !currentSeedMovePlayerSide.equals(moveSeedRequest.getPlayerTurn()))) {
				if (PlayerEnum.PLAYER_1.equals(currentSeedMovePlayerSide)) {
					currentSeedMovePlayerSide = PlayerEnum.PLAYER_2;
					currentMovingPlayerPits = boardToUpdate.getPlayer2Pits();
					movePit = boardToUpdate.getPlayer2Pits().length-1;
				} else {
					currentSeedMovePlayerSide = PlayerEnum.PLAYER_1;
					currentMovingPlayerPits = boardToUpdate.getPlayer1Pits();
					movePit = boardToUpdate.getPlayer1Pits().length-1;
				}
			}
			
			// Distribute 1 seed to current side pit
			boardToUpdate.addSeed(currentMovingPlayerPits, movePit);
			
			// If last seed check seed capture logic and call logic to update player's turn
			if (i == LAST_SEED_1) {
				checkCaptureSeeds(moveSeedRequest, boardToUpdate, movePit, currentSeedMovePlayerSide, currentMovingPlayerPits);

				updatePlayerTurn(moveSeedRequest, boardToUpdate, movePit, currentSeedMovePlayerSide);
			}
		}
	}

	/**
	 * Method to check capture seed logic
	 * 
	 * @param moveSeedRequest
	 * @param boardToUpdate
	 * @param movePit
	 * @param currentSeedMovePlayerSide
	 * @param currentMovingPlayerPits
	 */
	private void checkCaptureSeeds(BoardUpdateRequestDTO moveSeedRequest, Board boardToUpdate, int movePit, PlayerEnum currentSeedMovePlayerSide, int[] currentMovingPlayerPits) {
		// If its current player turn, not in his own big pit position and only 1 seed landed in an empty pit capture the seeds from opposite pit
		if(currentSeedMovePlayerSide.equals(moveSeedRequest.getPlayerTurn()) && movePit != 0 && boardToUpdate.getSeedsAmount(currentMovingPlayerPits, movePit) == 1) {
			int capturedSeedsAmount;
			if(PlayerEnum.PLAYER_1.equals(moveSeedRequest.getPlayerTurn())) {
				capturedSeedsAmount = boardToUpdate.removeSeeds(boardToUpdate.getPlayer2Pits(), boardToUpdate.getPlayer2Pits().length-movePit);
				boardToUpdate.addSeedToBigPit(boardToUpdate.getPlayer1Pits(), capturedSeedsAmount);
			} else {
				capturedSeedsAmount = boardToUpdate.removeSeeds(boardToUpdate.getPlayer1Pits(), boardToUpdate.getPlayer1Pits().length-movePit);
				boardToUpdate.addSeedToBigPit(boardToUpdate.getPlayer2Pits(), capturedSeedsAmount);
			}
		}
	}
	
	/**
	 * Method to validate correct player turn
	 * If wrong player tried to play through cheating and it's not his turn this method returns true
	 * 
	 * @param moveSeedRequest
	 * @param boardToUpdate
	 * @return boolean
	 */
	public boolean checkPlayerTurnNotAllowed(BoardUpdateRequestDTO moveSeedRequest, Board boardToUpdate) {
		return !boardToUpdate.getPlayerTurn().equals(moveSeedRequest.getPlayerTurn());
	}
	
	
	/**
	 * Method to move remaining seeds to players bigger pit after game ended
	 * 
	 * @param boardToUpdate
	 */
	public void updateBoardToGetFinalScore(Board boardToUpdate) {
		for (int i = 6; i > 0; i--) {
			int seedsRemoved = boardToUpdate.removeSeeds(boardToUpdate.getPlayer1Pits(), i);
			boardToUpdate.addSeedToBigPit(boardToUpdate.getPlayer1Pits(), seedsRemoved);
			
			seedsRemoved = boardToUpdate.removeSeeds(boardToUpdate.getPlayer2Pits(), i);
			boardToUpdate.addSeedToBigPit(boardToUpdate.getPlayer2Pits(), seedsRemoved);
		}
	}

	/**
	 * Logic to change player's turn
	 * 
	 * @param moveSeedRequest
	 * @param boardToUpdate
	 * @param movePit
	 * @param currentSeedMovePlayerSide
	 */
	private void updatePlayerTurn(BoardUpdateRequestDTO moveSeedRequest, Board boardToUpdate, int movePit, PlayerEnum currentSeedMovePlayerSide) {
		if(movePit == 0) {
			// Repeat same player turn if last seed lands on his bigger pit
			boardToUpdate.setPlayerTurn(moveSeedRequest.getPlayerTurn());
		} else {
			// Change to next Player turn
			if (PlayerEnum.PLAYER_1.equals(moveSeedRequest.getPlayerTurn())) {
				boardToUpdate.setPlayerTurn(PlayerEnum.PLAYER_2);
			} else {
				boardToUpdate.setPlayerTurn(PlayerEnum.PLAYER_1);
			}
		}
	}
	
}
