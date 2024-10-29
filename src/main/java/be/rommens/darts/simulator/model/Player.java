package be.rommens.darts.simulator.model;

public record Player (
        String name,
        int accuracyBull,
        int accuracyOuter,
        int accuracySingle,
        int accuracyDouble,
        double accuracyTreble,
        int accuracyDouble3rdDart,
        double first9avg) {

}
