package be.rommens.darts.database.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("TOURNAMENTS_PLAYERS")
public class TournamentQualifiedPlayer {

    @Id
    private String id;
    private String tournamentId;
    private String playerId;
    private int seed;

}
