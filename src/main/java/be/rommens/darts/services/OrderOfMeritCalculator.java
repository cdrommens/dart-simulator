package be.rommens.darts.services;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import be.rommens.darts.database.domain.Player;
import be.rommens.darts.database.domain.TournamentResult;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

@Component
public class OrderOfMeritCalculator {

    /**
     * Rules:
     * - calculated on prize money won by players over a 104-week period in Premier Events
     * - No Premier Event shall count three times
     * - In case of ties:
     *    - countback rule : cumulative money won by a player in the previous four eligible Events
     *    - still tied : go further back until a separation is found
     *    - no difference found : alphabetical
     * @param endDate : end date of the 104 week period
     * @param tournamentResults : all results
     * @param players : all players
     */
    public List<Pair<Player, Double>> calculateMainOrderOfMerit(LocalDate endDate, List<TournamentResult> tournamentResults, List<Player> players) {
        return calculateOrderOfMerit(
                endDate,
                tournamentResults,
                players,
                104,
                result -> true);
    }

    /**
     * Rules:
     * - calculated on prize money won by players over a 52-week period in European Tour and Players Championship Events
     * - In case of ties:
     *    - countback rule : cumulative money won by a player in the previous four eligible Events
     *    - still tied : go further back until a separation is found
     *    - no difference found : alphabetical
     * @param endDate : end date of the 104 week period
     * @param tournamentResults : all results
     * @param players : all players
     */
    public List<Pair<Player, Double>> calculateProTourOrderOfMerit(LocalDate endDate, List<TournamentResult> tournamentResults, List<Player> players) {
        return calculateOrderOfMerit(
                endDate,
                tournamentResults,
                players,
                52,
                TournamentResult::isProTourOom);
    }

    /**
     * Rules:
     * - calculated on prize money won by players over a @rollingPeriod period
     * - No Premier Event shall count three times
     * - In case of ties:
     *    - countback rule : cumulative money won by a player in the previous four eligible Events
     *    - still tied : go further back until a separation is found
     *    - no difference found : alphabetical
     * @param endDate : end date of the rolling period
     * @param tournamentResults : all results
     * @param players : all players
     * @param rollingPeriod : rolling period in weeks
     * @param filter : extra filter for filtering out results
     */
    private List<Pair<Player, Double>> calculateOrderOfMerit(
            LocalDate endDate,
            List<TournamentResult> tournamentResults,
            List<Player> players,
            int rollingPeriod,
            Predicate<TournamentResult> filter) {
        LocalDate startDate = endDate.minusWeeks(rollingPeriod);

        Map<String, String> playersMap = players.stream()
                .collect(toMap(Player::getId, Player::getName));

        Map<String, List<TournamentResult>> resultsPerPlayer = tournamentResults.stream()
                .filter(result -> startDate.isBefore(result.getDate()))
                .filter(result -> endDate.isAfter(result.getDate()))
                .filter(filter)
                .collect(groupingBy(TournamentResult::getPlayerId));

        Map<String, Double> sumPerPlayer = tournamentResults.stream()
                .filter(result -> startDate.isBefore(result.getDate()))
                .filter(result -> endDate.isAfter(result.getDate()))
                .filter(filter)
                .collect(groupingBy(TournamentResult::getPlayerId, summingDouble(TournamentResult::getMoney)));

        List<Pair<String, Double>> sorted = sumPerPlayer.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(entry -> Pair.of(entry.getKey(), entry.getValue()))
                .collect(toList());

        //add tour card holders with no prize money yet
        players.stream()
                .filter(Player::isHasTourCard)
                .filter(player -> sorted.stream().map(Pair::getLeft).noneMatch(p -> p.equals(player.getId())))
                .forEach(player -> sorted.add(Pair.of(player.getId(), 0.0)));
        List<Pair<String, Double>> finalList = sort(sorted, resultsPerPlayer, playersMap);

        return finalList.stream()
                .map(pair -> Pair.of(
                        players.stream().filter(p -> p.getId().equals(pair.getLeft())).findFirst().orElseThrow(() -> new NoSuchElementException(pair.getLeft())),
                        pair.getRight()))
                .collect(toList());
    }

    private List<Pair<String, Double>> sort(
            List<Pair<String, Double>> sorted,
            Map<String, List<TournamentResult>> resultsPerPlayer,
            Map<String, String> playersMap) {
        return sorted.stream()
                .sorted((pair1, pair2) -> {
                    // Compare by prize money
                    int prizeComparison = Double.compare(pair2.getRight(), pair1.getRight());
                    if (prizeComparison != 0) {
                        return prizeComparison;
                    }

                    // Compare by previous results if prize money is tied
                    int limit = 4;
                    double sum1 = getSum(resultsPerPlayer.getOrDefault(pair1.getLeft(), List.of()), limit);
                    double sum2 = getSum(resultsPerPlayer.getOrDefault(pair2.getLeft(), List.of()), limit);

                    while (Double.compare(sum1, sum2) == 0 &&
                            (limit < resultsPerPlayer.getOrDefault(pair1.getLeft(), List.of()).size() ||
                                    limit < resultsPerPlayer.getOrDefault(pair2.getLeft(), List.of()).size())) {
                        limit++;
                        sum1 = getSum(resultsPerPlayer.get(pair1.getLeft()), limit);
                        sum2 = getSum(resultsPerPlayer.get(pair2.getLeft()), limit);
                    }

                    if (Double.compare(sum1, sum2) != 0) {
                        return Double.compare(sum2, sum1);
                    }

                    // Compare alphabetically if still tied
                    return playersMap.get(pair1.getLeft()).compareTo(playersMap.get(pair2.getLeft()));
                })
                .collect(Collectors.toList());
    }

    private static Double getSum(List<TournamentResult> results, int limit) {
        return results.stream()
                .sorted(Comparator.comparing(TournamentResult::getDate))
                .toList()
                .reversed()
                .stream()
                .limit(limit).mapToDouble(TournamentResult::getMoney)
                .sum();
    }

}