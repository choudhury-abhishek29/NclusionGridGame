package com.nclusion.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nclusion.model.GAME_STATE;
import com.nclusion.model.Game;
import com.nclusion.repo.GameRepository;

public class GameServiceTest {

    private GameService gameService;
    private GameRepository repo;

    @BeforeEach
    void setup() {
        repo = new GameRepository();
        gameService = new GameService(repo);
    }

    @Test
    void createGame_initialStateWaiting() {
        Game g = gameService.createGame();
        assertNotNull(g.getId());
        assertEquals(GAME_STATE.WAITING_FOR_PLAYERS, g.getState());
        assertEquals(0, g.getPlayers().size());
    }

    @Test
    void joinGame_twoPlayersStartsGame() {
        Game g = gameService.createGame();
        String gid = g.getId();

        Game afterFirst = gameService.joinGame(gid, "alice");
        assertEquals(GAME_STATE.WAITING_FOR_PLAYERS, afterFirst.getState());
        assertEquals(1, afterFirst.getPlayers().size());

        Game afterSecond = gameService.joinGame(gid, "bob");
        assertEquals(GAME_STATE.IN_PROGRESS, afterSecond.getState());
        assertEquals(2, afterSecond.getPlayers().size());
        assertEquals("alice", afterSecond.getCurrentPlayer());
    }

    @Test
    void makeMove_enforcesTurnAndWin() {
        Game g = gameService.createGame();
        String gid = g.getId();
        gameService.joinGame(gid, "alice");
        gameService.joinGame(gid, "bob");

        // alice moves (0,0)
        g = gameService.makeMove(gid, "alice", 0, 0);
        assertEquals("bob", g.getCurrentPlayer());

        // wrong turn for alice
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> gameService.makeMove(gid, "alice", 0, 1));
        assertTrue(ex.getMessage().contains("Not this player's turn"));

        // bob moves
        g = gameService.makeMove(gid, "bob", 1, 1);
        assertEquals("alice", g.getCurrentPlayer());

        // alice completes a row to win
        g = gameService.makeMove(gid, "alice", 0, 1);
        g = gameService.makeMove(gid, "bob", 2, 2);
        g = gameService.makeMove(gid, "alice", 0, 2);
        assertEquals(GAME_STATE.FINISHED, g.getState());
        assertEquals("alice", g.getWinner());
    }
}


