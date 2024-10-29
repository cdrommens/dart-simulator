package be.rommens.darts.simulator.strategy.orginal;

import be.rommens.darts.simulator.model.Dart;
import be.rommens.darts.simulator.model.Player;
import be.rommens.darts.simulator.model.Throw;
import be.rommens.darts.simulator.model.Turn;
import be.rommens.darts.simulator.strategy.AimType;
import be.rommens.darts.simulator.strategy.ThrowSimulationStrategy;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("original")
@Component(AimType.SINGLE)
public class SingleThrowSimulationStrategy implements ThrowSimulationStrategy {

    @Override
    public Throw simulateThrow(Turn turn, Dart dart, int scoreToAim, Player player, boolean isFinishingShot) {
        //  return result of throwing for single d with accuracy p% (or q% for the outer)
        int r = generateRandom();
        int accuracyPercentage = player.accuracySingle();
        int accuracyOuterPercentage = player.accuracyOuter();

        if (scoreToAim == 25) {
            // outer with q% accuracy
            if (r < accuracyOuterPercentage) {
                return new Throw(null, 25, false, false, false, false);
            }
            else if (r < accuracyOuterPercentage + (100 - accuracyOuterPercentage)*0.5) {
                return new Throw(null, 50, true, false, false, false);
            }
            else {
                return new Throw(null, 1 + ThreadLocalRandom.current().nextInt(20), false, false, false, false);
            }
        }
        else {
            // 1 to 20 single with accuracy p%
            if (r < accuracyPercentage) {
                return new Throw(null, scoreToAim, false, false, false, false);
            }
            else if (r < accuracyPercentage + 1 * (100 - accuracyPercentage)*0.3) {
                return new Throw(null, DART_BOARD[0][scoreToAim], false, false, false, false);
            }
            else if (r < accuracyPercentage + 2 * (100 - accuracyPercentage)*0.3) {
                return new Throw(null, DART_BOARD[1][scoreToAim], false, false, false, false);
            }
            else if (r < accuracyPercentage + 2 * (100 - accuracyPercentage)*0.3 + (100 - accuracyPercentage)*0.2) {
                return new Throw(null, 3 * scoreToAim, false, false, false, false);
            }
            else {
                return new Throw(null, 2 * scoreToAim, true, false, false, false);
            }
        }
    }
}
