package be.rommens.darts.simulator;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Locale;
import org.junit.jupiter.api.Test;

public class AndereTests {

    @Test
    void nummertest() throws URISyntaxException, IOException {
        //Integer result = Integer.parseInt("501");
        var erui = Path.of(ThrowSimulator.class.getClassLoader().getResource("optimal_path.csv").toURI());
        var lijst = Files.readAllLines(erui, StandardCharsets.UTF_8);
        var item = lijst.get(1);
        var split = item.split(";");
        Integer num = Integer.valueOf(split[0]);
        System.out.println("lijst = " + split[0]);
    }

}
