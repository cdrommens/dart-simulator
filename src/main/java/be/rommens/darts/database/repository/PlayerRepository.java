package be.rommens.darts.database.repository;

import be.rommens.darts.database.domain.Player;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends CrudRepository<Player, UUID> {

}
