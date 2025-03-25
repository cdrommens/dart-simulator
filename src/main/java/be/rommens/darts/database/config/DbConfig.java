package be.rommens.darts.database.config;

import be.rommens.darts.database.domain.Player;
import be.rommens.darts.database.domain.Tournament;
import be.rommens.darts.database.domain.TournamentQualifiedPlayer;
import be.rommens.darts.database.domain.TournamentResult;
import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.relational.core.mapping.event.BeforeConvertCallback;

@Configuration
public class DbConfig {

    @Bean
    BeforeConvertCallback<Player> beforeConvertCallbackPlayer() {
        return (player) -> {
          if (player.getId() == null) {
              player.setId(UUID.randomUUID().toString());
          }
          return player;
        };
    }

    @Bean
    BeforeConvertCallback<Tournament> beforeConvertCallbackTournament() {
        return (tournament) -> {
            if (tournament.getId() == null) {
                tournament.setId(tournament.getKey() + tournament.getYear());
            }
            return tournament;
        };
    }

    @Bean
    BeforeConvertCallback<TournamentQualifiedPlayer> beforeConvertCallbackTournamentQualifiedPlayer() {
        return (tournamentQualifiedPlayer) -> {
            if (tournamentQualifiedPlayer.getId() == null) {
                tournamentQualifiedPlayer.setId(UUID.randomUUID().toString());
            }
            return tournamentQualifiedPlayer;
        };
    }

    @Bean
    BeforeConvertCallback<TournamentResult> beforeConvertCallbackTournamentResult() {
        return (tournamentResult) -> {
            if (tournamentResult.getId() == null) {
                tournamentResult.setId(UUID.randomUUID().toString());
            }
            return tournamentResult;
        };
    }

}
