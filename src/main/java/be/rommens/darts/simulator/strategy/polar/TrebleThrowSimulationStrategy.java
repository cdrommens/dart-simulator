package be.rommens.darts.simulator.strategy.polar;

import be.rommens.darts.simulator.model.Dart;
import be.rommens.darts.simulator.model.Player;
import be.rommens.darts.simulator.model.Throw;
import be.rommens.darts.simulator.strategy.AimType;
import be.rommens.darts.simulator.strategy.ThrowSimulationStrategy;
import java.util.Map;
import org.apache.commons.geometry.euclidean.twod.PolarCoordinates;
import org.apache.commons.geometry.euclidean.twod.Vector2D;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("polar")
@Component(AimType.TREBLE)
public class TrebleThrowSimulationStrategy implements ThrowSimulationStrategy {

    private static final Map<Integer, Vector2D> COORDINATES = Map.ofEntries(

            Map.entry(20, Vector2D.of(103,0)),
            Map.entry(1, Vector2D.of(98,32)),
            Map.entry(18, Vector2D.of(83,61)),
            Map.entry(4, Vector2D.of(61,83)),
            Map.entry(13, Vector2D.of(32,98)),
            Map.entry(6, Vector2D.of(0,103)),
            Map.entry(10, Vector2D.of(-32,98)),
            Map.entry(15, Vector2D.of(-61,83)),
            Map.entry(2, Vector2D.of(-83,61)),
            Map.entry(17, Vector2D.of(-98,32)),
            Map.entry(3, Vector2D.of(-103,0)),
            Map.entry(19, Vector2D.of(-98,-32)),
            Map.entry(7, Vector2D.of(-83,-61)),
            Map.entry(16, Vector2D.of(-61,-83)),
            Map.entry(8, Vector2D.of(-32,-98)),
            Map.entry(11, Vector2D.of(0,-103)),
            Map.entry(14, Vector2D.of(32,-98)),
            Map.entry(9, Vector2D.of(61,-83)),
            Map.entry(12, Vector2D.of(83,-61)),
            Map.entry(5, Vector2D.of(98,-32))
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
            return new Throw(2 * number, true, false, false);
        }
        if (result.getRadius() > 170) {
            return new Throw(0, false, false, false);
        }
        return new Throw(number, false, false, false);
    }
}
