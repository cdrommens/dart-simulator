package be.rommens.darts.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Turn {

    private final List<Throw> scores = new ArrayList<>();
    private final Integer startScore;

    public Turn(Integer startScore) {
        this.startScore = startScore;
    }

    public void addThrow(Throw score) {
        scores.add(score);
    }

    public Stream<Throw> getThrows() {
        return scores.stream();
    }

    public void markAsBusted() {
        for (Throw score : scores) {
            Throw replace = scores.get(scores.indexOf(score));
            scores.set(scores.indexOf(score), new Throw(replace.score(), replace.isFinishingThrow(), replace.isFinishingShot(), true));
        }
    }

    public int getScoreLeft() {
        return startScore - scores.stream().filter(s -> !s.isBusted()).map(Throw::score).reduce(0, Integer::sum);
    }

    public boolean is180() {
        return scores.stream().allMatch(t -> t.score() == 60);
    }

    public boolean is140() {
        return scores.stream()
                .map(Throw::score)
                .reduce(0, Integer::sum) == 140;
    }

    public Integer getStartScore() {
        return startScore;
    }

    @Override
    public String toString() {
        return "Turn{" +
                "scores=" + scores +
                ", startScore=" + startScore +
                '}';
    }
}
