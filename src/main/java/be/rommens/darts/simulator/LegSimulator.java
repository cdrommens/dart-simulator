package be.rommens.darts.simulator;

import be.rommens.darts.simulator.model.Bull;
import be.rommens.darts.simulator.model.Dart;
import be.rommens.darts.simulator.model.Leg;
import be.rommens.darts.simulator.model.Player;
import be.rommens.darts.simulator.model.Statistics;
import be.rommens.darts.simulator.model.Throw;
import be.rommens.darts.simulator.model.Turn;
import be.rommens.darts.simulator.strategy.AimType;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LegSimulator {

    private static final Logger log = LoggerFactory.getLogger(LegSimulator.class);

    private final ThrowSimulator simulator;

    public LegSimulator(ThrowSimulator simulator) {
        this.simulator = simulator;
    }

    // Explicit use of the first 9 average and not the general average so that double misses can happen
    public Leg playLeg(Player player) {
        List<Turn> turns;
        Statistics statistics;
        do {
            turns = simulateLeg(player);
            statistics = Statistics.calculate(turns);
        } while (statistics.getFirst9Average() < player.getMinimumAverageCurrentLevel());
        return new Leg(player, turns);
    }

    public Bull throwBull(Player player) {
        Throw bullThrow = simulator.throwDart(null, Dart.FIRST, 50, player);
        return switch (bullThrow.score()) {
            case 25 -> Bull.SINGLE_HIT;
            case 50 -> Bull.DOUBLE_HIT;
            default -> Bull.NO_HIT;
        };
    }

    private List<Turn> simulateLeg(Player player) {
        List<Turn> turns = new ArrayList<>();
        boolean finished = false;
        while (!finished) {
            Turn turn = new Turn(initializeScore(turns));
            log.debug("New turn : {}", turn);
            turn.throwDarts(player, simulator);
            if (turn.getScoreLeft() == 0) {
                finished = true;
                log.debug("finished");
            }
            turns.add(turn);
        }
        return turns;
    }

    private int initializeScore(List<Turn> turns) {
        return turns.stream()
                .filter(turn -> !turn.isBusted())
                .map(Turn::getScoreLeft)
                .filter(score -> score >= 0)
                .min(Comparator.naturalOrder())
                .orElse(501);
    }
}
