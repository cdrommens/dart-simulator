package be.rommens.darts.services;

import be.rommens.darts.database.domain.Player;
import be.rommens.darts.database.domain.TournamentResult;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

@Component
public class OrderOfMeritCalculator {

    /**
     * Rules:
     * - calculated on prize money won by players over a 104-week period
     * - No Premier Event shall count three times
     * - In case of ties:
     *    - countback rule : cumulative money won by a player in the previous four eligible Events
     *    - still tied : go further back until a separation is found
     *    - no difference found : alphabetical
     * @param endDate : end date of the 104 week period
     * @param tournamentResults : all results
     */
    public List<Pair<Player, Double>> calculateMainOrderOfMerit(LocalDate endDate, List<TournamentResult> tournamentResults, List<Player> players) {
        LocalDate startDate = endDate.minusWeeks(104);
        Map<String, String> playersMap = players.stream()
                .collect(toMap(Player::getId, Player::getName));
        Map<String, List<TournamentResult>> resultsPerPlayer = tournamentResults.stream()
                .filter(result -> startDate.isBefore(result.getDate()))
                .filter(result -> endDate.isAfter(result.getDate()))
                .collect(groupingBy(TournamentResult::getPlayerId));
        Map<String, Double> sumPerPlayer = tournamentResults.stream()
                .filter(result -> startDate.isBefore(result.getDate()))
                .filter(result -> endDate.isAfter(result.getDate()))
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
        //insertion sort
        Pair<String, Double>[] sortedArray = sorted.toArray(new Pair[0]);
        int n = sortedArray.length;
        for (int i = 1; i < n; i++) {
            Pair<String, Double> key = sortedArray[i];
            int j = i - 1;

            while (j >= 0) {
                // Sort on prize money
                if (sortedArray[j].getRight() > key.getRight()) {
                    sortedArray[j + 1] = sortedArray[j];
                    j = j - 1;
                } else if (Objects.equals(sortedArray[j].getRight(), key.getRight())) {
                    // Sort on previous results if tied
                    int limit = 4;
                    double sumSortedArray = getSum(resultsPerPlayer.computeIfAbsent(sortedArray[j].getLeft(), s ->List.of()), limit);
                    double sumKey = getSum(resultsPerPlayer.computeIfAbsent(key.getLeft(), s ->List.of()), limit);
                    while (sumSortedArray == sumKey && ( limit < resultsPerPlayer.get(sortedArray[j].getLeft()).size() || limit < resultsPerPlayer.get(key.getLeft()).size())) {
                        limit++;
                        sumSortedArray = getSum(resultsPerPlayer.get(sortedArray[j].getLeft()), limit);
                        sumKey = getSum(resultsPerPlayer.get(key.getLeft()), limit);
                    }
                    if (sumSortedArray > sumKey) {
                        sortedArray[j + 1] = sortedArray[j];
                        j = j - 1;
                    } else if (sumSortedArray == sumKey) {
                        // sort on name alphabetically
                        if (playersMap.get(sortedArray[j].getLeft()).compareTo(playersMap.get(key.getLeft())) < 0) {
                            sortedArray[j + 1] = sortedArray[j];
                            j = j - 1;
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
            sortedArray[j + 1] = key;
        }
        return Arrays.asList(sortedArray).reversed();
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