package be.rommens.darts.simulator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SimulatorApplicationTests {

	@Autowired
	Source source;

	@Test
	void contextLoads() {
		source.playGame();
	}

}
