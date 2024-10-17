package be.rommens.darts.simulator;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SingleGame {

    private final Board board;
    private final ThrowDecision decide;

    private Score score = new Score();

    private List<Turn> turns = new ArrayList<>();

    public SingleGame(Board board, ThrowDecision decide) {
        this.board = board;
        this.decide = decide;
    }

    public void playGame(Player player) {
        while (score.getCurrentGameScore() > 0) {
            int scoreBeforeThreeThrows = score.getCurrentGameScore();
            Turn turn = new Turn();
            for (Dart dart : Dart.values()) {
                int scoreCurrPlayer = score.getCurrentGameScore();

                SingleScore scoreAchieved = decide.actualTargetHit(dart, scoreCurrPlayer, board, player);  //returns score hit
                turn.addSingleScore(scoreAchieved);

                System.out.println(String.format("%s | %s : %s - %s (%s)", dart.ordinal()+1, player.name(), scoreCurrPlayer, scoreAchieved.score(), scoreAchieved.isFinishingShot()));

                //If score outcome large than 2 or equal to 2, update by subtraction
                if (scoreCurrPlayer - scoreAchieved.score() >= 2) {
                    score.setCurrentGameScore(scoreCurrPlayer - scoreAchieved.score());
                }
                //if invalid, reset to previus and...
                else if ((scoreCurrPlayer - scoreAchieved.score() == 1 || scoreCurrPlayer - scoreAchieved.score() < 0) || (
                        scoreCurrPlayer - scoreAchieved.score() == 0 && !scoreAchieved.isFinishingThrow())) {
                    score.setCurrentGameScore(scoreBeforeThreeThrows);
                    switch (dart){
                        case FIRST -> {
                            turn.addSingleScore(new SingleScore(0, scoreAchieved.isFinishingThrow(), scoreAchieved.isFinishingShot()));
                            turn.addSingleScore(new SingleScore(0, scoreAchieved.isFinishingThrow(), scoreAchieved.isFinishingShot()));
                        }
                        case SECOND -> turn.addSingleScore(new SingleScore(0, scoreAchieved.isFinishingThrow(), scoreAchieved.isFinishingShot()));
                    }
                    break;
                } else if (scoreCurrPlayer - scoreAchieved.score() == 0 && scoreAchieved.isFinishingThrow()) {
                    System.out.println(String.format("%s WON", player.name()));
                    score.setCurrentGameScore(0); //set current player's score to zero
                    break;
                } else {
                    throw new IllegalStateException("error scoring");
                }
            }
            turns.add(turn);
        }
    }

    //TODO : Treble less visits: Het aantal beurten dat gegooid is met een score onder de 60. Dus zonder een triple 20, 19, 18 of 17. Wanneer men onder de 200 punten komt en richting het wegzetten gaat, worden deze ‘visits’ niet meer geteld.
    public void calculateStatistics() {
        System.out.println("Number of darts thrown : " + turns.stream().flatMap(Turn::getScores).toList().size());
        System.out.println("Average : " + 501 / turns.stream().flatMap(Turn::getScores).toList().size() * 3);
        System.out.println("First 9 Average : " +
                turns.stream()
                        .flatMap(Turn::getScores)
                        .limit(9)
                        .map(SingleScore::score)
                        .reduce(0, Integer::sum)/ 9 * 3);
        System.out.println("Checkout% : " +
                (1.0/ turns.stream()
                        .flatMap(Turn::getScores)
                        .filter(SingleScore::isFinishingShot)
                        .toList().size()) * 100);
    }

}
