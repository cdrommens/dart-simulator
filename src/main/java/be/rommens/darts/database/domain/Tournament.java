package be.rommens.darts.database.domain;

import java.util.regex.Pattern;
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

    public static String getKey(String name, String year) {
        Pattern pc = Pattern.compile("PC[0-9]{1,2}");
        Pattern et = Pattern.compile("ET[0-9]{1,2}");
        return switch (name.toUpperCase()) {
            case "WORLD CHAMPIONSHIP" -> "WC" + year;
            case "UK OPEN" -> "UK" + year;
            case "WORLD MATCHPLAY" -> "WM" + year;
            case "GRAND SLAM OF DARTS" -> "GS" + year;
            case "WORLD GRAND PRIX" -> "GP" + year;
            case "PLAYERS CHAMPIONSHIP FINALS" -> "PF" + year;
            case "WORLD MASTERS" -> "MA" + year;
            case "EUROPEAN CHAMPIONSHIP" -> "EC" + year;
            case String s when pc.matcher(name).matches() -> name + year;
            case String s when et.matcher(name).matches() -> name + year;
            default -> throw new IllegalStateException("Unexpected tournament value: " + name);
        };
    }
}
