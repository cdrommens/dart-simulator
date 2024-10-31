package be.rommens.darts.simulator.strategy.polar;

import static org.assertj.core.api.Assertions.assertThat;

import be.rommens.darts.simulator.model.Dart;
import be.rommens.darts.simulator.model.Player;
import be.rommens.darts.simulator.model.Statistics;
import be.rommens.darts.simulator.model.Throw;
import be.rommens.darts.simulator.model.Turn;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jfree.data.xy.XYSeries;
import org.junit.jupiter.api.Test;

public class DoubleThrowSimulationStrategyTest {

    @Test
    public void testTurn() {
        DoubleThrowSimulationStrategy strategy = new DoubleThrowSimulationStrategy();
        Turn turn = new Turn(40);
        for (Dart dart: Dart.values()) {
            Throw result = strategy.simulateThrow(
                    null, Dart.FIRST,
                    20,
                    new Player("Humphries",25,50,95,42,44.42, 43, 108.89),
                    true);
            //turn.addThrow(dart, result);
        }
        System.out.println(turn);
    }

    @Test
    public void testFirst9Avg() throws IOException {
        DoubleThrowSimulationStrategy strategy = new DoubleThrowSimulationStrategy();
        List<Turn> turns = new ArrayList<>();
        XYSeries series = new XYSeries("hits");
        for (int i=0; i < 3; i++) {
            Turn turn = new Turn(40);
            for (Dart dart : Dart.values()) {
                Throw result = strategy.simulateThrow(
                        turn, dart,
                        20,
                        new Player("Humphries", 25, 50, 95, 42, 44.42, 43, 108.89),
                        true);
                //turn.addThrow(dart, result);
                series.add(Math.toDegrees(result.point().getAzimuth()), result.point().getRadius());
                if (result.isFinishingThrow()) {
                    break;
                }
            }
            turns.add(turn);
            System.out.println(turn);
        }
        WriteUtils.draw(series);
    }

    @Test
    public void testFirst9AvgInBatch() throws IOException {
        TrebleThrowSimulationStrategy strategy = new TrebleThrowSimulationStrategy();
        List<Turn> turns = new ArrayList<>();
        List<Statistics> stats = new ArrayList<>();
        XYSeries series = new XYSeries("hits");
        for (int y=0; y < 3000; y++) {
            for (int i = 0; i < 3; i++) {
                Turn turn = new Turn(501);
                for (Dart dart : Dart.values()) {
                    Throw result = strategy.simulateThrow(
                            turn, dart,
                            20,
                            new Player("Humphries", 25, 50, 95, 42, 44.42, 43, 108.89),
                            false);
                    //turn.addThrow(dart, result);
                    series.add(Math.toDegrees(result.point().getAzimuth()), result.point().getRadius());
                }
                turns.add(turn);
            }
            stats.add(Statistics.calculate(turns));
        }
        var avg = stats.stream().mapToDouble(Statistics::getFirst9Average).average().orElseThrow();
        WriteUtils.draw(series);
        assertThat(avg).isBetween(90.00, 120.00);
    }
}
