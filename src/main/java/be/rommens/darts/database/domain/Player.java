package be.rommens.darts.database.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("PLAYERS")
public class Player {

    @Id
    private String id;
    private String name;
    private String country;
    @Column("TOUR_CARD")
    private boolean hasTourCard;
    private double average;
    @Column("FIRST_NINE_AVERAGE")
    private double first9Average;
    private double accuracyStartingDouble;
    private double accuracyBull;
    private double accuracyDouble;
    @Column("ACCURACY_DOUBLE_THIRD")
    private double accuracyDouble3rd;
    private double accuracyTreble;
}
