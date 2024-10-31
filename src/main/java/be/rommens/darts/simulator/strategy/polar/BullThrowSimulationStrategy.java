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
@Component(AimType.BULL)
public class BullThrowSimulationStrategy extends PolarThrowSimulationStrategy {

    private static final int BULL_RADIUS = 16;

    private static final Map<Integer, Vector2D> COORDINATES = Map.ofEntries(
            Map.entry(25, Vector2D.of(11,0)),
            Map.entry(50, Vector2D.of(0,0))
    );

    @Override
    public Throw simulateThrow(Turn turn, Dart dart, int scoreToAim, Player player, boolean isFinishingShot) {
        PolarCoordinates result;
        do {
            Vector2D r = generateRandomVector(COORDINATES.get(scoreToAim));
            result = PolarCoordinates.fromCartesian(r);
        } while (!isInRadius(COORDINATES.get(scoreToAim), result, player));

        if (result.getRadius() <= 6.35) {
            return new Throw(result, 50, true, isFinishingShot, false, scoreToAim == 50);
        }
        if (result.getRadius() > 6.35 && result.getRadius() <= 15.9) {
            return new Throw(result, 25, false, false, false, scoreToAim == 25);
        }

        int number = getFieldFromPolarCoordinates(result);

        if (result.getRadius() > 99 && result.getRadius() <= 107) {
            return new Throw(result, 3 * number, false, false, false, false);
        }
        if (result.getRadius() > 162 && result.getRadius() <= 170) {
            return new Throw(result, 2 * number, false, false, false, false);
        }
        if (result.getRadius() > 170) {
            return new Throw(result, 0, false, false, false, false);
        }
        return new Throw(result, number, false, false, false, false);
    }

    private boolean isInRadius(Vector2D aim, PolarCoordinates dart, Player player) {
        PolarCoordinates center = PolarCoordinates.fromCartesian(aim);
        var center2d = center.toCartesian();
        var dart2d = dart.toCartesian();
        var distance = Math.pow((dart2d.getX() - center2d.getX()),2) + Math.pow((dart2d.getY() - center2d.getY()),2);
        if (distance < (Math.pow(BULL_RADIUS, 2))) {
            return true;
        }
        var r = generateRandom();
        return r >= player.accuracyBull();
    }
}
