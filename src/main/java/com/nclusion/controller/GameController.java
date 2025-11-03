package com.nclusion.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nclusion.model.Game;
import com.nclusion.model.Player;
import com.nclusion.repo.GameRepository;
import com.nclusion.service.GameService;
import com.nclusion.service.LeaderboardService;

@RestController
@RequestMapping("/api/game")
public class GameController {
	
	private final GameService gameService;
    private final GameRepository repo;
    private final LeaderboardService leaderboardService;

    public GameController(GameService gameService, GameRepository repo, LeaderboardService leaderboardService) {
        this.gameService = gameService;
        this.repo = repo;
        this.leaderboardService = leaderboardService;
    }

    // --- Games ---
    @PostMapping("/new")
    public Game createGame() {
        return gameService.createGame();
    }

    @PostMapping("/{gameId}/join")
    public Game joinGame(@PathVariable String gameId, @RequestParam String playerId) {
        if (!repo.playerExists(playerId)) {
            throw new IllegalArgumentException("Player not registered");
        }
        return gameService.joinGame(gameId, playerId);
    }

    public static class MoveRequest {
        public String playerId;
        public int row;
        public int col;
    }

    @PostMapping("/{gameId}/moves")
    public Game makeMove(@PathVariable String gameId, @RequestBody MoveRequest req) {
        return gameService.makeMove(gameId, req.playerId, req.row, req.col);
    }

    @GetMapping("/{gameId}")
    public Game getGame(@PathVariable String gameId) {
        return gameService.getGame(gameId);
    }

    @GetMapping("/leaderboard")
    public List<Player> leaderboard(@RequestParam(defaultValue = "winCount") String by) {
        if ("efficiency".equalsIgnoreCase(by)) {
            return leaderboardService.top3ByEfficiency();
        }
        return leaderboardService.top3ByWinCount();
    }
    
    @GetMapping(path="/on")
    public String healthCheck() {
    	return "Game is on...!!!";
    }

}
