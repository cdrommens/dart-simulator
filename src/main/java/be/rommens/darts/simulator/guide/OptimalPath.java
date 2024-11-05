package be.rommens.darts.simulator.guide;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import be.rommens.darts.simulator.ThrowSimulator;
import be.rommens.darts.simulator.model.Dart;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class OptimalPath {

    private static final List<Integer> BOGEY_NUMBERS_3_DARTS = List.of(349, 348, 346, 345, 343, 342, 339, 169, 168, 166, 165, 163, 162, 159);
    private static final List<Integer> BOGEY_NUMBERS_1_DART = List.of(409, 408, 406, 405, 403, 402, 399,  229, 228, 226, 225, 223, 222, 219);

    private final Map<Integer, Entry> optimalPath;

    public OptimalPath() throws URISyntaxException, IOException {
        optimalPath = Files.readAllLines(Path.of(ThrowSimulator.class.getClassLoader().getResource("optimal_path.csv").toURI()))
                .stream()
                .skip(1)
                .map(line -> line.split(";", -1))
                .map(path -> Entry.createFromString(path[0], path[1],  path[2], path[3]))
                .collect(toMap(entry -> entry.score, identity()));
        fill();
    }

    public void fill() {
        optimalPath.keySet().stream()
                .sorted(Comparator.reverseOrder())
                        .forEach(key -> {
                            optimalPath.get(key).secondDart = decideAimSecondDart(key);
                            optimalPath.get(key).thirdDart = decideAimThirdDart(key);
                        });
    }

    public int decideAim(Dart dart, int s) {
        return switch (dart) {
            case FIRST -> optimalPath.get(s).getFirstDart();
            case SECOND -> optimalPath.get(s).getSecondDart();
            case THIRD -> optimalPath.get(s).getThirdDart();
        };
    }

    private int decideAimSecondDart(int startScore) {
        if (optimalPath.get(startScore).secondDart != 0) {
            return optimalPath.get(startScore).secondDart;
        }
        return decideOptimalAim(startScore, BOGEY_NUMBERS_1_DART);
    }

    private int decideAimThirdDart(int startScore) {
        if (optimalPath.get(startScore).thirdDart != 0) {
            return optimalPath.get(startScore).thirdDart;
        }
        return decideOptimalAim(startScore, BOGEY_NUMBERS_3_DARTS);
    }

    public void write() {
        optimalPath.values().forEach(System.out::println);
    }

    private int decideOptimalAim(int startScore, List<Integer> bogeyNumbers) {
        int remaingScoreIf20 = startScore - 20;
        int remaingScoreIf60 = startScore - 60;
        int remaingScoreIf19 = startScore - 19;
        int remaingScoreIf57 = startScore - 47;
        int remaingScoreIf18 = startScore - 18;
        int remaingScoreIf54 = startScore - 54;
        if (!bogeyNumbers.contains(remaingScoreIf20) && !bogeyNumbers.contains(remaingScoreIf60)) {
            return 60;
        }
        if (!bogeyNumbers.contains(remaingScoreIf19) && !bogeyNumbers.contains(remaingScoreIf57)) {
            return 57;
        }
        if (!bogeyNumbers.contains(remaingScoreIf18) && !bogeyNumbers.contains(remaingScoreIf54)) {
            return 54;
        }
        throw new IllegalStateException("Didn't found a score");
    }

    static class Entry {
        private int score;
        private int firstDart;
        private int secondDart;
        private int thirdDart;

        public static Entry createFromString(String score, String firstDart, String secondDart, String thirdDart) {
            Entry entry = new Entry();
            entry.score = Integer.parseInt(score);
            if (!firstDart.isBlank()) {
                entry.firstDart = Integer.parseInt(firstDart);
            }
            if (!secondDart.isBlank()) {
                entry.secondDart = Integer.parseInt(secondDart);
            }
            if (!thirdDart.isBlank()) {
                entry.thirdDart = Integer.parseInt(thirdDart);
            }
            return entry;
        }

        public int getFirstDart() {
            return firstDart;
        }

        public void setFirstDart(int firstDart) {
            this.firstDart = firstDart;
        }

        public int getSecondDart() {
            return secondDart;
        }

        public void setSecondDart(int secondDart) {
            this.secondDart = secondDart;
        }

        public int getThirdDart() {
            return thirdDart;
        }

        public void setThirdDart(int thirdDart) {
            this.thirdDart = thirdDart;
        }

        @Override
        public String toString() {
            return String.format("%S;%S;%s;%s", score, firstDart, secondDart, thirdDart);
        }
    }

}
