package com.nclusion.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class Game {
	
	private String id;
	private String[][] board;
	private List<String> players;
	private String currentPlayer;
	private GAME_STATE state; 
	private String winner;
	private Integer moveCount;
	
	public Game() {
		this.id = UUID.randomUUID().toString();
		this.moveCount = 0;
		this.state = GAME_STATE.WAITING_FOR_PLAYERS;
		this.board = initializeBoard(3, 3);
		this.players = new ArrayList<String>();
	}

	public Game(String id, String[][] board, 
				List<String> players, 
				String currentPlayer, 
				String winner, 
				Integer moveCount) {
		super();
		this.id = UUID.randomUUID().toString();
		this.board = board;
		this.players = players;
		this.currentPlayer = currentPlayer;
		this.winner = winner;
		this.moveCount = moveCount;
	}
	
	private String[][] initializeBoard(int rows, int cols) {
        String[][] newBoard = new String[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                newBoard[i][j] = "";  // Initialize all cells as empty strings
            }
        }
        return newBoard;
    }

	public String getId() {
		return id;
	}

	public String[][] getBoard() {
		return board;
	}

	public void setBoard(String[][] board) {
		this.board = board;
	}

	public List<String> getPlayers() {
		return players;
	}

	public void setPlayers(List<String> players) {
		this.players = players;
	}

	public String getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(String currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public GAME_STATE getState() {
		return state;
	}

	public void setState(GAME_STATE state) {
		this.state = state;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public Integer getMoveCount() {
		return moveCount;
	}

	public void setMoveCount(Integer moveCount) {
		this.moveCount = moveCount;
	}
	
	public void incrementMoves() {
        this.moveCount++;
    }
	

}
