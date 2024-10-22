package be.rommens.darts.simulator.model;

public record Player (
        String name,
        int accuracyBull,
        int accuracyOuter,
        int accuracySingle,
        int accuracyDouble,
        int accuracyTreble,
        int accuracyDouble3rdDart) {

}
