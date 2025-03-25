package be.rommens.darts.database.repository;

import be.rommens.darts.database.domain.Tournament;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface TournamentRepository extends CrudRepository<Tournament, UUID> {

    @Query("SELECT * FROM TOURNAMENTS")
    List<Tournament> getAll();

}
