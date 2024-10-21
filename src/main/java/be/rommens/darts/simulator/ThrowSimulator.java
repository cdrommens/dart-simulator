package be.rommens.darts.simulator;

import be.rommens.darts.simulator.model.Player;
import be.rommens.darts.simulator.model.Throw;
import be.rommens.darts.simulator.strategy.ThrowSimulationStrategy;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ThrowSimulator {

    private final Map<String, ThrowSimulationStrategy> throwSimulationStrategies;

    public ThrowSimulator(Map<String, ThrowSimulationStrategy> throwSimulationStrategies) {
        this.throwSimulationStrategies = throwSimulationStrategies;
    }

    public Throw simulateThrow(String aimType, int scoreToAim, Player player, boolean isFinishingShot) {
        if (throwSimulationStrategies.containsKey(aimType.toString())) {
            return throwSimulationStrategies.get(aimType).simulateThrow(scoreToAim, player, isFinishingShot);
        }
        throw new IllegalArgumentException("Unknown aim type: " + aimType);
    }
}
