package com.nclusion.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
		return ResponseEntity.ok(repo.findAllPlayers());
	}
	
    @PostMapping("/register")
    public ResponseEntity<?> registerPlayer(@RequestParam String playerId, 
    		@RequestParam(defaultValue = "join")String mode) {
        boolean created = repo.registerPlayer(playerId);
        if (!created) {
            return ResponseEntity.badRequest().body("Player already exists");
        }
        
//        switch(mode) {
//        case "host":
//        	break;
//        case "join":
//        	break;
//        }
        return ResponseEntity.ok(playerId);
    }

}
