package be.rommens.darts.simulator.strategy;

import be.rommens.darts.simulator.model.Dart;
import be.rommens.darts.simulator.model.Player;
import be.rommens.darts.simulator.model.Throw;
import be.rommens.darts.simulator.model.Turn;
import java.util.concurrent.ThreadLocalRandom;

//TODO : https://github.com/Jacob-Lang/Monte-Carlo-Simulation---Darts/blob/master/darts.py
//TODO : https://www.researchgate.net/figure/Specification-of-points-on-dartboard_fig2_325194809
//TODO : https://github.com/jfree/jfreechart
//TODO : https://www.javatpoint.com/jfreechart-scatter-chart
public interface ThrowSimulationStrategy {

    Throw simulateThrow(Turn turn, Dart dart, int scoreToAim, Player player, boolean isFinishingShot);

    default int generateRandom() {
        return ThreadLocalRandom.current().nextInt(100);
    }
}
