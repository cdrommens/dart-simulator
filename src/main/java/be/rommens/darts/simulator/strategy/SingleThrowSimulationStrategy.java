package be.rommens.darts.simulator.strategy;

import be.rommens.darts.simulator.model.Player;
import be.rommens.darts.simulator.model.Throw;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;

@Component(AimType.SINGLE)
public class SingleThrowSimulationStrategy implements ThrowSimulationStrategy {

    @Override
    public Throw simulateThrow(int scoreToAim, Player player, boolean isFinishingShot) {
        //  return result of throwing for single d with accuracy p% (or q% for the outer)
        int r = ThreadLocalRandom.current().nextInt(100);
        int accuracyPercentage = player.accuracySingle();
        int accuracyOuterPercentage = player.accuracyOuter();

        if (scoreToAim == 25) {
            // outer with q% accuracy
            if (r < accuracyOuterPercentage) {
                return new Throw(25, false, false, false);
            }
            else if (r < accuracyOuterPercentage + (100 - accuracyOuterPercentage)*0.5) {
                return new Throw(50, true, false, false);
            }
            else {
                return new Throw(1 + ThreadLocalRandom.current().nextInt(20), false, false, false);
            }
        }
        else {
            // 1 to 20 single with accuracy p%
            if (r < accuracyPercentage) {
                return new Throw(scoreToAim, false, false, false);
            }
            else if (r < accuracyPercentage + 1 * (100 - accuracyPercentage)*0.3) {
                return new Throw(DART_BOARD[0][scoreToAim], false, false, false);
            }
            else if (r < accuracyPercentage + 2 * (100 - accuracyPercentage)*0.3) {
                return new Throw(DART_BOARD[1][scoreToAim], false, false, false);
            }
            else if (r < accuracyPercentage + 2 * (100 - accuracyPercentage)*0.3 + (100 - accuracyPercentage)*0.2) {
                return new Throw(3 * scoreToAim, false, false, false);
            }
            else {
                return new Throw(2 * scoreToAim, true, false, false);
            }
        }
    }
}
