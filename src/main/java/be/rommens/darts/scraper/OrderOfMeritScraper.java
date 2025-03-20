package be.rommens.darts.scraper;

import be.rommens.darts.scraper.core.Breakdown;
import be.rommens.darts.scraper.core.Detail;
import be.rommens.darts.scraper.core.OrderOfMeritScrapeResult;
import be.rommens.darts.scraper.core.SSLHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class OrderOfMeritScraper {

    public void scrape() throws IOException {
        Document doc = SSLHelper.getConnection("https://www.dartsrankings.com/").get();
        Element table = doc.selectFirst("table#tablesingle");
        //Element table = doc.selectFirst("table#tableliveETChange");
        Elements rows = table.select("tr");
        List<OrderOfMeritScrapeResult> orderOfMerits = new ArrayList<>();
        for(int i = 1; i < rows.size(); i++) {
            Element row = rows.get(i);
            Elements cols = row.select("td");
            String name = cols.get(2).text();
            log.info("Start " + name);
            var link = cols.get(2).select("a").attr("href");
            Detail detail = breakdown(link);
            orderOfMerits.add(new OrderOfMeritScrapeResult(name, detail));
            log.info("End " + name);
        }
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(orderOfMerits);
        BufferedWriter writer = new BufferedWriter(new FileWriter("order_of_merit.json"));
        writer.write(json);
        writer.close();
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
        Element recentlyDropped = doc.select("table.breakdown").get(1);
        Elements rowsRecentlyDropped = recentlyDropped.select("tr");
        List<Breakdown> breakdownsRecentlyDropped = new ArrayList<>();
        for(int i = 1; i < rowsRecentlyDropped.size(); i++) {
            Element row = rowsRecentlyDropped.get(i);
            if (!row.hasAttr("style") || !row.attr("style").contains("background:#ffcccc")) {
                Elements cols = row.select("td");
                String date = cols.get(0).text();
                String tournament = cols.get(1).text();
                String money = cols.get(2).text();
                breakdownsRecentlyDropped.add(new Breakdown(date, tournament, money));
            }
        }
        List<Breakdown> allBreakdowns = new ArrayList<>();
        allBreakdowns.addAll(breakdownsRecentlyDropped);
        allBreakdowns.addAll(breakdowns);
        allBreakdowns.sort(Comparator.comparing(x -> x.date()));
        Elements tc = doc.select("span.tc");
        return new Detail(!tc.isEmpty(), allBreakdowns);
    }
}
