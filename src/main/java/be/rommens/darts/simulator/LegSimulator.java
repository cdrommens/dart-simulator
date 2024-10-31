package be.rommens.darts.simulator;

import be.rommens.darts.simulator.model.Dart;
import be.rommens.darts.simulator.model.Player;
import be.rommens.darts.simulator.model.Statistics;
import be.rommens.darts.simulator.model.Throw;
import be.rommens.darts.simulator.model.Turn;
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
    public List<Turn> playLeg(Player player) {
        List<Turn> turns;
        Statistics statistics;
        do {
            turns = simulateLeg(player);
            statistics = Statistics.calculate(turns);
        } while (statistics.getFirst9Average() < getPlayerLevel(player));
        return turns;
    }

    public List<Turn> simulateLeg(Player player) {
        List<Turn> turns = new ArrayList<>();
        boolean finished = false;
        while (!finished) {
            Turn turn = new Turn(initializeScore(turns));
            log.debug("New turn : {}", turn);

            for (Dart dart : Dart.values()) {
                int scoreCurrPlayer = turn.getScoreLeft();

                Throw simulatedThrow = simulator.throwDart(turn, dart, scoreCurrPlayer, player);  //returns score hit
                turn.addThrow(dart, simulatedThrow);

                log.debug(turn.toString());
                log.debug("{} | {} : {} - {} {}", dart.ordinal()+1, player.name(), scoreCurrPlayer, simulatedThrow.score(), simulatedThrow.isFinishingShot());

                int remainingScore = scoreCurrPlayer - simulatedThrow.score();
                if ((remainingScore == 1 || remainingScore < 0) || (remainingScore == 0 && !simulatedThrow.isFinishingThrow())) {
                    turn.markAsBusted(dart);
                    log.debug("busted, so break");
                    break;
                }
                if (remainingScore == 0) {
                    finished = true;
                    log.debug("finished, so break");
                    break;
                }
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

    /*
    As an amateur player, my own average is some way below the level of the PDC tour.
    At the 2022 Viking Cup, I threw a 3-point average of 47.86 in my last 256 game,
    whilst my opponent George Willetts posted a much superior average of 68.32.
    An average of around 35-45 is considered a beginner average, ﻿46-55 ﻿a pub player average,
    56-70 a super league/county player average,
    71-85 a PDC Challenge Tour average,
    86-99 a PDC tour player average,
    and 100 and above a world-class average.
     */
    //TODO : upgrade java 23, rework to switch
    private static double getPlayerLevel(Player player) {
        if (player.first9avg() <= 45.00) {
            // 35-45 is considered a beginner average
            return 0.00;
        } else if (player.first9avg() <= 55.00) {
            // 46-55 pub player
            return 46.00;
        } else if (player.first9avg() <= 70.00) {
            // super league / county player
            return 56.00;
        } else if (player.first9avg() <= 85.00) {
            // PDC Challenge Tour
            return 71.00;
        } else if (player.first9avg() <= 99.00) {
            // PDC Tour
            return 86.00;
        } else {
            // world class
            return 87.00;
        }
    }
}
