package be.rommens.darts.database.loader;

import be.rommens.darts.database.domain.Player;
import be.rommens.darts.database.domain.RoundResult;
import be.rommens.darts.database.domain.Tournament;
import be.rommens.darts.database.domain.TournamentQualifiedPlayer;
import be.rommens.darts.database.domain.TournamentResult;
import be.rommens.darts.database.repository.TournamentQualifiedPlayerRepository;
import be.rommens.darts.database.repository.TournamentRepository;
import be.rommens.darts.database.repository.TournamentResultRepository;
import be.rommens.darts.scraper.core.Breakdown;
import be.rommens.darts.scraper.core.OrderOfMeritScrapeResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TournamentLoader {

    private final TournamentRepository tournamentRepository;
    private final TournamentQualifiedPlayerRepository qualifiedPlayerRepository;
    private final TournamentResultRepository resultRepository;

    public void loadTournamentQualifiedPlayers(List<Player> players) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<OrderOfMeritScrapeResult> orderOfMeritScrapeResults = objectMapper
                .readerForListOf(OrderOfMeritScrapeResult.class)
                .readValue(this.getClass().getClassLoader().getResource("scraper/order_of_merit.json"));

        log.info("Start loading tournament qualified players and results...");
        Map<String, Tournament> tournaments = tournamentRepository.getAll().stream().collect(Collectors.toMap(Tournament::getId, Function.identity()));

        for (OrderOfMeritScrapeResult orderOfMeritScrapeResult : orderOfMeritScrapeResults) {
            Player player = players.stream().filter(p -> p.getName().equalsIgnoreCase(orderOfMeritScrapeResult.name()))
                    .findFirst().orElseThrow(() -> new IllegalStateException("Could not find player with name " + orderOfMeritScrapeResult.name()));
            List<TournamentQualifiedPlayer> qualifiedPlayersToAdd = new ArrayList<>();
            List<TournamentResult> results = new ArrayList<>();
            for (Breakdown breakdown : orderOfMeritScrapeResult.detail().breakdowns().stream().filter(b -> !b.money().equals("-")).toList()) {
                String tournamentKey = Tournament.getKey(breakdown.tournament(), breakdown.date().substring(0,4));
                qualifiedPlayersToAdd.add(TournamentQualifiedPlayer.builder()
                        .tournamentId(tournaments.get(tournamentKey).getId())
                        .playerId(player.getId())
                        .build());
                log.info(String.format("%s - %s - %s - %s", player.getName(), tournamentKey, breakdown.date(), breakdown.money()));
                results.add(TournamentResult.builder()
                        .tournamentId(tournaments.get(tournamentKey).getId())
                        .playerId(player.getId())
                        .result(mapResult(tournamentKey, breakdown.money()))
                        .date(LocalDate.parse(breakdown.date(), DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                        .money(Double.parseDouble(breakdown.money()))
                        .isProTourOom(isProTourOom(tournamentKey))
                        .isEuropeanTourOom(isEuropeanTourOom(tournamentKey))
                        .isPlayersChampionshipOom(isPcOom(tournamentKey))
                        .build());
            }
            qualifiedPlayerRepository.saveAll(qualifiedPlayersToAdd);
            resultRepository.saveAll(results);
        }
        log.info("Finished loading tournament qualified players and results...");
    }

    private static boolean isProTourOom(String key) {
        return switch (key.substring(0, 2)) {
            case "PC" -> true;
            case "ET" -> true;
            default -> false;
        };
    }

    private static boolean isEuropeanTourOom(String key) {
        return key.startsWith("ET");
    }

    private static boolean isPcOom(String key) {
        return key.startsWith("PC");
    }

    private static RoundResult mapResult(String key, String money) {
        return switch (key.substring(0, 2)) {
            case "WC" -> mapWcResult(money);
            case "UK" -> mapUkResult(money);
            case "WM" -> mapWmResult(money);
            case "GS" -> null;
            case "GP" -> mapGpResult(money);
            case "PF" -> mapPfResult(money);
            case "MA" -> null;
            case "EC" -> mapEcResult(money);
            case "PC" -> key.endsWith("2023") ? mapPc2023Result(money) : mapPcResult(money);
            case "ET" -> mapEtResult(money);
            default -> throw new IllegalStateException("Unexpected value: " + key);
        };
    }

    private static RoundResult mapWcResult(String money) {
        return switch (money) {
            case "7.5" -> RoundResult.FIRST_ROUND;
            case "15" -> RoundResult.SECOND_ROUND;
            case "25" -> RoundResult.THIRD_ROUND;
            case "35" -> RoundResult.FOURTH_ROUND;
            case "50" -> RoundResult.QUARTER_FINAL;
            case "100" -> RoundResult.SEMI_FINAL;
            case "200" -> RoundResult.RUNNER_UP;
            case "500" -> RoundResult.WINNER;
            default -> throw new IllegalStateException("Unexpected value at WC: " + money);
        };
    }

    private static RoundResult mapUkResult(String money) {
        return switch (money) {
            case "0" -> RoundResult.FIRST_ROUND;
            case "1" -> RoundResult.SECOND_ROUND;
            case "1.5" -> RoundResult.THIRD_ROUND;
            case "2.5" -> RoundResult.FOURTH_ROUND;
            case "5" -> RoundResult.FIFTH_ROUND;
            case "10" -> RoundResult.SIXTH_ROUND;
            case "15" -> RoundResult.QUARTER_FINAL;
            case "30" -> RoundResult.SEMI_FINAL;
            case "50" -> RoundResult.RUNNER_UP;
            case "110" -> RoundResult.WINNER;
            default -> throw new IllegalStateException("Unexpected value at UK: " + money);
        };
    }

    private static RoundResult mapWmResult(String money) {
        return switch (money) {
            case "10" -> RoundResult.FIRST_ROUND;
            case "15" -> RoundResult.SECOND_ROUND;
            case "30" -> RoundResult.QUARTER_FINAL;
            case "50" -> RoundResult.SEMI_FINAL;
            case "100" -> RoundResult.RUNNER_UP;
            case "200" -> RoundResult.WINNER;
            default -> throw new IllegalStateException("Unexpected value at WM: " + money);
        };
    }

    private static RoundResult mapGpResult(String money) {
        return switch (money) {
            case "7.5" -> RoundResult.FIRST_ROUND;
            case "15" -> RoundResult.SECOND_ROUND;
            case "25" -> RoundResult.QUARTER_FINAL;
            case "40" -> RoundResult.SEMI_FINAL;
            case "60" -> RoundResult.RUNNER_UP;
            case "120" -> RoundResult.WINNER;
            default -> throw new IllegalStateException("Unexpected value at GP: " + money);
        };
    }

    private static RoundResult mapPfResult(String money) {
        return switch (money) {
            case "3" -> RoundResult.FIRST_ROUND;
            case "6.5" -> RoundResult.SECOND_ROUND;
            case "10" -> RoundResult.THIRD_ROUND;
            case "20" -> RoundResult.QUARTER_FINAL;
            case "30" -> RoundResult.SEMI_FINAL;
            case "60" -> RoundResult.RUNNER_UP;
            case "120" -> RoundResult.WINNER;
            default -> throw new IllegalStateException("Unexpected value at PF: " + money);
        };
    }

    private static RoundResult mapEcResult(String money) {
        return switch (money) {
            case "7.5" -> RoundResult.FIRST_ROUND;
            case "15" -> RoundResult.SECOND_ROUND;
            case "25" -> RoundResult.QUARTER_FINAL;
            case "40" -> RoundResult.SEMI_FINAL;
            case "60" -> RoundResult.RUNNER_UP;
            case "120" -> RoundResult.WINNER;
            default -> throw new IllegalStateException("Unexpected value at EC: " + money);
        };
    }

    private static RoundResult mapPc2023Result(String money) {
        return switch (money) {
            case "0" -> RoundResult.FIRST_ROUND;
            case "0.75" -> RoundResult.SECOND_ROUND;
            case "1.25" -> RoundResult.THIRD_ROUND;
            case "2" -> RoundResult.FOURTH_ROUND;
            case "3" -> RoundResult.QUARTER_FINAL;
            case "4" -> RoundResult.SEMI_FINAL;
            case "8" -> RoundResult.RUNNER_UP;
            case "12" -> RoundResult.WINNER;
            default -> throw new IllegalStateException("Unexpected value at PC2023: " + money);
        };
    }

    private static RoundResult mapPcResult(String money) {
        return switch (money) {
            case "0" -> RoundResult.FIRST_ROUND;
            case "1" -> RoundResult.SECOND_ROUND;
            case "1.5" -> RoundResult.THIRD_ROUND;
            case "2.5" -> RoundResult.FOURTH_ROUND;
            case "3.5" -> RoundResult.QUARTER_FINAL;
            case "5" -> RoundResult.SEMI_FINAL;
            case "10" -> RoundResult.RUNNER_UP;
            case "15" -> RoundResult.WINNER;
            default -> throw new IllegalStateException("Unexpected value at PC: " + money);
        };
    }

    private static RoundResult mapEtResult(String money) {
        return switch (money) {
            case "0" -> RoundResult.FIRST_ROUND;
            case "1.25" -> RoundResult.SECOND_ROUND;
            case "2.5" -> RoundResult.THIRD_ROUND;
            case "4" -> RoundResult.FOURTH_ROUND;
            case "6" -> RoundResult.QUARTER_FINAL;
            case "8.5" -> RoundResult.SEMI_FINAL;
            case "12" -> RoundResult.RUNNER_UP;
            case "30" -> RoundResult.WINNER;
            default -> throw new IllegalStateException("Unexpected value at ET: " + money);
        };
    }

}
