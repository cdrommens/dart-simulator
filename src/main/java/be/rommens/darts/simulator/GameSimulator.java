package be.rommens.darts.simulator;

import be.rommens.darts.simulator.model.Leg;
import be.rommens.darts.simulator.model.Player;
import be.rommens.darts.simulator.model.Turn;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GameSimulator {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameSimulator.class);

    private final LegSimulator legSimulator;

    public GameSimulator(LegSimulator legSimulator) {
        this.legSimulator = legSimulator;
    }

    public void simulate(Player player1, Player player2, int bestOf) {
        // for the moment, let player1 always start
        Player startingPlayer = player1;
        Player otherPlayer = player2;
        List<LegResult> legResults = new ArrayList<>();
        int player1Wins = 0;
        int player2Wins = 0;
        boolean finished = false;
        do {
            LegResult result = simulate(startingPlayer, otherPlayer);
            legResults.add(result);
            if (result.winner.name().equals(player1.name())) {
                player1Wins++;
            } else {
                player2Wins++;
            }
            if (player1Wins == ((bestOf - 1)/2) || player2Wins == ((bestOf - 1))/2) {
                finished = true;
            }
            Player currentStartingPlayer = startingPlayer;
            startingPlayer = otherPlayer;
            otherPlayer = currentStartingPlayer;
        } while(!finished);

        LOGGER.info("{} {} - {} {}", player1.name(), player1Wins, player2Wins, player2.name());
    }

    public LegResult simulate(Player player1, Player player2) {
        //TODO introduce bull throw to start; for now always start player1

        Leg player1Results = legSimulator.playLeg(player1);
        Leg player2Results = legSimulator.playLeg(player2);
        int currentRound = 0;
        boolean finished = false;
        Player winner = null;
        LOGGER.info(" \t501\t|\t\t501");
        do {
            Turn player1Turn = player1Results.turns().get(currentRound);
            Turn player2Turn = player2Results.turns().get(currentRound);
            if (player1Turn.getScoreLeft() == 0) {
                finished = true;
                LOGGER.info("{}\t{}\t|\t{}\t{}", player1Turn.getScoreThrown(), player1Turn.getScoreLeft(), " ", " ");
                LOGGER.info("player 1 wins");
                player2Results = reduceLeg(currentRound, player2Results);
                winner = player1;
            } else if (player2Turn.getScoreLeft() == 0) {
                finished = true;
                LOGGER.info("{}\t{}\t|\t{}\t{}", " ", " ", player2Turn.getScoreThrown(), player2Turn.getScoreLeft());
                LOGGER.info("player 2 wins");
                player1Results = reduceLeg(currentRound, player1Results);
                winner = player2;
            } else {
                LOGGER.info("{}\t{}\t|\t{}\t{}", player1Turn.getScoreThrown(), player1Turn.getScoreLeft(), player2Turn.getScoreThrown(), player2Turn.getScoreLeft());
            }
            currentRound++;
        } while (!finished);
        player1Results.statistics().write();
        player2Results.statistics().write();
        return new LegResult(winner, player1Results, player2Results);
    }

    private Leg reduceLeg(int index, Leg leg) {
        return new Leg(leg.player(), leg.turns().subList(0, index));
    }

    record LegResult(Player winner, Leg player1Results, Leg player2Results) {}
}
