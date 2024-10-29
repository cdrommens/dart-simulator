package be.rommens.darts.simulator.model;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class Turn {

    private final Integer startScore;
    private Throw firstThrow;
    private Throw secondThrow;
    private Throw thirdThrow;
    private boolean isBusted = false;

    public Turn(Integer startScore) {
        this.startScore = startScore;
    }

    public void addThrow(Dart dart, Throw throwToAdd) {
        switch (dart) {
            case FIRST:
                firstThrow = throwToAdd;
                break;
            case SECOND:
                secondThrow = throwToAdd;
                break;
            case THIRD:
                thirdThrow = throwToAdd;
                break;
        }
    }

    public void markAsBusted(Dart dart) {
        switch (dart){
            case FIRST -> {
                firstThrow = new Throw(null, firstThrow.score(), firstThrow.isFinishingThrow(), firstThrow.isFinishingShot(), true, false);
                secondThrow = new Throw(null, 0, firstThrow.isFinishingThrow(), firstThrow.isFinishingShot(), true, false);
                thirdThrow = new Throw(null, 0, firstThrow.isFinishingThrow(), firstThrow.isFinishingShot(), true, false);
            }
            case SECOND -> {
                firstThrow = new Throw(null, firstThrow.score(), firstThrow.isFinishingThrow(), firstThrow.isFinishingShot(), true, false);
                secondThrow = new Throw(null, secondThrow.score(), secondThrow.isFinishingThrow(), secondThrow.isFinishingShot(), true, false);
                thirdThrow = new Throw(null, 0, secondThrow.isFinishingThrow(), secondThrow.isFinishingShot(), true, false);
            }
            case THIRD -> {
                firstThrow = new Throw(null, firstThrow.score(), firstThrow.isFinishingThrow(), firstThrow.isFinishingShot(), true, false);
                secondThrow = new Throw(null, secondThrow.score(), secondThrow.isFinishingThrow(), secondThrow.isFinishingShot(), true, false);
                thirdThrow = new Throw(null, thirdThrow.score(), thirdThrow.isFinishingThrow(), thirdThrow.isFinishingShot(), true, false);
            }
        }
        isBusted = true;
    }

    public int getScoreLeft() {
        return startScore - getScoreThrown();
    }

    public boolean is180() {
        return getScoreThrown() == 180;
    }

    public boolean is140() {
        return getScoreThrown() == 140;
    }

    public boolean isTonPlus() {
        return getScoreThrown() >= 100;
    }

    public Integer getStartScore() {
        return startScore;
    }

    public Integer getScoreThrown() {
        return getScore(firstThrow) + getScore(secondThrow) + getScore(thirdThrow);
    }

    public Integer getNumberOfDartsThrown() {
        int firstThrown = firstThrow == null ? 0 : 1;
        int secondThrown = secondThrow == null ? 0 : 1;
        int thirdThrown = thirdThrow == null ? 0 : 1;
        return firstThrown + secondThrown + thirdThrown;
    }

    public Stream<Throw> getThrows() {
        return Stream.of(firstThrow, secondThrow, thirdThrow).filter(Objects::nonNull);
    }

    public Optional<Throw> getPreviousThrow(Dart dart) {
        return switch (dart) {
            case FIRST -> Optional.empty();
            case SECOND -> Optional.of(firstThrow);
            case THIRD -> Optional.of(secondThrow);
        };
    }

    public boolean isBusted() {
        return isBusted;
    }

    private static int getScore(Throw throwToAdd) {
        if (throwToAdd == null) {
            return 0;
        }
        return throwToAdd.score();
    }

    @Override
    public String toString() {
        return "Turn{" +
                "startScore=" + startScore +
                ", firstThrow=" + firstThrow +
                ", secondThrow=" + secondThrow +
                ", thirdThrow=" + thirdThrow +
                ", isBusted=" + isBusted +
                '}';
    }
}
