package be.rommens.darts.simulator;

import be.rommens.darts.simulator.model.Dart;
import be.rommens.darts.simulator.model.Player;
import be.rommens.darts.simulator.model.Throw;
import be.rommens.darts.simulator.model.Turn;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class legSimulator {

    private static final Logger log = LoggerFactory.getLogger(legSimulator.class);

    private final ThrowSimulator simulator;

    public legSimulator(ThrowSimulator simulator) {
        this.simulator = simulator;
    }

    public List<Turn> playGame(Player player) {
        List<Turn> turns = new ArrayList<>();
        boolean finished = false;
        while (!finished) {
            Turn turn = new Turn(initializeScore(turns));
            log.debug(turn.toString());

            for (Dart dart : Dart.values()) {
                int scoreCurrPlayer = turn.getScoreLeft();

                Throw simulatedThrow = simulator.throwDart(dart, scoreCurrPlayer, player);  //returns score hit
                turn.addThrow(simulatedThrow);

                log.debug(turn.toString());
                log.debug("{} | {} : {} - {} {}", dart.ordinal()+1, player.name(), scoreCurrPlayer, simulatedThrow.score(), simulatedThrow.isFinishingShot());

                int remainingScore = scoreCurrPlayer - simulatedThrow.score();
                if ((remainingScore == 1 || remainingScore < 0) || (remainingScore == 0 && !simulatedThrow.isFinishingThrow())) {
                    fillBustedThrows(dart, turn, simulatedThrow);
                    turn.markAsBusted();
                    break;
                }
                if (remainingScore == 0) {
                    finished = true;
                    break;
                }
            }
            turns.add(turn);
        }
        return turns;
    }

    private int initializeScore(List<Turn> turns) {
        return turns.stream()
                .map(Turn::getScoreLeft)
                .filter(score -> score >= 0)
                .min(Comparator.naturalOrder())
                .orElse(501);
    }

    private void fillBustedThrows(Dart dart, Turn turn, Throw simulatedThrow) {
        switch (dart){
            case FIRST -> {
                turn.addThrow(new Throw(0, simulatedThrow.isFinishingThrow(), simulatedThrow.isFinishingShot(), false));
                turn.addThrow(new Throw(0, simulatedThrow.isFinishingThrow(), simulatedThrow.isFinishingShot(), false));
            }
            case SECOND -> turn.addThrow(new Throw(0, simulatedThrow.isFinishingThrow(), simulatedThrow.isFinishingShot(), false));
        }
    }
}
