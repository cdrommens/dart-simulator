package be.rommens.darts.database.repository;

import be.rommens.darts.database.domain.TournamentResult;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface TournamentResultRepository extends CrudRepository<TournamentResult, UUID> {

}
