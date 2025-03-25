package be.rommens.darts.database.loader;

import be.rommens.darts.database.domain.Player;
import be.rommens.darts.database.repository.PlayerRepository;
import be.rommens.darts.scraper.core.DartsOrakelScrapeResult;
import be.rommens.darts.scraper.core.OrderOfMeritScrapeResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlayersLoader {

    private final PlayerRepository playerRepository;

    public List<Player> loadPlayers() throws IOException {
        if (playerRepository.count() > 0) {
            throw new IllegalStateException("There are players in local");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        List<OrderOfMeritScrapeResult> orderOfMeritScrapeResults = objectMapper
                .readerForListOf(OrderOfMeritScrapeResult.class)
                .readValue(this.getClass().getClassLoader().getResource("scraper/order_of_merit.json"));
        List<DartsOrakelScrapeResult> dartsOrakelScrapeResults = objectMapper
                .readerForListOf(DartsOrakelScrapeResult.class)
                .readValue(this.getClass().getClassLoader().getResource("scraper/dartsorakel_player_stats.json"));

        log.info("Start loading players...");
        for (OrderOfMeritScrapeResult oomResult : orderOfMeritScrapeResults) {
            var player = dartsOrakelScrapeResults.stream()
                    .filter(d -> d.name().equals(oomResult.name()))
                    .findFirst()
                    .map(d -> Player.builder()
                            .name(oomResult.name())
                            .country(d.country())
                            .hasTourCard(oomResult.detail().hasTourCard())
                            .average(d.average())
                            .first9Average(d.first9avg())
                            .accuracyStartingDouble(d.startingDouble())
                            .accuracyBull(d.accuracyBull())
                            .accuracyDouble(d.accuracyDouble())
                            .accuracyDouble3rd(d.accuracyDouble3rdDart())
                            .accuracyTreble(d.accuracyTreble())
                            .build())
                    .orElseThrow();
            playerRepository.save(player);
        }
        log.info("Finished loading players...");
        return playerRepository.getAll();
    }
}
