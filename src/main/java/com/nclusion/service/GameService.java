package com.nclusion.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.nclusion.model.GAME_STATE;
import com.nclusion.model.Game;
import com.nclusion.model.Player;
import com.nclusion.repo.GameRepository;

@Service
public class GameService {

	private final GameRepository repo;

    public GameService(GameRepository repo) {
        this.repo = repo;
    }

    public Game createGame() {
        return repo.createNewGame();
    }
    
    public Game joinGame(String gameId, String playerId) {
        Game game = repo.findGame(gameId).orElseThrow(() -> new IllegalArgumentException("Game not found"));
        synchronized (game) {
            if (game.getState() != GAME_STATE.WAITING_FOR_PLAYERS) {
                throw new IllegalStateException("Game already started");
            }
            if (game.getPlayers().contains(playerId)) {
                return game;
            }
            if (game.getPlayers().size() >= 2) {
                throw new IllegalStateException("Game already has 2 players");
            }
            game.getPlayers().add(playerId);
            
            if (game.getPlayers().size() == 2) {
                game.setState(GAME_STATE.IN_PROGRESS);
                game.setCurrentPlayer(game.getPlayers().get(0));
            }
            repo.saveGame(game);
            repo.getOrCreateStats(playerId); // ensure stats exist
            return game;
        }
    }
    
    public Game makeMove(String gameId, String playerId, int row, int col) {
        Game game = repo.findGame(gameId).orElseThrow(() -> new IllegalArgumentException("Game not found"));
        synchronized (game) {
            if (game.getState() != GAME_STATE.IN_PROGRESS) {
                throw new IllegalStateException("Game is not in progress");
            }
            if (!game.getPlayers().contains(playerId)) {
                throw new IllegalArgumentException("Player not part of this game");
            }
            if (!playerIdEquals(game.getCurrentPlayer(), playerId)) {
                throw new IllegalStateException("Not this player's turn");
            }
            validateCell(row, col);
            String[][] board = game.getBoard();
            if (board[row][col] != "") {
                throw new IllegalStateException("Cell already occupied");
            }
            board[row][col] = playerId;
            game.incrementMoves();

            // check win
            if (hasPlayerWon(board, playerId)) {
                game.setWinner(playerId);
                game.setState(GAME_STATE.FINISHED);
                updateStatsOnWin(game, playerId);
            } else if (game.getMoveCount() == 9) {
                // draw
                game.setState(GAME_STATE.FINISHED);
                updateStatsOnDraw(game);
            } else {
                // switch turn
                String next = game.getPlayers().get(0).equals(playerId)
                        ? game.getPlayers().get(1)
                        : game.getPlayers().get(0);
                game.setCurrentPlayer(next);
            }
            repo.saveGame(game);
            return game;
        }
    }
    
    private boolean playerIdEquals(String a, String b) {
        return a != null && a.equals(b);
    }

    private void validateCell(int row, int col) {
        Assert.isTrue(row >= 0 && row < 3, "row out of bounds");
        Assert.isTrue(col >= 0 && col < 3, "col out of bounds");
    }
    
    private boolean hasPlayerWon(String[][] board, String p) {
        // rows
        for (int r = 0; r < 3; r++) {
            if (board[r][0].equals(p) && board[r][1].equals(p) && board[r][2].equals(p)) return true;
        }
        // cols
        for (int c = 0; c < 3; c++) {
            if (board[0][c].equals(p) && board[1][c].equals(p) && board[2][c].equals(p)) return true;
        }
        // diags
        if (board[0][0].equals(p) && board[1][1].equals(p) && board[2][2].equals(p)) return true;
        if (board[0][2].equals(p) && board[1][1].equals(p) && board[2][0].equals(p)) return true;
        return false;
    }

    private void updateStatsOnWin(Game game, String winnerId) {
        Player winner = repo.getOrCreateStats(winnerId);
        winner.incWins(game.getMoveCount());

        // loser
        List<String> players = game.getPlayers();
        for (String pid : players) {
            if (pid != winnerId) {
                repo.getOrCreateStats(pid).incLosses();
            }
        }
    }

    private void updateStatsOnDraw(Game game) {
        for (String pid : game.getPlayers()) {
            repo.getOrCreateStats(pid).incDraws();
        }
    }

    public Game getGame(String gameId) {
        return repo.findGame(gameId).orElseThrow(() -> new IllegalArgumentException("Game not found"));
    }

}
