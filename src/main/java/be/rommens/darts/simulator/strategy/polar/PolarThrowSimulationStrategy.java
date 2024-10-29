package be.rommens.darts.simulator.strategy.polar;

import be.rommens.darts.simulator.model.Player;
import be.rommens.darts.simulator.strategy.ThrowSimulationStrategy;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.geometry.euclidean.twod.PolarCoordinates;
import org.apache.commons.geometry.euclidean.twod.Vector2D;

public abstract class PolarThrowSimulationStrategy implements ThrowSimulationStrategy {

    private static final int STANDARD_DEVIATION = 10;

    private final int[] SEGMENT_VALUES = { 20, 1, 18, 4, 13, 6, 10, 15, 2, 17, 3, 19, 7, 16, 8, 11, 14, 9, 12, 5, 20 };

    //TODO : accuracy
    Vector2D generateRandomVector(double stddev, Vector2D aim) {
        return Vector2D.of(
                ThreadLocalRandom.current().nextGaussian(aim.getX(), stddev),
                ThreadLocalRandom.current().nextGaussian(aim.getY(), stddev));
    }

    Vector2D generateRandomVector(Vector2D aim) {
        return generateRandomVector(STANDARD_DEVIATION, aim);
    }

    int getFieldFromPolarCoordinates(PolarCoordinates polar) {
        int index = Math.floorDiv(new BigDecimal(String.valueOf(Math.toDegrees(polar.getAzimuth()))).setScale(0, RoundingMode.HALF_UP).intValue() + 9, 18);
        return SEGMENT_VALUES[index];
    }

    /*
    Gaussion distribution : mean 0 en deviation 1 wil zeggen : 70% van de values zullen tussen -1 en 1 zitten
    nextGaussian() * 100 + 500 => 70% van de values zullen tussen 400 en 600 zitten

     stel: center T20 : 103, 0
     rechts: 102, 16
     links: 102, -16
     boven : 107, 0
     onder: 99, 0

     voor x : ik wil dat 42% van de values tussen 99 en 107 zit : nextGaussian() * 103 + (107-99=8)
     voor y : ik wil dat 42% van de values tussen 16 en -16 zit : nextGaussian() * 0 + (32)
     */

    /**
     * - teken een cirkel, de cirkel is afhankelijk van de first 9 average
     * - als buiten de cirkel valt, naargelang de trebble% random opnieuw
     *
     * - als pijl in treble valt, kans groter maken dat de volgende er ook in zit?
     */

}
