package be.rommens.darts.simulator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SingleGameTest {

    @Autowired
    private SingleGame singleGame;

    @Test
    void singleGameTest() {
        singleGame.playGame(new Player("Humphries",25,50,95,42,44));
        singleGame.calculateStatistics();
    }

    @Test
    void testBatch() {
        while (true) {
            singleGame.playGame(new Player("Humphries",25,50,95,42,44));
        }
    }

}
