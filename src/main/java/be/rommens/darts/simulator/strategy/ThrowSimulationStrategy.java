package be.rommens.darts.simulator.strategy;

import be.rommens.darts.simulator.model.Dart;
import be.rommens.darts.simulator.model.Player;
import be.rommens.darts.simulator.model.Throw;
import java.util.concurrent.ThreadLocalRandom;

//TODO : https://github.com/Jacob-Lang/Monte-Carlo-Simulation---Darts/blob/master/darts.py
//TODO : https://www.researchgate.net/figure/Specification-of-points-on-dartboard_fig2_325194809
//TODO : https://github.com/jfree/jfreechart
//TODO : https://www.javatpoint.com/jfreechart-scatter-chart
public interface ThrowSimulationStrategy {

    int[][] DART_BOARD = {
            {0, 20, 15, 17, 18, 12, 13, 19, 16, 14, 6, 8, 9, 4, 11, 10, 7, 2, 1, 3, 5}, //numbers on dartboard anticlockwise
            {0, 18, 17, 19, 13, 20, 10, 16, 11, 12, 15, 14, 5, 6, 9, 2, 8, 3, 4, 7, 1}  //numbers on dartboard clockwise}
    };

    Throw simulateThrow(Dart dart, int scoreToAim, Player player, boolean isFinishingShot);

    default int generateRandom() {
        int r = ThreadLocalRandom.current().nextInt(100);
        return r;
    }
}
