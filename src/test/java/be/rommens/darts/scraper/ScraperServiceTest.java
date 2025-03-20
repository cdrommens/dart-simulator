package be.rommens.darts.scraper;

import be.rommens.darts.scraper.core.OrderOfMeritScrapeResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ScraperServiceTest {

    @Autowired
    OrderOfMeritScraper orderOfMeritScraper;

    @Autowired
    DartsOrakelScraper dartsOrakelScraper;

    @Test
    void scrapeOrderOfMerits() throws IOException {
        orderOfMeritScraper.scrape();
    }

    @Test
    void scrapeDartsOrakel() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<OrderOfMeritScrapeResult> result = mapper
                .readerForListOf(OrderOfMeritScrapeResult.class)
                .readValue(ScraperServiceTest.class.getClassLoader().getResource("scraper/order_of_merit.json"));
        dartsOrakelScraper.scrape(result);
    }

}
