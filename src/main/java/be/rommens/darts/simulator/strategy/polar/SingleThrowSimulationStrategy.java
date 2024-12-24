package be.rommens.darts.simulator.strategy.polar;

import be.rommens.darts.simulator.model.Dart;
import be.rommens.darts.simulator.model.Player;
import be.rommens.darts.simulator.model.Throw;
import be.rommens.darts.simulator.model.Turn;
import be.rommens.darts.simulator.strategy.AimType;
import java.util.Map;
import org.apache.commons.geometry.euclidean.twod.PolarCoordinates;
import org.apache.commons.geometry.euclidean.twod.Vector2D;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("polar")
@Component(AimType.SINGLE)
public class SingleThrowSimulationStrategy extends PolarThrowSimulationStrategy {

    private static final Map<Integer, Vector2D> COORDINATES = Map.ofEntries(
            Map.entry(20, Vector2D.of(135,0)),
            Map.entry(1, Vector2D.of(128,42)),
            Map.entry(18, Vector2D.of(109,79)),
            Map.entry(4, Vector2D.of(79,109)),
            Map.entry(13, Vector2D.of(42,128)),
            Map.entry(6, Vector2D.of(0,135)),
            Map.entry(10, Vector2D.of(-42,128)),
            Map.entry(15, Vector2D.of(-79,109)),
            Map.entry(2, Vector2D.of(-109,79)),
            Map.entry(17, Vector2D.of(-128,42)),
            Map.entry(3, Vector2D.of(-135,0)),
            Map.entry(19, Vector2D.of(-128,-42)),
            Map.entry(7, Vector2D.of(-109,-79)),
            Map.entry(16, Vector2D.of(-79,-109)),
            Map.entry(8, Vector2D.of(-42,-128)),
            Map.entry(11, Vector2D.of(0,-135)),
            Map.entry(14, Vector2D.of(42,-128)),
            Map.entry(9, Vector2D.of(79,-109)),
            Map.entry(12, Vector2D.of(109,-79)),
            Map.entry(5, Vector2D.of(128,-42)),
            Map.entry(25, Vector2D.of(11,0))
    );

    @Override
    public Throw simulateThrow(Turn turn, Dart dart, int scoreToAim, Player player, boolean isFinishingShot) {
        Vector2D r = generateRandomVector(COORDINATES.get(scoreToAim), player);

        PolarCoordinates result = PolarCoordinates.fromCartesian(r);

        if (result.getRadius() <= 6.35) {
            return new Throw(result, 50, true, false, false, false);
        }
        if (result.getRadius() > 6.35 && result.getRadius() <= 15.9) {
            return new Throw(result, 25, false, false, false, false);
        }

        int number = getFieldFromPolarCoordinates(result);

        if (result.getRadius() > 99 && result.getRadius() <= 107) {
            return new Throw(result, 3 * number, false, false, false, false);
        }
        if (result.getRadius() > 162 && result.getRadius() <= 170) {
            return new Throw(result, 2 * number, false, false, false, false);
        }
        if (result.getRadius() > 170) {
            return new Throw(result, 0, false, false, false, true);
        }
        return new Throw(result, number, false, false, false, true);
    }
}
