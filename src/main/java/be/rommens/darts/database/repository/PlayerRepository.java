package be.rommens.darts.database.repository;

import be.rommens.darts.database.domain.Player;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends CrudRepository<Player, UUID> {

    @Query("SELECT * FROM PLAYERS")
    List<Player> getAll();
}
