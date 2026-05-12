package repositories;

import Entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {

    List<Player> findByGameId(int gameId);

    Player findByDisplayName(String displayName);
    boolean existsByDisplayNameAndGameId(String displayName, int gameId);
}