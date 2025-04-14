package be.rommens.darts.testutils;

import be.rommens.darts.database.domain.Player;
import be.rommens.darts.database.domain.RoundResult;
import be.rommens.darts.database.domain.TournamentResult;
import java.io.File;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.SneakyThrows;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

public class TestFileLoader {

    @SneakyThrows
    public static List<Player> loadPlayers(File file) {
        List<Player> players = new ArrayList<>();
        FileUtils.readLines(file, Charset.defaultCharset()).stream()
                .skip(1)
                .forEach(line -> {
                    String[] splitted = line.split(",");
                    players.add(Player.builder()
                            .accuracyBull(Double.parseDouble(trimCharacter(splitted[0])))
                            .accuracyDouble(Double.parseDouble(trimCharacter(splitted[1])))
                            .accuracyDouble3rd(Double.parseDouble(trimCharacter(splitted[2])))
                            .accuracyStartingDouble(Double.parseDouble(trimCharacter(splitted[3])))
                            .accuracyTreble(Double.parseDouble(trimCharacter(splitted[4])))
                            .average(Double.parseDouble(trimCharacter(splitted[5])))
                            .country(trimCharacter(splitted[6]))
                            .first9Average(Double.parseDouble(trimCharacter(splitted[7])))
                            .hasTourCard(Boolean.parseBoolean(trimCharacter(splitted[8])))
                            .id(trimCharacter(splitted[9]))
                            .name(trimCharacter(splitted[10]))
                            .build());
                });
        return players;
    }

    @SneakyThrows
    public static List<TournamentResult> loadTournamentResults(File file) {
        List<TournamentResult> tournamentResults = new ArrayList<>();
        FileUtils.readLines(file, Charset.defaultCharset()).stream()
                .skip(1)
                .forEach(line -> {
                    String[] splitted = line.split(",");
                    tournamentResults.add(TournamentResult.builder()
                                    .date(LocalDate.parse(trimCharacter(splitted[0])))
                                    .id(trimCharacter(splitted[1]))
                                    .isEuropeanTourOom(Boolean.parseBoolean(trimCharacter(splitted[2])))
                                    .isPlayersChampionshipOom(Boolean.parseBoolean(trimCharacter(splitted[3])))
                                    .isProTourOom(Boolean.parseBoolean(trimCharacter(splitted[4])))
                                    .money(Double.parseDouble(trimCharacter(splitted[5])))
                                    .playerId(trimCharacter(splitted[6]))
                                    .result(RoundResult.valueOf(trimCharacter(splitted[7])))
                                    .tournamentId(trimCharacter(splitted[8]))
                            .build());
                });
        return tournamentResults;
    }

    private static String trimCharacter(String input, char trimChar) {
        int start = 0;
        int end = input.length() - 1;

        while (start <= end && input.charAt(start) == trimChar) {
            start++;
        }

        while (end >= start && input.charAt(end) == trimChar) {
            end--;
        }

        return input.substring(start, end + 1);
    }

    private static String trimCharacter(String input) {
        return trimCharacter(input, '\'');
    }
}
