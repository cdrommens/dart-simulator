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
@Component(AimType.DOUBLE)
public class DoubleThrowSimulationStrategy implements ThrowSimulationStrategy {

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
    public Throw simulateThrow(Dart dart, int scoreToAim, Player player, boolean isFinishingShot) {

        int accuracyPercentage = switch(dart) {
            case FIRST, SECOND -> player.accuracyDouble();
            case THIRD -> player.accuracyDouble3rdDart();
        };
        Vector2D r = generateRandomVector(COORDINATES.get(scoreToAim));

        PolarCoordinates result = PolarCoordinates.fromCartesian(r);

        if (result.getRadius() <= 6.35) {
            return new Throw(50, true, isFinishingShot, false);
        }
        if (result.getRadius() > 6.35 && result.getRadius() <= 15.9) {
            return new Throw(25, false, isFinishingShot, false);
        }

        int number = getFieldFromPolarCoordinates(result);

        if (result.getRadius() > 99 && result.getRadius() <= 107) {
            return new Throw(3 * number, false, isFinishingShot, false);
        }
        if (result.getRadius() > 162 && result.getRadius() <= 170) {
            return new Throw(2 * number, true, isFinishingShot, false);
        }
        if (result.getRadius() > 170) {
            return new Throw(0, false, isFinishingShot, false);
        }
        return new Throw(number, false, isFinishingShot, false);
    }
}
