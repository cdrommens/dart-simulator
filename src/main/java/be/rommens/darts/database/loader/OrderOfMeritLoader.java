package be.rommens.darts.database.loader;

import be.rommens.darts.database.domain.OomType;
import be.rommens.darts.database.domain.OrderOfMerit;
import be.rommens.darts.database.domain.Player;
import be.rommens.darts.database.domain.TournamentResult;
import be.rommens.darts.database.repository.OrderOfMeritRepository;
import be.rommens.darts.database.repository.TournamentResultRepository;
import be.rommens.darts.services.OrderOfMeritCalculator;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderOfMeritLoader {

    private final TournamentResultRepository tournamentResultRepository;
    private final OrderOfMeritRepository orderOfMeritRepository;

    private final OrderOfMeritCalculator orderOfMeritCalculator;

    public void loadMainOrderOfMerit(List<Player> players) {
        List<TournamentResult> results = tournamentResultRepository.getAllOrderedByDate();

        log.info("Loading Main Order of Merit...");
        var oom = orderOfMeritCalculator.calculateMainOrderOfMerit(LocalDate.of(2025, 1, 5), results, players);

        for (int rank = 0; rank < oom.size(); rank++) {
            var pair = oom.get(rank);
            var orderOfMerit = OrderOfMerit.builder()
                    .oomType(OomType.OOM)
                    .rank(rank + 1)
                    .previousRank(rank + 1)
                    .playerId(UUID.fromString(pair.getLeft().getId()))
                    .playerName(pair.getLeft().getName())
                    .money(pair.getRight())
                    .build();
            orderOfMeritRepository.save(orderOfMerit);
        }
        log.info("Finished loading Main Order of Merit...");
    }

    public void loadProTourOrderOfMerit(List<Player> players) {
        List<TournamentResult> results = tournamentResultRepository.getAllOrderedByDate();

        log.info("Loading ProTour Order of Merit...");
        var oom = orderOfMeritCalculator.calculateProTourOrderOfMerit(LocalDate.of(2025, 1, 5), results, players);

        for (int rank = 0; rank < oom.size(); rank++) {
            var pair = oom.get(rank);
            var orderOfMerit = OrderOfMerit.builder()
                    .oomType(OomType.PRO)
                    .rank(rank + 1)
                    .previousRank(rank + 1)
                    .playerId(UUID.fromString(pair.getLeft().getId()))
                    .playerName(pair.getLeft().getName())
                    .money(pair.getRight())
                    .build();
            orderOfMeritRepository.save(orderOfMerit);
        }
        log.info("Finished loading ProTour Order of Merit...");
    }

}
