package be.rommens.darts.simulator.strategy.orginal;

import be.rommens.darts.simulator.model.Dart;
import be.rommens.darts.simulator.model.Player;
import be.rommens.darts.simulator.model.Throw;
import be.rommens.darts.simulator.strategy.AimType;
import be.rommens.darts.simulator.strategy.ThrowSimulationStrategy;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("original")
@Component(AimType.BULL)
public class BullThrowSimulationStrategy implements ThrowSimulationStrategy {

    @Override
    public Throw simulateThrow(Dart dart, int scoreToAim, Player player, boolean isFinishingShot) {
        //Throw for the bull with accuracy p%; iV stands for "is this valid as a finishing throw?"

        //C++ : rand() % 100 => 0 - 99
        //C++ : rand() % 100 + 1  => 1 - 100
        int r = generateRandom();
        int accuracyPercentage = player.accuracyBull();
        System.out.printf("(%s - %s) ", r, accuracyPercentage);
        // formerly: hit bull with p=accurancy-20% // I changed this because it made no sense to me that, if the accuracy is p%,
        // the probability of hitting accurately should be p%-20%
        if (r < accuracyPercentage) {
            //isFinishingThrow stands for "is this valid as a finishing throw?" (yes, it is)
            return new Throw(50, true, isFinishingShot, false);
        }
        else if (r < accuracyPercentage + (100 - accuracyPercentage)*0.6) {
            // hit 25 with accuracy a 60% given that not to hit as intended
            return new Throw(25, false, isFinishingShot, false);
        }
        else {
            //remaining 40% (given that not to hit as intended) return any other single number with the same probability
            return new Throw(1 + ThreadLocalRandom.current().nextInt(20), false, isFinishingShot, false);
        }
    }
}
