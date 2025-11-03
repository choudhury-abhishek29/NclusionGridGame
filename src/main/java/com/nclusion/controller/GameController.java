package com.nclusion.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> createGame() {
        try {
            return ResponseEntity.ok(gameService.createGame());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{gameId}/join")
    public ResponseEntity<?> joinGame(@PathVariable String gameId, @RequestParam String playerId) {
        if (!repo.playerExists(playerId)) {
            return ResponseEntity.badRequest().body("Player not registered");
        }
        try {
            return ResponseEntity.ok(gameService.joinGame(gameId, playerId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public static class MoveRequest {
        public String playerId;
        public int row;
        public int col;
    }

    @PostMapping("/{gameId}/moves")
    public ResponseEntity<?> makeMove(@PathVariable String gameId, @RequestBody MoveRequest req) {
        try {
            return ResponseEntity.ok(gameService.makeMove(gameId, req.playerId, req.row, req.col));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<?> getGame(@PathVariable String gameId) {
        try {
            return ResponseEntity.ok(gameService.getGame(gameId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<?> leaderboard(@RequestParam(defaultValue = "winCount") String by) {
        try {
            if ("efficiency".equalsIgnoreCase(by)) {
                return ResponseEntity.ok(leaderboardService.top3ByEfficiency());
            }
            return ResponseEntity.ok(leaderboardService.top3ByWinCount());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(path = "/on")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Game is on...!!!");
    }

}
