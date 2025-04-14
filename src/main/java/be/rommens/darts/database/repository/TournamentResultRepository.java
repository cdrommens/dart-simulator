package be.rommens.darts.database.repository;

import be.rommens.darts.database.domain.TournamentResult;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface TournamentResultRepository extends CrudRepository<TournamentResult, UUID> {

    @Query("SELECT * FROM TOURNAMENTS_RESULTS ORDER BY date")
    List<TournamentResult> getAllOrderedByDate();

}
