package com.nclusion.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nclusion.model.Player;
import com.nclusion.repo.GameRepository;


@Service
public class LeaderboardService {

	private final GameRepository repo;

    public LeaderboardService(GameRepository repo) {
        this.repo = repo;
    }

    public List<Player> top3ByWinCount() {
        return repo.findAllStats().stream()
                .sorted(Comparator.comparingInt(Player::getGamesWon).reversed()
                        .thenComparing(Player::getEfficiency))
                .limit(3)
                .collect(Collectors.toList());
    }

    public List<Player> top3ByEfficiency() {
        return repo.findAllStats().stream()
                .filter(s -> s.getGamesWon() > 0)
                .sorted(Comparator.comparingDouble(Player::getEfficiency)
                        .thenComparing((Player s) -> -s.getGamesWon()))
                .limit(3)
                .collect(Collectors.toList());
    }

}
