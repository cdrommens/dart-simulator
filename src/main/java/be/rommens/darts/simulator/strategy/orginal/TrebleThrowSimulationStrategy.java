package be.rommens.darts.simulator.strategy.orginal;

import be.rommens.darts.simulator.model.Dart;
import be.rommens.darts.simulator.model.Player;
import be.rommens.darts.simulator.model.Throw;
import be.rommens.darts.simulator.model.Turn;
import be.rommens.darts.simulator.strategy.AimType;
import be.rommens.darts.simulator.strategy.ThrowSimulationStrategy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("original")
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
    public Throw simulateThrow(Turn turn, Dart dart, int scoreToAim, Player player, boolean isFinishingShot) {
        // return result of throwing for treble d with accuracy p% ; iV stands for "is this valid as a finishing throw?".
        // This is unchanged and hence would not have been needed
        int r = generateRandom();
        double accuracyPercentage = player.accuracyTreble();
        //TODO : based on the average, the accuracy increases based on the result of the previous darts if in the same field
        //System.out.printf("(%s - %s) ", r, accuracyPercentage);

        if (r < accuracyPercentage) {
            return new Throw(null, 3 * scoreToAim, false, false, false, false);
        }
        else if (r < accuracyPercentage + (100 - accuracyPercentage)*0.8) {
            // if treble is missed, in 80% of the time, a single is hit
            return new Throw(null, scoreToAim, false, false, false, false);
        }
        else if (r < accuracyPercentage + (100 - accuracyPercentage)*0.875) {
            // if treble is missed, 7,5% of the time, a neighboring treble is hit
            return new Throw(null, 3 * DART_BOARD[0][scoreToAim], false, false, false, false);
        }
        else if (r < accuracyPercentage + (100 - accuracyPercentage)*0.95) {
            return new Throw(null, 3 * DART_BOARD[1][scoreToAim], false, false, false, false);
        }
        else if (r < accuracyPercentage + (100 - accuracyPercentage)*0.975) {
            // rarely a loose dart
            return new Throw(null, DART_BOARD[0][scoreToAim], false, false, false, false);
        }
        else {
            return new Throw(null, DART_BOARD[1][scoreToAim], false, false, false, false);
        }
    }
}
