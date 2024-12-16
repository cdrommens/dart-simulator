package be.rommens.darts.simulator;

import be.rommens.darts.simulator.model.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("polar")
@SpringBootTest
public class GameSimulatorTest {

    @Autowired
    GameSimulator gameSimulator;

    @Test
    void testGame() {
        var player1 = new Player("Humphries",24.35, 41.69, 44.49, 42.94, 108.94);
        var player2 = new Player("Whitlock",40.00, 36.79, 35.32, 37.04, 97.66);

        gameSimulator.simulate(player1, player2, 11);
    }

}
