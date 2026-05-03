package repositories;

import models.Player;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PlayerRepository {
    private List<Player> players = new ArrayList<>();

    public void addPlayer(Player player) {
        players.add(player);
    }

    public List<Player> getPlayersByGameId(int gameId) {
        List<Player> gamePlayers = new ArrayList<>();
        for (Player p : players) {
            if (p.getGameId() == gameId) {
                gamePlayers.add(p);
            }
        }
        return gamePlayers;
    }
}
