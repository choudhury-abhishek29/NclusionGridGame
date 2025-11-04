package com.nclusion.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nclusion.model.GAME_STATE;
import com.nclusion.model.Game;
import com.nclusion.repo.GameRepository;
import com.nclusion.service.GameService;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    private final GameRepository repo;
    private final GameService gameService;

    public PlayerController(GameRepository repo, GameService gameService) {
        this.repo = repo;
        this.gameService = gameService;
    }

    @GetMapping("/listAllPlayers")
    public ResponseEntity<?> getAllPlayers() {
        try {
            return ResponseEntity.ok(repo.findAllPlayers());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerPlayer(@RequestParam String playerId,
            @RequestParam(defaultValue = "join") String mode) {
        try {
            boolean created = repo.registerPlayer(playerId);
            if (!created) {
                return ResponseEntity.badRequest().body("Player already exists");
            }
            if ("host".equalsIgnoreCase(mode)) {
                var game = gameService.createGame();
                gameService.joinGame(game.getId(), playerId);
                Map<String, String> response = new HashMap<>();
                response.put("playerId", playerId);
                response.put("gameId", game.getId());
                return ResponseEntity.ok(response);
            }
            // For 'join' mode, return playerId and array of available game IDs
            List<String> waitingGameIds = repo.findAllGames().stream()
                    .filter(game -> game.getState() == GAME_STATE.WAITING_FOR_PLAYERS)
                    .map(Game::getId)
                    .collect(Collectors.toList());
            Map<String, Object> response = new HashMap<>();
            response.put("playerId", playerId);
            response.put("gameIds", waitingGameIds);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<?> getPlayer(@PathVariable String playerId) {
        try {
            if (!repo.playerExists(playerId)) {
                return ResponseEntity.badRequest().body("Player not registered");
            }
            return ResponseEntity.ok(repo.getOrCreateStats(playerId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
