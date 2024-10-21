package be.rommens.darts.simulator.strategy;

import be.rommens.darts.simulator.model.Player;
import be.rommens.darts.simulator.model.Throw;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;

@Component(AimType.TREBLE)
public class TrebleThrowSimulationStrategy implements ThrowSimulationStrategy {

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
        else if (r < accuracyPercentage + (100 - accuracyPercentage)*0.2) {
            //return single value (and other 'miss' options) with a fifth of the probability not to hit as intended etc.
            return new Throw(scoreToAim, false, false, false);
        }
        else if (r < accuracyPercentage + 2 * (100 - accuracyPercentage)*0.2) {
            return new Throw(3 * DART_BOARD[0][scoreToAim], false, false, false);
        }
        else if (r < accuracyPercentage + 3 * (100 - accuracyPercentage)*0.2) {
            return new Throw(3 * DART_BOARD[1][scoreToAim], false, false, false);
        }
        else if (r < accuracyPercentage + 4 * (100 - accuracyPercentage)*0.2) {
            return new Throw(DART_BOARD[0][scoreToAim], false, false, false);
        }
        else {
            return new Throw(DART_BOARD[1][scoreToAim], false, false, false);
        }
    }
}
