package be.rommens.darts.scraper;

import static java.util.Objects.requireNonNull;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class ScraperService {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    //For all players on dartsorakel and their internal ID's: go to https://app.dartsorakel.com/stats/player, and check result of networkcall https://app.dartsorakel.com/api/stats/player?dateFrom=2023-12-16&dateTo=2024-12-16&rankKey=25&organStat=All&tourns=All&minMatches=200&tourCardYear=&showStatsBreakdown=0&_=1734342354735

    public void scrapePlayersAndPriceMoney() throws IOException {
        Map<String, Integer> playerlink = parseDartsOrakelPlayers();

        Document doc = SSLHelper.getConnection("https://www.dartsrankings.com/").get();
        //Element table = doc.selectFirst("table#tablesingle|tablechangelive");
        Element table = doc.selectFirst("table#tableliveETChange");
        Elements rows = table.select("tr");
        List<Player> players = new ArrayList<>();
        for(int i = 1; i < rows.size(); i++) {
            Element row = rows.get(i);
            Elements cols = row.select("td");
            String rank = cols.get(0).text();
            String name = cols.get(2).text();
            var link = cols.get(2).select("a").attr("href");
            String priceMoney = cols.get(3).text();
            Detail detail = breakdown(link);

            //stats
            if (!playerlink.containsKey(name.toUpperCase())) {
                throw new IllegalArgumentException(String.format("Player {} not found", name));
            }
            Document docPlayer = SSLHelper.getConnection("https://app.dartsorakel.com/player/stats/" + playerlink.get(name.toUpperCase())).get();
            String country = requireNonNull(docPlayer.select("img[src*=/flags/]").last()).attr("alt");
            String hometownAndBirthDate = ((TextNode)docPlayer.select("span.svg-icon-4").get(1).parent().childNode(2)).text();
            String hometown = hometownAndBirthDate.split(",")[0].strip();
            String birthday = hometownAndBirthDate.split(",")[1].strip();
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
                    accuracyBull = Double.parseDouble(e.parent().parent().children().get(1).text().replace("%", ""));
                }
                if(e.text().strip().equals("Checkout Pcnt 3rd Dart")) {
                    accuracyDouble3rdDart = Double.parseDouble(e.parent().parent().children().get(1).text().replace("%", ""));
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
                    startingDouble = Double.parseDouble(e.parent().parent().children().get(1).text().replace("%", ""));
                }
            }

            players.add(new Player(rank, name, country, hometown, birthday, average, first9Average, startingDouble, accuracyBull, accuracyDouble, accuracyDouble3rdDart, accuracyTreble, priceMoney, detail.hasTourCard(), detail.breakdowns));
        }
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(players);
        System.out.println(json);
    }

    private Map<String, Integer> parseDartsOrakelPlayers() throws IOException {
        DartsorakelPlayerInput input = MAPPER.readValue(ScraperService.class.getClassLoader().getResource("scraper/dartsorakel_players.json"), DartsorakelPlayerInput.class);
        return input.data().stream().collect(Collectors.toMap(x -> x.playerName().toUpperCase(), DartsoraklPlayerDetail::playerKey));
    }

    private Detail breakdown(String link) throws IOException {
        String url = String.format("https://www.dartsrankings.com/%s&oom=big&sort=asc", link);
        Document doc = SSLHelper.getConnection(url).get();
        Element table = doc.selectFirst("table.breakdown");
        Elements rows = table.select("tr");
        List<Breakdown> breakdowns = new ArrayList<>();
        for(int i = 1; i < rows.size(); i++) {
            Element row = rows.get(i);
            if (!row.hasAttr("style") || !row.attr("style").contains("background:#ffcccc")) {
                Elements cols = row.select("td");
                String date = cols.get(0).text();
                String tournament = cols.get(1).text();
                String money = cols.get(2).text();
                breakdowns.add(new Breakdown(date, tournament, money));
            }
        }

        Elements tc = doc.select("span.tc");
        return new Detail(!tc.isEmpty(), breakdowns);
    }


    static class SSLHelper {

        static public Connection getConnection(String url){
            return Jsoup.connect(url).sslSocketFactory(SSLHelper.socketFactory());
        }

        static private SSLSocketFactory socketFactory() {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            try {
                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                SSLSocketFactory result = sslContext.getSocketFactory();

                return result;
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                throw new RuntimeException("Failed to create a SSL socket factory", e);
            }
        }
    }

    record Detail(boolean hasTourCard, List<Breakdown> breakdowns) {}
    record Breakdown(String date, String tournament, String money) {}
    record Player(String rank,
                  String name,
                  String country,
                  String hometown,
                  String birthday,
                  double average,
                  double first9avg,
                  double startingDouble,
                  double accuracyBull,
                  double accuracyDouble,
                  double accuracyDouble3rdDart,
                  double accuracyTreble,
                  String priceMoney,
                  boolean hasTourCard,
                  List<Breakdown> breakdowns) {}

    record DartsorakelPlayerInput(List<DartsoraklPlayerDetail> data){}
    record DartsoraklPlayerDetail(@JsonAlias("player_key") int playerKey, @JsonAlias("player_name") String playerName) {}

}
