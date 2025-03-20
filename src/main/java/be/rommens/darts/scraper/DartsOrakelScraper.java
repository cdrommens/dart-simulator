package be.rommens.darts.scraper;

import be.rommens.darts.scraper.core.DartsOrakelScrapeResult;
import be.rommens.darts.scraper.core.OrderOfMeritScrapeResult;
import be.rommens.darts.scraper.core.SSLHelper;
import be.rommens.darts.scraper.core.in.DartsOrakelPlayerDetail;
import be.rommens.darts.scraper.core.in.DartsOrakelPlayerInput;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.Objects.requireNonNull;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class DartsOrakelScraper {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final Map<String, String> NORMALIZED_MAPPING = new HashMap<>();

    private final Map<OrderOfMeritScrapeResult, Integer> linkedPlayers = new HashMap<>();

    public DartsOrakelScraper() {
        NORMALIZED_MAPPING.put("WILLIAM O'CONNOR", "WILLIAM O&#039;CONNOR");
        NORMALIZED_MAPPING.put("MENSUR SULJOVIC", "MENSUR SULJOVIĆ");
        NORMALIZED_MAPPING.put("RADEK SZAGANSKI", "RADOSLAW SZAGANSKI");
        NORMALIZED_MAPPING.put("DANNY LAUBY", "DANIEL LAUBY JR");
        NORMALIZED_MAPPING.put("KAREL SEDLACEK", "KAREL SEDLÁČEK");
        NORMALIZED_MAPPING.put("BORIS KRCMAR", "BORIS KRČMAR");
        NORMALIZED_MAPPING.put("ROB OWEN", "ROBERT OWEN");
        NORMALIZED_MAPPING.put("TOM SYKES", "THOMAS SYKES");
        NORMALIZED_MAPPING.put("PERO LJUBIC", "PERO LJUBIĆ");
    }

    @SneakyThrows
    public void scrape(List<OrderOfMeritScrapeResult> orderOfMeritScrapeResults) {
        if (linkOrderOfMeritPlayersWithDartsOrakelPlayers(orderOfMeritScrapeResults)) {
            List<DartsOrakelScrapeResult> results = linkedPlayers.entrySet().stream()
                    .map((entry) -> scrapePlayer(entry.getValue(), entry.getKey().name()))
                    .toList();
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(results);
            BufferedWriter writer = new BufferedWriter(new FileWriter("dartsorakel_player_stats.json"));
            writer.write(json);
            writer.close();
        }
    }

    private boolean linkOrderOfMeritPlayersWithDartsOrakelPlayers(List<OrderOfMeritScrapeResult> orderOfMeritScrapeResults) throws IOException {
        boolean allLinked = true;
        Map<String, Integer> dartsorakelPlayerKeys = parseDartsOrakelPlayers();
        for (OrderOfMeritScrapeResult orderOfMeritScrapeResult : orderOfMeritScrapeResults) {
            if (dartsorakelPlayerKeys.containsKey(orderOfMeritScrapeResult.name().toUpperCase())) {
                linkedPlayers.put(orderOfMeritScrapeResult, dartsorakelPlayerKeys.get(orderOfMeritScrapeResult.name().toUpperCase()));
            } else if (NORMALIZED_MAPPING.containsKey(orderOfMeritScrapeResult.name().toUpperCase())) {
                linkedPlayers.put(orderOfMeritScrapeResult,
                        dartsorakelPlayerKeys.get(NORMALIZED_MAPPING.get(orderOfMeritScrapeResult.name().toUpperCase())));
            } else {
                log.info(orderOfMeritScrapeResult.name() + " not found");
                allLinked = false;
            }
        }
        return allLinked;
    }

    private Map<String, Integer> parseDartsOrakelPlayers() throws IOException {
        DartsOrakelPlayerInput input = MAPPER.readValue(DartsOrakelScraper.class.getClassLoader().getResource("scraper/dartsorakel_players.json"), DartsOrakelPlayerInput.class);
        return input.data().stream().collect(Collectors.toMap(x -> x.playerName().toUpperCase(), DartsOrakelPlayerDetail::playerKey));
    }

    @SneakyThrows
    private DartsOrakelScrapeResult scrapePlayer(int key, String name) {
        Document docPlayer = SSLHelper.getConnection("https://app.dartsorakel.com/player/stats/" + key).get();
        String country = requireNonNull(docPlayer.select("img[src*=/flags/]").last()).attr("alt");
        Elements hometownAndBirthDateNode = docPlayer.select("span.svg-icon-4");
        String hometown = "";
        String birthday = "";
        if (hometownAndBirthDateNode.size() > 1) {
            String hometownAndBirthDate = ((TextNode)docPlayer.select("span.svg-icon-4").get(1).parent().childNode(2)).text();
            if (hometownAndBirthDate.contains(",")) {
                String[] hometownAndBirthDateSplit = hometownAndBirthDate.split(",");
                hometown = hometownAndBirthDate.split(",")[0].strip();
                if (hometownAndBirthDateSplit.length == 2) {
                    birthday = hometownAndBirthDate.split(",")[1].strip();
                }
            } else {
                String[] birthDate = hometownAndBirthDate.split("\\(");
                birthday = birthDate[0].strip();
            }
        }
        double average = 0.0;
        double first9Average = 0.0;
        for(Element e : docPlayer.select("a:contains(Averages)")) {
            if(e.text().strip().equals("First 9 Averages")) {
                first9Average = Double.parseDouble(e.parent().parent().children().get(1).text());
            }
            if(e.text().strip().equals("Averages")) {
                average = Double.parseDouble(e.parent().parent().children().get(1).text());
            }
        }
        double accuracyDouble = 0.0;
        double accuracyBull = 0.0;
        double accuracyDouble3rdDart = 0.0;
        for(Element e : docPlayer.select("a:contains(checkout)")) {
            if(e.text().strip().equals("Checkout Pcnt")) {
                accuracyDouble = Double.parseDouble(e.parent().parent().children().get(1).text().replace("%", ""));
            }
            if(e.text().strip().equals("Bullseye Checkout Pcnt")) {
                String start = e.parent().parent().children().get(1).text().replace("%", "");
                if (StringUtils.isNotEmpty(start)) {
                    accuracyBull = Double.parseDouble(start);
                } else {
                    accuracyBull = accuracyDouble;
                }
            }
            if(e.text().strip().equals("Checkout Pcnt 3rd Dart")) {
                String start = e.parent().parent().children().get(1).text().replace("%", "");
                if (StringUtils.isNotEmpty(start)) {
                    accuracyDouble3rdDart = Double.parseDouble(start);
                } else {
                    accuracyDouble3rdDart = accuracyDouble;
                }
            }
        }
        double accuracyTreble = 0.0;
        for(Element e : docPlayer.select("a:contains(treble)")) {
            if(e.text().strip().equals("Treble 20 Hit Pcnt")) {
                accuracyTreble = Double.parseDouble(e.parent().parent().children().get(1).text().replace("%", ""));
            }
        }
        double startingDouble = 0.0;
        for(Element e : docPlayer.select("a:contains(starting)")) {
            if(e.text().strip().equals("Starting Double Hit Pcnt")) {
                String start = e.parent().parent().children().get(1).text().replace("%", "");
                if (StringUtils.isNotEmpty(start)) {
                    startingDouble = Double.parseDouble(start);
                } else {
                    startingDouble = accuracyDouble;
                }
            }
        }
        return new DartsOrakelScrapeResult(name, country, hometown, birthday, average, first9Average, startingDouble, accuracyBull, accuracyDouble, accuracyDouble3rdDart, accuracyTreble);
    }
}
