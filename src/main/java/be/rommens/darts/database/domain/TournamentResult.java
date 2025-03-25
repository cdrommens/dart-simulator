package be.rommens.darts.database.domain;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("TOURNAMENTS_RESULTS")
public class TournamentResult {

    @Id
    private String id;
    private String tournamentId;
    private String playerId;
    private LocalDate date;
    @Column("RESULT")
    private RoundResult result;
    private double money;
    @Column("PRO_TOUR_OOM")
    private boolean isProTourOom;
    @Column("EUROPEAN_TOUR_OOM")
    private boolean isEuropeanTourOom;
    @Column("PC_OOM")
    private boolean isPlayersChampionshipOom;

}
