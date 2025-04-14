package be.rommens.darts.database.loader;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

public class SortTest {

    @Test
    void sort() {
        var res = List.of(
                Pair.of("1", 1),
                Pair.of("2", 2),
                Pair.of("3", 3),
                Pair.of("4", 4),
                Pair.of("5", 5),
                Pair.of("6", 6),
                Pair.of("0", 0),
                Pair.of("0", 0),
                Pair.of("0", 0),
                Pair.of("0", 0),
                Pair.of("0", 0),
                Pair.of("0", 0)
        );
        sort(res);
    }

    private List<Pair<String, Integer>> sort(List<Pair<String, Integer>> sorted) {
        //insertion sort
        Pair<String, Integer>[] sortedArray = sorted.toArray(new Pair[0]);
        int n = sortedArray.length;
        for (int i = 1; i < n; i++) {
            Pair<String, Integer> key = sortedArray[i];
            int j = i - 1;

            /* Move elements of arr[0..i-1], that are
               greater than key, to one position ahead
               of their current position */
            while (j >= 0 && sortedArray[j].getRight() > key.getRight()) {
                sortedArray[j + 1] = sortedArray[j];
                j = j - 1;
            }
            sortedArray[j + 1] = key;
        }
        return Arrays.asList(sortedArray);
    }

}
