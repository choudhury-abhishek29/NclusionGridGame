package com.nclusion.repo;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;
import com.nclusion.model.Game;
import com.nclusion.model.Player;

@Repository
public class GameRepository {

    private final Map<String, Game> games = new ConcurrentHashMap<>();
    private final Map<String, Player> stats = new ConcurrentHashMap<>();
    private final Set<String> players = Collections.synchronizedSet(new HashSet<>());

    public Game saveGame(Game game) {
        games.put(game.getId(), game);
        return game;
    }

    public Optional<Game> findGame(String id) {
        return Optional.ofNullable(games.get(id));
    }

    public Player getOrCreateStats(String playerId) {
        return stats.computeIfAbsent(playerId, Player::new);
    }

    public Collection<Player> findAllStats() {
        return stats.values();
    }

    public Game createNewGame() {
        Game g = new Game();
        saveGame(g);
        return g;
    }

    public boolean registerPlayer(String playerId) {
        if (!playerExists(playerId)) {
            Player player = new Player(playerId);
            players.add(player.getName());
            return true;
        }
        return false;
    }

    public boolean playerExists(String playerId) {
        return players.contains(playerId);
    }

    public Collection<Game> findAllGames() {
        return games.values();
    }

    public Set<String> findAllPlayers() {
        return players;
    }

}
