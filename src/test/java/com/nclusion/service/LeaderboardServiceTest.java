package com.nclusion.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nclusion.model.Player;
import com.nclusion.repo.GameRepository;

public class LeaderboardServiceTest {

    private GameRepository repo;
    private LeaderboardService leaderboardService;

    @BeforeEach
    void setup() {
        repo = new GameRepository();
        leaderboardService = new LeaderboardService(repo);

        // seed some players
        Player a = repo.getOrCreateStats("alice");
        a.incWins(3);
        a.incWins(4);

        Player b = repo.getOrCreateStats("bob");
        b.incWins(5);
        b.incLosses();

        Player c = repo.getOrCreateStats("charlie");
        c.incLosses();
        c.incDraws();

        Player d = repo.getOrCreateStats("dave");
        d.incWins(7);
        d.incWins(8);
        d.incWins(9);
    }

    @Test
    void top3ByWinCount_ordersByWinsThenEfficiency() {
        List<Player> top = leaderboardService.top3ByWinCount();
        assertEquals(3, top.size());
        // dave (3 wins), alice (2 wins), bob (1 win)
        assertEquals("dave", top.get(0).getName());
        assertEquals("alice", top.get(1).getName());
        assertEquals("bob", top.get(2).getName());
    }

    @Test
    void top3ByEfficiency_filtersPlayersWithAtLeastOneWin() {
        List<Player> top = leaderboardService.top3ByEfficiency();
        assertTrue(top.stream().allMatch(p -> p.getGamesWon() > 0));
        assertTrue(top.size() <= 3);
    }
}


