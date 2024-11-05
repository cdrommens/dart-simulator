package be.rommens.darts.simulator;

import be.rommens.darts.simulator.guide.OptimalPath;
import be.rommens.darts.simulator.model.Dart;
import be.rommens.darts.simulator.model.Player;
import be.rommens.darts.simulator.model.Statistics;
import be.rommens.darts.simulator.model.Throw;
import be.rommens.darts.simulator.model.Turn;
import be.rommens.darts.simulator.strategy.ThrowSimulationStrategy;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("polar")
@SpringBootTest
public class LegSimulatorPolarTest {

    @Autowired
    private LegSimulator legSimulator;

    @Test
    void test100LegsWorldClass() {
        var player = new Player("Humphries",24.35, 41.69, 44.49, 42.94, 108.94);
        List<Statistics> statistics = new ArrayList<>();

        for(int i = 0; i < 1000; i++) {
            var result = legSimulator.playLeg(player);
            statistics.add(result.statistics());
            //result.statistics().write();
        }
        var first9 = statistics.stream().mapToDouble(Statistics::getFirst9Average).average().orElseThrow();
        var avg = statistics.stream().mapToDouble(Statistics::getAverage).average().orElseThrow();
        var checkout = statistics.stream().mapToDouble(Statistics::getCheckoutPercentage).average().orElseThrow();
        System.out.printf("First 9 avg : %s%n", first9);
        System.out.printf("Avg : %s%n", avg);
        System.out.printf("checkout : %s%n", checkout);
        Assertions.assertThat(first9).isBetween(87.0, 130.0);
        Assertions.assertThat(avg).isGreaterThan(87.00);
        Assertions.assertThat(checkout).isBetween(35.0, 100.0);
    }

    @Test
    void test100PdcTourClass() {
        var player = new Player("Whitlock",40.00, 36.79, 35.32, 37.04, 97.66);
        List<Statistics> statistics = new ArrayList<>();

        for(int i = 0; i < 1000; i++) {
            var result = legSimulator.playLeg(player);
            statistics.add(result.statistics());
            //result.statistics().write();
        }
        var first9 = statistics.stream().mapToDouble(Statistics::getFirst9Average).average().orElseThrow();
        var avg = statistics.stream().mapToDouble(Statistics::getAverage).average().orElseThrow();
        var checkout = statistics.stream().mapToDouble(Statistics::getCheckoutPercentage).average().orElseThrow();
        System.out.printf("First 9 avg : %s%n", first9);
        System.out.printf("Avg : %s%n", avg);
        System.out.printf("checkout : %s%n", checkout);
        Assertions.assertThat(first9).isBetween(87.0, 130.0);
        Assertions.assertThat(avg).isGreaterThan(87.00);
        Assertions.assertThat(checkout).isBetween(35.0, 100.0);
    }

    @Test
    void test100PdcChallengerTourClass() {
        var player = new Player("van Wauwe",15.00, 31.93, 23.95, 31.93, 83.03);
        List<Statistics> statistics = new ArrayList<>();

        for(int i = 0; i < 1000; i++) {
            var result = legSimulator.playLeg(player);
            statistics.add(result.statistics());
            //result.statistics().write();
            System.out.println(result.turns());
        }
        var first9 = statistics.stream().mapToDouble(Statistics::getFirst9Average).average().orElseThrow();
        var avg = statistics.stream().mapToDouble(Statistics::getAverage).average().orElseThrow();
        var checkout = statistics.stream().mapToDouble(Statistics::getCheckoutPercentage).average().orElseThrow();
        System.out.printf("First 9 avg : %s%n", first9);
        System.out.printf("Avg : %s%n", avg);
        System.out.printf("checkout : %s%n", checkout);
        Assertions.assertThat(first9).isBetween(87.0, 92.0);
        Assertions.assertThat(avg).isGreaterThan(65.00);
        Assertions.assertThat(checkout).isBetween(35.0, 100.0);
    }

    @Test
    void test100RestClass() {
        var player = new Player("Schoenmakers",15.00, 18.18, 18.79, 18.18, 66.77);
        List<Statistics> statistics = new ArrayList<>();

        for(int i = 0; i < 1000; i++) {
            var result = legSimulator.playLeg(player);
            statistics.add(result.statistics());
            //result.statistics().write();
        }
        var first9 = statistics.stream().mapToDouble(Statistics::getFirst9Average).average().orElseThrow();
        var avg = statistics.stream().mapToDouble(Statistics::getAverage).average().orElseThrow();
        var checkout = statistics.stream().mapToDouble(Statistics::getCheckoutPercentage).average().orElseThrow();
        System.out.printf("First 9 avg : %s%n", first9);
        System.out.printf("Avg : %s%n", avg);
        System.out.printf("checkout : %s%n", checkout);
        Assertions.assertThat(first9).isBetween(0.0, 76.0);
        Assertions.assertThat(avg).isGreaterThan(35.00);
        Assertions.assertThat(checkout).isBetween(15.0, 35.0);
    }

    @Test
    @Disabled
    void nekeer() {
        var player = new Player("Schoenmakers",15.00, 18.18, 18.79, 18.18, 66.77);

        List<Turn> turns = new ArrayList<>();
        /*
        turns.add(createTurn(player, 501,
                new Throw(null, 20, false, false, false, false),
                new Throw(null, 5, false, false, false, false),
                new Throw(null, 20, false, false, false, false)));
        turns.add(createTurn(player, 456,
                new Throw(null, 60, false, false, false, true),
                new Throw(null, 60, false, false, false, true),
                new Throw(null, 60, false, false, false, true)));
        turns.add(createTurn(player, 276,
                new Throw(null, 20, false, false, false, false),
                new Throw(null, 20, false, false, false, false),
                new Throw(null, 20, false, false, false, false)));
        turns.add(createTurn(player, 216,
                new Throw(null, 1, false, false, false, false),
                new Throw(null, 20, false, false, false, false),
                new Throw(null, 12, false, false, false, true)));
        turns.add(createTurn(player, 183,
                new Throw(null, 20, false, false, false, false),
                new Throw(null, 5, false, false, false, false),
                new Throw(null, 20, false, false, false, false)));
        turns.add(createTurn(player, 138,
                new Throw(null, 20, false, false, false, false),
                new Throw(null, 5, false, false, false, false),
                new Throw(null, 3, false, false, false, true)));
        turns.add(createTurn(player, 110,
                new Throw(null, 20, false, false, false, false),
                new Throw(null, 18, false, false, false, false),
                new Throw(null, 16, false, false, false, false)));

         */
        turns.add(createTurn(player, 56,
                new Throw(null, 0, false, false, false, true),
                new Throw(null, 16, false, false, false, true),
                new Throw(null, 20, false, true, false, false)));

        var result = turns.stream()
                .filter(turn -> !turn.isBusted())
                .map(Turn::getScoreLeft)
                .filter(score -> score >= 0)
                .min(Comparator.naturalOrder())
                .orElse(501);
    }

    private static Turn createTurn(Player player, int startscore, Throw throw1, Throw throw2, Throw throw3) {
        var turn = new Turn(startscore);
        turn.throwDarts(player, new StaticSimulator(null, null, throw1, throw2, throw3));
        return turn;
    }

    static class StaticSimulator extends ThrowSimulator {
        private final Throw first;
        private final Throw second;
        private final Throw third;

        public StaticSimulator(OptimalPath optimalPath,
                Map<String, ThrowSimulationStrategy> throwSimulationStrategies, Throw first, Throw second, Throw third) {
            super(optimalPath, throwSimulationStrategies);
            this.first = first;
            this.second = second;
            this.third = third;
        }

        @Override
        public Throw throwDart(Turn turn, Dart dart, int currentScore, Player player) {
            return switch (dart) {
                case FIRST -> first;
                case SECOND -> second;
                case THIRD -> third;
            };
        }
    }
}
