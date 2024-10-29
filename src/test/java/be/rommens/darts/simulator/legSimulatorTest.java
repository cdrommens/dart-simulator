package be.rommens.darts.simulator;

import be.rommens.darts.simulator.model.Player;
import be.rommens.darts.simulator.model.Statistics;
import be.rommens.darts.simulator.model.Turn;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("original")
@SpringBootTest
public class legSimulatorTest {

    @Autowired
    private LegSimulator legSimulator;

    @Test
    void singleGameTest() {
        var leg = legSimulator.playLeg(new Player("Humphries",25,50,95,42,44, 43, 108));
        for(Turn turn : leg) {
            System.out.println(turn.getStartScore() + " : " + turn.getScoreThrown() + " (" + turn + ")");
        }
        Statistics.calculate(leg).write();
    }

    @Test
    void testBatch() {
        List<Statistics> statistics = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            var result = legSimulator.playLeg(new Player("Humphries",25,50,95,42,44, 43, 108));
            statistics.add(Statistics.calculate(result));
        }
    }

}
