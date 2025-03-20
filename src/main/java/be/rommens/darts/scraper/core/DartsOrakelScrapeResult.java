package be.rommens.darts.scraper.core;

public record DartsOrakelScrapeResult(String name,
                                      String country,
                                      String hometown,
                                      String birthday,
                                      double average,
                                      double first9avg,
                                      double startingDouble,
                                      double accuracyBull,
                                      double accuracyDouble,
                                      double accuracyDouble3rdDart,
                                      double accuracyTreble) {

}
