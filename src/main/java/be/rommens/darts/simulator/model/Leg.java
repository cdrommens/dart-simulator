package be.rommens.darts.simulator.model;

import java.util.List;

public record Leg(Player player,
                  List<Turn> turns,
                  Statistics statistics) {

    public Leg(Player player, List<Turn> turns, Statistics statistics) {
        this.player = player;
        this.turns = turns;
        this.statistics = statistics;
    }

    public Leg(Player player, List<Turn> turns) {
        this(player, turns, Statistics.calculate(turns));
    }
}
