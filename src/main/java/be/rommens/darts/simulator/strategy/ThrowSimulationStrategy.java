package be.rommens.darts.simulator.strategy;

import be.rommens.darts.simulator.model.Player;
import be.rommens.darts.simulator.model.Throw;

//TODO : upgrade to 21 and refactor with switch expressions : case int rand when r < accuracyPercentage -> new SingleScore(...);
//TODO : introduce levels : lower the aimed hit rate with a certain % AND play with the rest of probability (0.2 etc)
//TODO : introduce hit % for doubles on 3, 2 and 1 dart checkout
public interface ThrowSimulationStrategy {

    int[][] DART_BOARD = {
            {0, 20, 15, 17, 18, 12, 13, 19, 16, 14, 6, 8, 9, 4, 11, 10, 7, 2, 1, 3, 5}, //numbers on dartboard anticlockwise
            {0, 18, 17, 19, 13, 20, 10, 16, 11, 12, 15, 14, 5, 6, 9, 2, 8, 3, 4, 7, 1}  //numbers on dartboard clockwise}
    };

    Throw simulateThrow(int scoreToAim, Player player, boolean isFinishingShot);
}
