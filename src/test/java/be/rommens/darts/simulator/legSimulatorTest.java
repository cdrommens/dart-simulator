package be.rommens.darts.simulator;

import be.rommens.darts.simulator.model.Player;
import be.rommens.darts.simulator.model.Statistics;
import be.rommens.darts.simulator.model.Throw;
import be.rommens.darts.simulator.model.Turn;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class legSimulatorTest {

    @Autowired
    private legSimulator legSimulator;

    @Test
    void singleGameTest() {
        var leg = legSimulator.playGame(new Player("Humphries",25,50,95,42,44, 43));
        for(Turn turn : leg) {
            System.out.println(turn.getStartScore() + " : " + turn.getScoreThrown() + " (" + turn + ")");
        }
        Statistics.calculate(leg).write();
    }

    @Test
    void testBatch() {
        List<Statistics> statistics = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            var result = legSimulator.playGame(new Player("Humphries",25,50,95,42,44, 43));
            statistics.add(Statistics.calculate(result));
        }
    }

}
