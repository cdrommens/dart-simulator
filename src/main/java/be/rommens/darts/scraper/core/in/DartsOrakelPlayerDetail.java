package be.rommens.darts.scraper.core.in;

import com.fasterxml.jackson.annotation.JsonAlias;

public record DartsOrakelPlayerDetail(@JsonAlias("player_key") int playerKey, @JsonAlias("player_name") String playerName) {

}
