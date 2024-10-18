package be.rommens.darts.simulator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class legSimulatorTest {

    @Autowired
    private legSimulator legSimulator;

    @Test
    void singleGameTest() {
        var leg = legSimulator.playGame(new Player("Humphries",25,50,95,42,44));
        for(Turn turn : leg) {
            System.out.println(turn.getStartScore() + " : " + turn.getThrows().map(Throw::score).reduce(0, Integer::sum) + " (" + turn + ")");
        }
        Statistics.calculate(leg).write();
    }

    @Test
    void testBatch() {
        while (true) {
            legSimulator.playGame(new Player("Humphries",25,50,95,42,44));
        }
    }

}
