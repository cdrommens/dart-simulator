package be.rommens.darts.simulator;

import java.util.ArrayList;
import java.util.List;

public class Turn {

    private List<SingleScore> scores = new ArrayList<>();

    public void addSingleScore(SingleScore score) {
        scores.add(score);
    }

}
