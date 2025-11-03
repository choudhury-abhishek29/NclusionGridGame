package com.nclusion.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nclusion.repo.GameRepository;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    private final GameRepository repo;

    public PlayerController(GameRepository repo) {
        this.repo = repo;
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
            return ResponseEntity.ok(playerId);
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
