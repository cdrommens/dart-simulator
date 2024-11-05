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
@Component(AimType.TREBLE)
public class TrebleThrowSimulationStrategy extends PolarThrowSimulationStrategy {

    private static final int TREBLE_RADIUS = 16;

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
    public Throw simulateThrow(Turn turn, Dart dart, int scoreToAim, Player player, boolean isFinishingShot) {

        PolarCoordinates result;
        do {
            boolean isPreviousTreble = turn.getPreviousThrow(dart).map(Throw::isHitAsIntended).orElse(false);
            Vector2D r = isPreviousTreble ? generateRandomVector(5, COORDINATES.get(scoreToAim)) : generateRandomVector(COORDINATES.get(scoreToAim), player);
            result = PolarCoordinates.fromCartesian(r);
        } while (!isInRadius(COORDINATES.get(scoreToAim), result, player));

        if (result.getRadius() <= 6.35) {
            return new Throw(result, 50, true, false, false, false);
        }
        if (result.getRadius() > 6.35 && result.getRadius() <= 15.9) {
            return new Throw(result, 25, false, false, false, false);
        }

        int number = getFieldFromPolarCoordinates(result);

        if (result.getRadius() > 99 && result.getRadius() <= 107) {
            return new Throw(result, 3 * number, false, false, false, true);
        }
        if (result.getRadius() > 162 && result.getRadius() <= 170) {
            return new Throw(result,2 * number, true, false, false, false);
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
        if (distance < (Math.pow((TREBLE_RADIUS + calculateRadius(player) - player.first9avg()), 2))) {
            return true;
        }
        var r = generateRandom();
        return r >= player.accuracyTreble();
    }

    private int calculateRadius(Player player) {
        if (player.first9avg() <= 45.00) {
            // 35-45 is considered a beginner average
            return 40;
        } else if (player.first9avg() <= 55.00) {
            // 46-55 pub player
            return 20;
        } else if (player.first9avg() <= 70.00) {
            // super league / county player
            return 17;
        } else if (player.first9avg() <= 85.00) {
            // PDC Challenge Tour
            return 8;
        } else if (player.first9avg() <= 99.00) {
            // PDC Tour
            return 5;
        } else {
            // world class
            return 4;
        }
    }
}
