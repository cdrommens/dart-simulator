package be.rommens.darts.database.loader;

import be.rommens.darts.database.domain.Player;
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


    @EventListener
    @SneakyThrows
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (event.getApplicationContext().getEnvironment().acceptsProfiles(Profiles.of("local"))) {
            List<Player> players = playersLoader.loadPlayers();
            tournamentLoader.loadTournamentQualifiedPlayers(players);
        }
    }

}
