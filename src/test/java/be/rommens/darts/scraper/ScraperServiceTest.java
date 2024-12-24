package be.rommens.darts.scraper;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ScraperServiceTest {

    @Autowired
    ScraperService scraperService;

    @Test
    void scrapePlayersAndPriceMoneyTest() throws IOException {
        scraperService.scrapePlayersAndPriceMoney();
    }

}
