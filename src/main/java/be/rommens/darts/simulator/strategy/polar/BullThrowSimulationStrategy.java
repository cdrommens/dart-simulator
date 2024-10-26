package be.rommens.darts.simulator.strategy.polar;

import be.rommens.darts.simulator.model.Dart;
import be.rommens.darts.simulator.model.Player;
import be.rommens.darts.simulator.model.Throw;
import be.rommens.darts.simulator.strategy.AimType;
import be.rommens.darts.simulator.strategy.ThrowSimulationStrategy;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.geometry.euclidean.twod.PolarCoordinates;
import org.apache.commons.geometry.euclidean.twod.Vector2D;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("polar")
@Component(AimType.BULL)
public class BullThrowSimulationStrategy implements ThrowSimulationStrategy {

    private static final Map<Integer, Vector2D> COORDINATES = Map.ofEntries(
            Map.entry(25, Vector2D.of(11,0)),
            Map.entry(50, Vector2D.of(0,0))
    );

    @Override
    public Throw simulateThrow(Dart dart, int scoreToAim, Player player, boolean isFinishingShot) {
        Vector2D r = generateRandomVector(COORDINATES.get(scoreToAim));

        PolarCoordinates result = PolarCoordinates.fromCartesian(r);

        if (result.getRadius() <= 6.35) {
            return new Throw(50, true, false, false);
        }
        if (result.getRadius() > 6.35 && result.getRadius() <= 15.9) {
            return new Throw(25, false, false, false);
        }

        int number = getFieldFromPolarCoordinates(result);

        if (result.getRadius() > 99 && result.getRadius() <= 107) {
            return new Throw(3 * number, false, false, false);
        }
        if (result.getRadius() > 162 && result.getRadius() <= 170) {
            return new Throw(2 * number, false, false, false);
        }
        if (result.getRadius() > 170) {
            return new Throw(0, false, false, false);
        }
        return new Throw(number, false, false, false);
    }
}
