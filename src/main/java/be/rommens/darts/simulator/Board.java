package be.rommens.darts.simulator;

import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;

//TODO : upgrade to 21 and refactor with switch expressions : case int rand when r < accuracyPercentage -> new SingleScore(...);
//TODO : introduce levels : lower the aimed hit rate with a certain % AND play with the rest of probability (0.2 etc)
//TODO : introduce hit % for doubles on 3, 2 and 1 dart checkout
@Component
public class Board {

    private static final int[][] DART_BOARD = {
            {0, 20, 15, 17, 18, 12, 13, 19, 16, 14, 6, 8, 9, 4, 11, 10, 7, 2, 1, 3, 5}, //numbers on dartboard anticlockwise
            {0, 18, 17, 19, 13, 20, 10, 16, 11, 12, 15, 14, 5, 6, 9, 2, 8, 3, 4, 7, 1}  //numbers on dartboard clockwise}
    };

    public SingleScore throwBull(int accuracyPercentage, boolean isFinishingShot) {
        //Throw for the bull with accuracy p%; iV stands for "is this valid as a finishing throw?"

        //C++ : rand() % 100 => 0 - 99
        //C++ : rand() % 100 + 1  => 1 - 100
        int r = ThreadLocalRandom.current().nextInt(100);  //should be 1000
        System.out.printf("(%s - %s) ", r, accuracyPercentage);
        // formerly: hit bull with p=accurancy-20% // I changed this because it made no sense to me that, if the accuracy is p%,
        // the probability of hitting accurately should be p%-20%
        if (r < accuracyPercentage) {
            //isFinishingThrow stands for "is this valid as a finishing throw?" (yes, it is)
            return new SingleScore(50, true, isFinishingShot);
        }
        else if (r < accuracyPercentage + (100 - accuracyPercentage)*0.6) {
            // hit 25 with accuracy a 60% given that not to hit as intended
            return new SingleScore(25, false, isFinishingShot);
        }
        else {
            //remaining 40% (given that not to hit as intended) return any other single number with the same probability
            return new SingleScore(1 + ThreadLocalRandom.current().nextInt(20), false, isFinishingShot);
        }
    }

    public SingleScore throwTreble(int treble, int accuracyPercentage) {
        // return result of throwing for treble d with accuracy p% ; iV stands for "is this valid as a finishing throw?".
        // This is unchanged and hence would not have been needed
        int r = ThreadLocalRandom.current().nextInt(100);
        System.out.printf("(%s - %s) ", r, accuracyPercentage);

        if (r < accuracyPercentage) {
            return new SingleScore(3 * treble, false, false);
        }
        else if (r < accuracyPercentage + (100 - accuracyPercentage)*0.2) {
            //return single value (and other 'miss' options) with a fifth of the probability not to hit as intended etc.
            return new SingleScore(treble, false, false);
        }
        else if (r < accuracyPercentage + 2 * (100 - accuracyPercentage)*0.2) {
            return new SingleScore(3 * DART_BOARD[0][treble], false, false);
        }
        else if (r < accuracyPercentage + 3 * (100 - accuracyPercentage)*0.2) {
            return new SingleScore(3 * DART_BOARD[1][treble], false, false);
        }
        else if (r < accuracyPercentage + 4 * (100 - accuracyPercentage)*0.2) {
            return new SingleScore(DART_BOARD[0][treble], false, false);
        }
        else {
            return new SingleScore(DART_BOARD[1][treble], false, false);
        }
    }

    public SingleScore throwDouble(int double_, int accuracyPercentage, boolean isFinishingShot) {
        //  return result of throwing for double d with accuracy p%
        int r = ThreadLocalRandom.current().nextInt(100);
        System.out.printf("(%s - %s) ", r, accuracyPercentage);

        if (r < accuracyPercentage) {
            return new SingleScore(2 * double_, true, isFinishingShot);
        }
        else if (r < accuracyPercentage + 1 * (100 - accuracyPercentage)*0.3) {
            //return single value and zero (complete 'miss' options) with 30% of the probability not to hit as intended
            return new SingleScore(0, false, isFinishingShot);
        }
        else if (r < accuracyPercentage + 2 * (100 - accuracyPercentage)*0.3) {
            return new SingleScore(double_, false, isFinishingShot);
        }
        else if (r < accuracyPercentage + 2 * (100 - accuracyPercentage)*0.3 + 2 * (100 - accuracyPercentage)*0.15) {
            //return each neighbouring double with 15% of the probability not to hit as intended
            return new SingleScore(2 * DART_BOARD[0][double_], true, isFinishingShot);
        }
        else if (r < accuracyPercentage + 2 * (100 - accuracyPercentage)*0.3 + 3 * (100 - accuracyPercentage)*0.15) {
            return new SingleScore(2 * DART_BOARD[1][double_], true, isFinishingShot);
        }
        else if (r < accuracyPercentage + 2 * (100 - accuracyPercentage)*0.3 + 4 * (100 - accuracyPercentage)*0.05) {
            //return each neighbouring single with 5% of the probability not to hit as intended
            return new SingleScore(DART_BOARD[0][double_], false, isFinishingShot);
        }
        else {
            return new SingleScore(DART_BOARD[1][double_], false, isFinishingShot);
        }
    }

    public SingleScore throwSingle(int single, int accuracyPercentage, int accuracyOuterPercentage) {
        //  return result of throwing for single d with accuracy p% (or q% for the outer)
        int r = ThreadLocalRandom.current().nextInt(100);
        System.out.printf("(%s - %s) ", r, accuracyPercentage);

        if (single == 25) {
            // outer with q% accuracy
            if (r < accuracyOuterPercentage) {
                return new SingleScore(25, false, false);
            }
            else if (r < accuracyOuterPercentage + (100 - accuracyOuterPercentage)*0.5) {
                return new SingleScore(50, true, false);
            }
            else {
                return new SingleScore(1 + ThreadLocalRandom.current().nextInt(20), false, false);
            }
        }
        else {
            // 1 to 20 single with accuracy p%
            if (r < accuracyPercentage) {
                return new SingleScore(single, false, false);
            }
            else if (r < accuracyPercentage + 1 * (100 - accuracyPercentage)*0.3) {
                return new SingleScore(DART_BOARD[0][single], false, false);
            }
            else if (r < accuracyPercentage + 2 * (100 - accuracyPercentage)*0.3) {
                return new SingleScore(DART_BOARD[1][single], false, false);
            }
            else if (r < accuracyPercentage + 2 * (100 - accuracyPercentage)*0.3 + (100 - accuracyPercentage)*0.2) {
                return new SingleScore(3 * single, false, false);
            }
            else {
                return new SingleScore(2 * single, true, false);
            }
        }
    }
}
