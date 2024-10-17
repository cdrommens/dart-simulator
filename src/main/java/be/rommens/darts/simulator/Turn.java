package be.rommens.darts.simulator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class Turn {

    private List<SingleScore> scores = new ArrayList<>();

    public void addSingleScore(SingleScore score) {
        scores.add(score);
    }

    public Stream<SingleScore> getScores() {
        return scores.stream();
    }

}
