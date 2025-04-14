package be.rommens.darts.database.loader;

import be.rommens.darts.database.domain.Player;
import be.rommens.darts.database.repository.TournamentResultRepository;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import java.io.FileWriter;
import java.io.Writer;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseLoader {

    private final PlayersLoader playersLoader;
    private final TournamentLoader tournamentLoader;
    private final OrderOfMeritLoader orderOfMeritLoader;

    private final TournamentResultRepository tournamentResultRepository;


    @EventListener
    @SneakyThrows
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (event.getApplicationContext().getEnvironment().acceptsProfiles(Profiles.of("local"))) {
            List<Player> players = playersLoader.loadPlayers();
            tournamentLoader.loadTournamentQualifiedPlayers(players);
            exportToCsv("players2.csv", players);
            exportToCsv("tournamentResults2.csv", tournamentResultRepository.getAllOrderedByDate());
            //orderOfMeritLoader.loadOrderOfMerit(players);
        }
    }

    @SneakyThrows
    public <T> void exportToCsv(String file, List<T> objects) {
        Writer writer = null;
        writer = new FileWriter(file);
        StatefulBeanToCsv<T> csv = new StatefulBeanToCsvBuilder<T>(writer)
                .withQuotechar('\'')
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .build();
        objects.forEach(o -> {
            try {
                csv.write(o);
            } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
                throw new RuntimeException(e);
            }
        });
        csv.write(objects);
        writer.close();
    }
}
