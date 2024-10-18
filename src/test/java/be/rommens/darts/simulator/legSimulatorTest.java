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
        legSimulator.playGame(new Player("Humphries",25,50,95,42,44));
        legSimulator.calculateStatistics();
    }

    @Test
    void testBatch() {
        while (true) {
            legSimulator.playGame(new Player("Humphries",25,50,95,42,44));
        }
    }

}
