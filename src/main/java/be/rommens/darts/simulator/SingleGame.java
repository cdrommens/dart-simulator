package be.rommens.darts.simulator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SingleGame {

    private final ThrowDecision decide;

    private final List<Turn> turns = new ArrayList<>();
    private boolean finished = false;

    public SingleGame(ThrowDecision decide) {
        this.decide = decide;
    }

    public void playGame(Player player) {
        reset();
        while (!finished) {
            Turn turn = new Turn(initializeScore());
            System.out.println(String.format("[DEBUG] : %s", turn));
            for (Dart dart : Dart.values()) {
                int scoreCurrPlayer = turn.getScoreLeft();

                Throw scoreAchieved = decide.actualTargetHit(dart, scoreCurrPlayer, player);  //returns score hit
                turn.addThrow(scoreAchieved);

                System.out.println(String.format("[DEBUG] : %s", turn));
                System.out.println(String.format("%s | %s : %s - %s (%s)", dart.ordinal()+1, player.name(), scoreCurrPlayer, scoreAchieved.score(), scoreAchieved.isFinishingShot()));

                if ((scoreCurrPlayer - scoreAchieved.score() == 1 || scoreCurrPlayer - scoreAchieved.score() < 0) || (
                        scoreCurrPlayer - scoreAchieved.score() == 0 && !scoreAchieved.isFinishingThrow())) {
                    switch (dart){
                        case FIRST -> {
                            turn.addThrow(new Throw(0, scoreAchieved.isFinishingThrow(), scoreAchieved.isFinishingShot(), false));
                            turn.addThrow(new Throw(0, scoreAchieved.isFinishingThrow(), scoreAchieved.isFinishingShot(), false));
                        }
                        case SECOND -> turn.addThrow(new Throw(0, scoreAchieved.isFinishingThrow(), scoreAchieved.isFinishingShot(), false));
                    }
                    turn.markAsBusted();
                    break;
                } else if (scoreCurrPlayer - scoreAchieved.score() == 0 && scoreAchieved.isFinishingThrow()) {
                    System.out.println(String.format("%s WON", player.name()));
                    finished = true;
                    break;
                }
            }
            turns.add(turn);
        }
    }

    //TODO : Treble less visits: Het aantal beurten dat gegooid is met een score onder de 60. Dus zonder een triple 20, 19, 18 of 17. Wanneer men onder de 200 punten komt en richting het wegzetten gaat, worden deze ‘visits’ niet meer geteld.
    public void calculateStatistics() {
        System.out.println("Number of darts thrown : " + turns.stream().flatMap(Turn::getThrows).toList().size());
        System.out.println("Average : " + 501 / turns.stream().flatMap(Turn::getThrows).toList().size() * 3);
        System.out.println("First 9 Average : " +
                turns.stream()
                        .flatMap(Turn::getThrows)
                        .limit(9)
                        .map(Throw::score)
                        .reduce(0, Integer::sum)/ 9 * 3);
        System.out.println("Checkout% : " +
                (1.0/ turns.stream()
                        .flatMap(Turn::getThrows)
                        .filter(Throw::isFinishingShot)
                        .toList().size()) * 100);
        System.out.println("180's : " +
                turns.stream()
                        .filter(Turn::is180)
                        .count());
    }

    private int initializeScore() {
        return turns.stream()
                .map(Turn::getScoreLeft)
                .filter(score -> score >= 0)
                .min(Comparator.naturalOrder())
                .orElse(501);
    }

    private void reset() {
        turns.clear();
        finished = false;
    }
}
