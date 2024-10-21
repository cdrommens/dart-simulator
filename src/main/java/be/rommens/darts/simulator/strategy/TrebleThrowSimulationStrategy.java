package be.rommens.darts.simulator.strategy;

import be.rommens.darts.simulator.model.Player;
import be.rommens.darts.simulator.model.Throw;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;

@Component(AimType.TREBLE)
public class TrebleThrowSimulationStrategy implements ThrowSimulationStrategy {

    /**
     *  Aim for treble where X is the accuracy:
     *  - 0 -> X : hit treble
     *  - Rest : treble missed:
     *     - 80% lands in the single of the same number
     *     - 15% lands in a neighboring treble
     *     - 5% is a loose dart
     */

    @Override
    public Throw simulateThrow(int scoreToAim, Player player, boolean isFinishingShot) {
        // return result of throwing for treble d with accuracy p% ; iV stands for "is this valid as a finishing throw?".
        // This is unchanged and hence would not have been needed
        int r = ThreadLocalRandom.current().nextInt(100);
        int accuracyPercentage = player.accuracyTreble();
        //System.out.printf("(%s - %s) ", r, accuracyPercentage);

        if (r < accuracyPercentage) {
            return new Throw(3 * scoreToAim, false, false, false);
        }
        else if (r < accuracyPercentage + (100 - accuracyPercentage)*0.8) {
            // if treble is missed, in 80% of the time, a single is hit
            return new Throw(scoreToAim, false, false, false);
        }
        else if (r < accuracyPercentage + (100 - accuracyPercentage)*0.875) {
            // if treble is missed, 7,5% of the time, a neighboring treble is hit
            return new Throw(3 * DART_BOARD[0][scoreToAim], false, false, false);
        }
        else if (r < accuracyPercentage + (100 - accuracyPercentage)*0.95) {
            return new Throw(3 * DART_BOARD[1][scoreToAim], false, false, false);
        }
        else if (r < accuracyPercentage + (100 - accuracyPercentage)*0.975) {
            // rarely a loose dart
            return new Throw(DART_BOARD[0][scoreToAim], false, false, false);
        }
        else {
            return new Throw(DART_BOARD[1][scoreToAim], false, false, false);
        }
    }
}
