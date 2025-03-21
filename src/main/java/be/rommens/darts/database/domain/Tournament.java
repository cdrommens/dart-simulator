package be.rommens.darts.database.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("TOURNAMENTS")
public class Tournament {

    @Id
    private String id;
    private String key;
    private String year;
    private String name;
}
