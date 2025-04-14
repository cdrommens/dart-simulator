package be.rommens.darts.database.loader;

import be.rommens.darts.database.domain.Player;
import be.rommens.darts.database.domain.TournamentResult;
import be.rommens.darts.database.repository.OrderOfMeritRepository;
import be.rommens.darts.database.repository.TournamentResultRepository;
import be.rommens.darts.services.OrderOfMeritCalculator;
import java.time.LocalDate;
import java.util.List;
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

    public void loadOrderOfMerit(List<Player> players) {
        List<TournamentResult> results = tournamentResultRepository.getAllOrderedByDate();

        log.info("Loading Main Order of Merit...");
        orderOfMeritCalculator.calculateMainOrderOfMerit(LocalDate.of(2025, 1, 5), results, players);
    }

}
