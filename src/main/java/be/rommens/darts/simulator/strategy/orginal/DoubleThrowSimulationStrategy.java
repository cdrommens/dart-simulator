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
@Component(AimType.DOUBLE)
public class DoubleThrowSimulationStrategy implements ThrowSimulationStrategy {

    /**
     * Aim for double, where X is accuracy
     * - 0 -> X : double hit
     * - Rest:
     *   - 70% is out of the board
     *   - 20% is single hit
     *   - 6% is a neighbouring double
     *   - 4% is neighbouring single
     */

    //TODO : baseline is checkout%, en de cijfers voor checkout% 1,2,3 darter gebruiken om de random te beperken om meer kans over te laten
    //TODO : meer kans overlaten, genereer X aantal randoms; laat de hoogste vallen? Neem gemiddelde van y resultaten naargelang %?
    @Override
    public Throw simulateThrow(Turn turn, Dart dart, int scoreToAim, Player player, boolean isFinishingShot) {
        //  return result of throwing for double d with accuracy p%
        int r = generateRandom();
        int accuracyPercentage = switch(dart) {
            case FIRST, SECOND -> player.accuracyDouble();
            case THIRD -> player.accuracyDouble3rdDart();
        };

        if (r < accuracyPercentage) {
            return new Throw(null, 2 * scoreToAim, true, isFinishingShot, false, false);
        }
        else if (r < accuracyPercentage + (100 - accuracyPercentage)*0.7) {
            //return single value and zero (complete 'miss' options) with 30% of the probability not to hit as intended
            return new Throw(null, 0, false, isFinishingShot, false, false);
        }
        else if (r < accuracyPercentage + (100 - accuracyPercentage)*0.9) {
            return new Throw(null, scoreToAim, false, isFinishingShot, false, false);
        }
        else if (r < accuracyPercentage + (100 - accuracyPercentage)*0.93) {
            //return each neighbouring double with 15% of the probability not to hit as intended
            return new Throw(null, 2 * DART_BOARD[0][scoreToAim], true, isFinishingShot, false, false);
        }
        else if (r < accuracyPercentage + (100 - accuracyPercentage)*0.96) {
            return new Throw(null, 2 * DART_BOARD[1][scoreToAim], true, isFinishingShot, false, false);
        }
        else if (r < accuracyPercentage + (100 - accuracyPercentage)*0.98) {
            //return each neighbouring single with 5% of the probability not to hit as intended
            return new Throw(null, DART_BOARD[0][scoreToAim], false, isFinishingShot, false, false);
        }
        else {
            return new Throw(null, DART_BOARD[1][scoreToAim], false, isFinishingShot, false, false);
        }
    }
}
