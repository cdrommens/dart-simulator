package be.rommens.darts.simulator.strategy.polar;

import static be.rommens.darts.simulator.model.Dart.FIRST;
import static be.rommens.darts.simulator.model.Dart.SECOND;
import static be.rommens.darts.simulator.model.Dart.THIRD;

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
@Component(AimType.DOUBLE)
public class DoubleThrowSimulationStrategy extends PolarThrowSimulationStrategy {

    private static final int DOUBLE_RADIUS = 16;

    private static final Map<Integer, Vector2D> COORDINATES = Map.ofEntries(
            Map.entry(20, Vector2D.of(166,0)),
            Map.entry(1, Vector2D.of(158,51)),
            Map.entry(18, Vector2D.of(134,98)),
            Map.entry(4, Vector2D.of(98,134)),
            Map.entry(13, Vector2D.of(51,158)),
            Map.entry(6, Vector2D.of(0,166)),
            Map.entry(10, Vector2D.of(-51,158)),
            Map.entry(15, Vector2D.of(-98,134)),
            Map.entry(2, Vector2D.of(-134,98)),
            Map.entry(17, Vector2D.of(-158,51)),
            Map.entry(3, Vector2D.of(-166,0)),
            Map.entry(19, Vector2D.of(-158,-51)),
            Map.entry(7, Vector2D.of(-134,-98)),
            Map.entry(16, Vector2D.of(-98,-134)),
            Map.entry(8, Vector2D.of(-51,-158)),
            Map.entry(11, Vector2D.of(0,-166)),
            Map.entry(14, Vector2D.of(51,-158)),
            Map.entry(9, Vector2D.of(98,-134)),
            Map.entry(12, Vector2D.of(134,-98)),
            Map.entry(5, Vector2D.of(158,-51))
    );

    //TODO : baseline is checkout%, en de cijfers voor checkout% 1,2,3 darter gebruiken om de random te beperken om meer kans over te laten
    //TODO : meer kans overlaten, genereer X aantal randoms; laat de hoogste vallen? Neem gemiddelde van y resultaten naargelang %?
    @Override
    public Throw simulateThrow(Turn turn, Dart dart, int scoreToAim, Player player, boolean isFinishingShot) {

        PolarCoordinates result;
        do {
            var previousRadius = turn.getPreviousThrow(dart).map(Throw::point).map(PolarCoordinates::getRadius).orElse(0.0);
            Vector2D r = previousRadius >= 170 && previousRadius <= 180 ? generateRandomVector(5, COORDINATES.get(scoreToAim)) : generateRandomVector(COORDINATES.get(scoreToAim));
            result = PolarCoordinates.fromCartesian(r);
        } while (!isInRadius(COORDINATES.get(scoreToAim), result, player));

        if (result.getRadius() <= 6.35) {
            return new Throw(result, 50, true, isFinishingShot, false, true);
        }
        if (result.getRadius() > 6.35 && result.getRadius() <= 15.9) {
            return new Throw(result, 25, false, isFinishingShot, false, false);
        }

        int number = getFieldFromPolarCoordinates(result);

        if (result.getRadius() > 99 && result.getRadius() <= 107) {
            return new Throw(result, 3 * number, false, isFinishingShot, false, false);
        }
        if (result.getRadius() > 162 && result.getRadius() <= 170) {
            return new Throw(result, 2 * number, true, isFinishingShot, false, true);
        }
        if (result.getRadius() > 170) {
            return new Throw(result, 0, false, isFinishingShot, false, false);
        }
        return new Throw(result, number, false, isFinishingShot, false, false);
    }

    private boolean isInRadius(Vector2D aim, PolarCoordinates dart, Player player) {
        PolarCoordinates center = PolarCoordinates.fromCartesian(aim);
        var center2d = center.toCartesian();
        var dart2d = dart.toCartesian();
        var distance = Math.pow((dart2d.getX() - center2d.getX()),2) + Math.pow((dart2d.getY() - center2d.getY()),2);
        if (distance < (Math.pow(DOUBLE_RADIUS, 2))) {
            return true;
        }
        var r = generateRandom();
        return r >= player.accuracyDouble();
    }
}
