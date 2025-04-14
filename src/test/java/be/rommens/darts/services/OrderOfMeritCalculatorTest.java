package be.rommens.darts.services;

import be.rommens.darts.database.domain.Player;
import be.rommens.darts.database.domain.TournamentResult;
import be.rommens.darts.testutils.TestFileLoader;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OrderOfMeritCalculator.class)
public class OrderOfMeritCalculatorTest {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private OrderOfMeritCalculator orderOfMeritCalculator;

    @Test
    void iets() throws IOException {
        File playersTestFile = resourceLoader.getResource("classpath:/services/players.csv").getFile();
        File tournamentResultsTestFile = resourceLoader.getResource("classpath:/services/tournamentResults.csv").getFile();

        List<Player> players = TestFileLoader.loadPlayers(playersTestFile);
        List<TournamentResult> tournamentResults = TestFileLoader.loadTournamentResults(tournamentResultsTestFile);

        List<Pair<Player, Double>> orderOfMerit = orderOfMeritCalculator.calculateMainOrderOfMerit(LocalDate.of(2025, 1, 5), tournamentResults, players);
        Assertions.assertThat(orderOfMerit).containsExactly(
                Pair.of(players.stream().filter(p -> p.getName().equals("Luke Humphries")).findFirst().orElseThrow(), Double.parseDouble("1804.25")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Luke Littler")).findFirst().orElseThrow(), Double.parseDouble("1118.50")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Michael van Gerwen")).findFirst().orElseThrow(), Double.parseDouble("815.50")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Rob Cross")).findFirst().orElseThrow(), Double.parseDouble("551.75")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Stephen Bunting")).findFirst().orElseThrow(), Double.parseDouble("536.00")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Dave Chisnall")).findFirst().orElseThrow(), Double.parseDouble("528.50")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Jonny Clayton")).findFirst().orElseThrow(), Double.parseDouble("494.00")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Damon Heta")).findFirst().orElseThrow(), Double.parseDouble("484.00")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Gerwyn Price")).findFirst().orElseThrow(), Double.parseDouble("480.50")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Chris Dobey")).findFirst().orElseThrow(), Double.parseDouble("480.25")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Nathan Aspinall")).findFirst().orElseThrow(), Double.parseDouble("470.50")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Peter Wright")).findFirst().orElseThrow(), Double.parseDouble("442.50")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Danny Noppert")).findFirst().orElseThrow(), Double.parseDouble("431.75")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Gary Anderson")).findFirst().orElseThrow(), Double.parseDouble("430.50")),
                Pair.of(players.stream().filter(p -> p.getName().equals("James Wade")).findFirst().orElseThrow(), Double.parseDouble("426.75")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Josh Rock")).findFirst().orElseThrow(), Double.parseDouble("405.00")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Michael Smith")).findFirst().orElseThrow(), Double.parseDouble("405.00")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Dimitri Van den Bergh")).findFirst().orElseThrow(), Double.parseDouble("399.25")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Ryan Searle")).findFirst().orElseThrow(), Double.parseDouble("398.25")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Andrew Gilding")).findFirst().orElseThrow(), Double.parseDouble("395.50")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Ross Smith")).findFirst().orElseThrow(), Double.parseDouble("395.00")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Martin Schindler")).findFirst().orElseThrow(), Double.parseDouble("365.00")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Joe Cullen")).findFirst().orElseThrow(), Double.parseDouble("357.25")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Mike De Decker")).findFirst().orElseThrow(), Double.parseDouble("356.00")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Daryl Gurney")).findFirst().orElseThrow(), Double.parseDouble("334.25")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Dirk van Duijvenbode")).findFirst().orElseThrow(), Double.parseDouble("325.75")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Gian van Veen")).findFirst().orElseThrow(), Double.parseDouble("299.00")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Ritchie Edhouse")).findFirst().orElseThrow(), Double.parseDouble("289.25")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Ryan Joyce")).findFirst().orElseThrow(), Double.parseDouble("282.00")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Ricardo Pietreczko")).findFirst().orElseThrow(), Double.parseDouble("280.75")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Brendan Dolan")).findFirst().orElseThrow(), Double.parseDouble("271.50")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Krzysztof Ratajski")).findFirst().orElseThrow(), Double.parseDouble("267.75")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Luke Woodhouse")).findFirst().orElseThrow(), Double.parseDouble("262.75")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Raymond van Barneveld")).findFirst().orElseThrow(), Double.parseDouble("252.75")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Jermaine Wattimena")).findFirst().orElseThrow(), Double.parseDouble("240.25")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Scott Williams")).findFirst().orElseThrow(), Double.parseDouble("220.75")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Gabriel Clemens")).findFirst().orElseThrow(), Double.parseDouble("219.75")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Martin Lukeman")).findFirst().orElseThrow(), Double.parseDouble("202.50")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Cameron Menzies")).findFirst().orElseThrow(), Double.parseDouble("178.00")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Callan Rydz")).findFirst().orElseThrow(), Double.parseDouble("177.00")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Kevin Doets")).findFirst().orElseThrow(), Double.parseDouble("146.50")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Madars Razma")).findFirst().orElseThrow(), Double.parseDouble("144.75")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Mickey Mansell")).findFirst().orElseThrow(), Double.parseDouble("143.25")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Ricky Evans")).findFirst().orElseThrow(), Double.parseDouble("142.25")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Jose de Sousa")).findFirst().orElseThrow(), Double.parseDouble("139.00")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Kim Huybrechts")).findFirst().orElseThrow(), Double.parseDouble("136.00")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Richard Veenstra")).findFirst().orElseThrow(), Double.parseDouble("122.75")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Niels Zonneveld")).findFirst().orElseThrow(), Double.parseDouble("121.25")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Ian White")).findFirst().orElseThrow(), Double.parseDouble("116.25")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Keane Barry")).findFirst().orElseThrow(), Double.parseDouble("115.25")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Jim Williams")).findFirst().orElseThrow(), Double.parseDouble("111.25")),
                Pair.of(players.stream().filter(p -> p.getName().equals("William O\"'Connor")).findFirst().orElseThrow(), Double.parseDouble("107.00")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Florian Hempel")).findFirst().orElseThrow(), Double.parseDouble("103.00")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Matt Campbell")).findFirst().orElseThrow(), Double.parseDouble("103.00")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Wessel Nijman")).findFirst().orElseThrow(), Double.parseDouble("100.00")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Alan Soutar")).findFirst().orElseThrow(), Double.parseDouble("85.50")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Dylan Slevin")).findFirst().orElseThrow(), Double.parseDouble("80.50")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Rob Owen")).findFirst().orElseThrow(), Double.parseDouble("80.25")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Ryan Meikle")).findFirst().orElseThrow(), Double.parseDouble("78.50")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Stephen Burton")).findFirst().orElseThrow(), Double.parseDouble("78.00")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Connor Scutt")).findFirst().orElseThrow(), Double.parseDouble("77.00")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Mensur Suljovic")).findFirst().orElseThrow(), Double.parseDouble("75.25")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Jeffrey de Graaf")).findFirst().orElseThrow(), Double.parseDouble("75.00")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Nick Kenny")).findFirst().orElseThrow(), Double.parseDouble("71.75")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Thibault Tricole")).findFirst().orElseThrow(), Double.parseDouble("57.25")),
                Pair.of(players.stream().filter(p -> p.getName().equals("James Hurrell")).findFirst().orElseThrow(), Double.parseDouble("45.75")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Dom Taylor")).findFirst().orElseThrow(), Double.parseDouble("39.50")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Chris Landman")).findFirst().orElseThrow(), Double.parseDouble("39.25")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Mario Vandenbogaerde")).findFirst().orElseThrow(), Double.parseDouble("31.00")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Rhys Griffin")).findFirst().orElseThrow(), Double.parseDouble("28.00")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Andy Baetens")).findFirst().orElseThrow(), Double.parseDouble("24.50")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Berry van Peer")).findFirst().orElseThrow(), Double.parseDouble("23.50")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Nathan Rafferty")).findFirst().orElseThrow(), Double.parseDouble("22.25")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Steve Lennon")).findFirst().orElseThrow(), Double.parseDouble("22.00")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Radek Szaganski")).findFirst().orElseThrow(), Double.parseDouble("21.75")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Lukas Wenig")).findFirst().orElseThrow(), Double.parseDouble("21.50")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Patrick Geeraets")).findFirst().orElseThrow(), Double.parseDouble("19.00")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Matthew Dennant")).findFirst().orElseThrow(), Double.parseDouble("19.00")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Darren Beveridge")).findFirst().orElseThrow(), Double.parseDouble("18.75")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Jitse Van der Wal")).findFirst().orElseThrow(), Double.parseDouble("18.25")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Benjamin Reus")).findFirst().orElseThrow(), Double.parseDouble("18.00")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Danny Lauby")).findFirst().orElseThrow(), Double.parseDouble("17.00")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Robert Grundy")).findFirst().orElseThrow(), Double.parseDouble("16.50")),
                Pair.of(players.stream().filter(p -> p.getName().equals("George Killington")).findFirst().orElseThrow(), Double.parseDouble("14.50")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Owen Bates")).findFirst().orElseThrow(), Double.parseDouble("14.00")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Brett Claydon")).findFirst().orElseThrow(), Double.parseDouble("13.75")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Adam Hunt")).findFirst().orElseThrow(), Double.parseDouble("13.50")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Martijn Dragt")).findFirst().orElseThrow(), Double.parseDouble("13.50")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Haupai Puha")).findFirst().orElseThrow(), Double.parseDouble("12.50")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Jelle Klaasen")).findFirst().orElseThrow(), Double.parseDouble("11.75")),
                Pair.of(players.stream().filter(p -> p.getName().equals("William Borland")).findFirst().orElseThrow(), Double.parseDouble("11.50")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Tim Wolters")).findFirst().orElseThrow(), Double.parseDouble("7.50")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Joshua Richardson")).findFirst().orElseThrow(), Double.parseDouble("7.50")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Michele Turetta")).findFirst().orElseThrow(), Double.parseDouble("6.00")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Jules van Dongen")).findFirst().orElseThrow(), Double.parseDouble("4.00")),

                Pair.of(players.stream().filter(p -> p.getName().equals("Adam Lipscombe")).findFirst().orElseThrow(), Double.parseDouble("0")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Adam Paxton")).findFirst().orElseThrow(), Double.parseDouble("0")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Adam Warner")).findFirst().orElseThrow(), Double.parseDouble("0")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Andy Boulton")).findFirst().orElseThrow(), Double.parseDouble("0")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Bradley Brooks")).findFirst().orElseThrow(), Double.parseDouble("0")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Cam Crabtree")).findFirst().orElseThrow(), Double.parseDouble("0")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Christian Kist")).findFirst().orElseThrow(), Double.parseDouble("0")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Cor Dekker")).findFirst().orElseThrow(), Double.parseDouble("0")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Darryl Pilgrim")).findFirst().orElseThrow(), Double.parseDouble("0")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Dennie Olde Kalter")).findFirst().orElseThrow(), Double.parseDouble("0")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Dominik Gruellich")).findFirst().orElseThrow(), Double.parseDouble("0")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Greg Ritchie")).findFirst().orElseThrow(), Double.parseDouble("0")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Jim Long")).findFirst().orElseThrow(), Double.parseDouble("0")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Justin Hood")).findFirst().orElseThrow(), Double.parseDouble("0")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Kai Gotthardt")).findFirst().orElseThrow(), Double.parseDouble("0")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Karel Sedlacek")).findFirst().orElseThrow(), Double.parseDouble("0")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Leon Weber")).findFirst().orElseThrow(), Double.parseDouble("0")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Maik Kuivenhoven")).findFirst().orElseThrow(), Double.parseDouble("0")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Marvin van Velzen")).findFirst().orElseThrow(), Double.parseDouble("0")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Max Hopp")).findFirst().orElseThrow(), Double.parseDouble("0")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Maximilian Czerwinski")).findFirst().orElseThrow(), Double.parseDouble("0")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Niko Springer")).findFirst().orElseThrow(), Double.parseDouble("0")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Oskar Lukasiak")).findFirst().orElseThrow(), Double.parseDouble("0")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Pero Ljubic")).findFirst().orElseThrow(), Double.parseDouble("0")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Rusty-Jake Rodriguez")).findFirst().orElseThrow(), Double.parseDouble("0")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Sebastian Bialecki")).findFirst().orElseThrow(), Double.parseDouble("0")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Stefaan Henderyck")).findFirst().orElseThrow(), Double.parseDouble("0")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Tavis Dudeney")).findFirst().orElseThrow(), Double.parseDouble("0")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Thomas Lovely")).findFirst().orElseThrow(), Double.parseDouble("0")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Tom Bissell")).findFirst().orElseThrow(), Double.parseDouble("0")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Tytus Kanik")).findFirst().orElseThrow(), Double.parseDouble("0")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Viktor Tingstrom")).findFirst().orElseThrow(), Double.parseDouble("0")),
                Pair.of(players.stream().filter(p -> p.getName().equals("Wesley Plaisier")).findFirst().orElseThrow(), Double.parseDouble("0"))
        );
    }

}
