package be.rommens.darts.simulator.strategy;

import be.rommens.darts.simulator.model.Dart;
import be.rommens.darts.simulator.model.Player;
import be.rommens.darts.simulator.model.Throw;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.geometry.euclidean.twod.PolarCoordinates;
import org.apache.commons.geometry.euclidean.twod.Vector2D;
import org.jfree.data.xy.XYSeries;

//TODO : https://github.com/Jacob-Lang/Monte-Carlo-Simulation---Darts/blob/master/darts.py
//TODO : https://www.researchgate.net/figure/Specification-of-points-on-dartboard_fig2_325194809
//TODO : https://github.com/jfree/jfreechart
//TODO : https://www.javatpoint.com/jfreechart-scatter-chart
public interface ThrowSimulationStrategy {

    int[][] DART_BOARD = {
            {0, 20, 15, 17, 18, 12, 13, 19, 16, 14, 6, 8, 9, 4, 11, 10, 7, 2, 1, 3, 5}, //numbers on dartboard anticlockwise
            {0, 18, 17, 19, 13, 20, 10, 16, 11, 12, 15, 14, 5, 6, 9, 2, 8, 3, 4, 7, 1}  //numbers on dartboard clockwise}
    };

    int[] SEGMENT_VALUES = { 20, 1, 18, 4, 13, 6, 10, 15, 2, 17, 3, 19, 7, 16, 8, 11, 14, 9, 12, 5, 20 };


    Throw simulateThrow(Dart dart, int scoreToAim, Player player, boolean isFinishingShot);

    default int generateRandom() {
        int r = ThreadLocalRandom.current().nextInt(100);
        return r;
    }

    //TODO : accuracy
    default Vector2D generateRandomVector(Vector2D aim) {
        return Vector2D.of(
                ThreadLocalRandom.current().nextGaussian(aim.getX(), 10),
                ThreadLocalRandom.current().nextGaussian(aim.getY(), 10));
    }

    default int getFieldFromPolarCoordinates(PolarCoordinates polar) {
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
