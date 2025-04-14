package be.rommens.darts.database.domain;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

@Data
@Builder
public class OrderOfMerit {

    @Id
    private String id;
    private OomType oomType;
    private int rank;
    @Column("PREV_RANK")
    private int previousRank;
    private UUID playerId;
    private String playerName;
    private double money;
}
