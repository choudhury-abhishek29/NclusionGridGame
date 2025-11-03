package com.nclusion.model;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class Player {
	private String id;
	private Integer gamesWon;
	private Integer gamesLost;
	private Integer gamesDraw;
	private Integer totalMovesInWins;

	public Player() {
		this.id = UUID.randomUUID().toString();
		this.gamesWon = 0;
		this.gamesLost = 0;
		this.gamesDraw = 0;
		this.totalMovesInWins = 0;
	}

	public Player(String id) {
		super();
		this.id = UUID.randomUUID().toString();
		this.gamesWon = 0;
		this.gamesLost = 0;
		this.gamesDraw = 0;
		this.totalMovesInWins = 0;
	}

	public Player(String id, Integer gamesWon, Integer gamesLost, Integer gamesDraw) {
		super();
		this.id = UUID.randomUUID().toString();
		;
		this.gamesWon = gamesWon;
		this.gamesLost = gamesLost;
		this.gamesDraw = gamesDraw;
		this.totalMovesInWins = 0;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getGamesWon() {
		return gamesWon;
	}

	public void setGamesWon(Integer gamesWon) {
		this.gamesWon = gamesWon;
	}

	public Integer getGamesLost() {
		return gamesLost;
	}

	public void setGamesLost(Integer gamesLost) {
		this.gamesLost = gamesLost;
	}

	public Integer getGamesDraw() {
		return gamesDraw;
	}

	public void setGamesDraw(Integer gamesDraw) {
		this.gamesDraw = gamesDraw;
	}

	public Integer getTotalMovesInWins() {
		return totalMovesInWins;
	}

	public void setTotalMovesInWins(Integer totalMovesInWins) {
		this.totalMovesInWins = totalMovesInWins;
	}

	public double getEfficiency() {
		if (gamesWon == 0)
			return Double.MAX_VALUE;
		return (double) totalMovesInWins / gamesWon;
	}

	public void incWins(int movesInThisWin) {
		this.gamesWon++;
		this.totalMovesInWins += movesInThisWin;
	}

	public void incLosses() {
		this.gamesLost++;
	}

	public void incDraws() {
		this.gamesDraw++;
	}

}
