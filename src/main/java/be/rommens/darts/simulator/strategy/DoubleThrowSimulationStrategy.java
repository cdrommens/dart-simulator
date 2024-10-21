package be.rommens.darts.simulator.strategy;

import be.rommens.darts.simulator.model.Player;
import be.rommens.darts.simulator.model.Throw;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;

@Component(AimType.DOUBLE)
public class DoubleThrowSimulationStrategy implements ThrowSimulationStrategy {

    @Override
    public Throw simulateThrow(int scoreToAim, Player player, boolean isFinishingShot) {
        //  return result of throwing for double d with accuracy p%
        int r = ThreadLocalRandom.current().nextInt(100);
        int accuracyPercentage = player.accuracyDouble();
        System.out.printf("(%s - %s) ", r, accuracyPercentage);

        if (r < accuracyPercentage) {
            return new Throw(2 * scoreToAim, true, isFinishingShot, false);
        }
        else if (r < accuracyPercentage + 1 * (100 - accuracyPercentage)*0.3) {
            //return single value and zero (complete 'miss' options) with 30% of the probability not to hit as intended
            return new Throw(0, false, isFinishingShot, false);
        }
        else if (r < accuracyPercentage + 2 * (100 - accuracyPercentage)*0.3) {
            return new Throw(scoreToAim, false, isFinishingShot, false);
        }
        else if (r < accuracyPercentage + 2 * (100 - accuracyPercentage)*0.3 + 2 * (100 - accuracyPercentage)*0.15) {
            //return each neighbouring double with 15% of the probability not to hit as intended
            return new Throw(2 * DART_BOARD[0][scoreToAim], true, isFinishingShot, false);
        }
        else if (r < accuracyPercentage + 2 * (100 - accuracyPercentage)*0.3 + 3 * (100 - accuracyPercentage)*0.15) {
            return new Throw(2 * DART_BOARD[1][scoreToAim], true, isFinishingShot, false);
        }
        else if (r < accuracyPercentage + 2 * (100 - accuracyPercentage)*0.3 + 4 * (100 - accuracyPercentage)*0.05) {
            //return each neighbouring single with 5% of the probability not to hit as intended
            return new Throw(DART_BOARD[0][scoreToAim], false, isFinishingShot, false);
        }
        else {
            return new Throw(DART_BOARD[1][scoreToAim], false, isFinishingShot, false);
        }
    }
}
