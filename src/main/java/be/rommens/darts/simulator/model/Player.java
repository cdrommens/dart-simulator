package be.rommens.darts.simulator.model;

public record Player (
        String name,
        double accuracyBull,
        double accuracyDouble,
        double accuracyTreble,
        double accuracyDouble3rdDart,
        double first9avg) {

    /*
    As an amateur player, my own average is some way below the level of the PDC tour.
    At the 2022 Viking Cup, I threw a 3-point average of 47.86 in my last 256 game,
    whilst my opponent George Willetts posted a much superior average of 68.32.
    An average of around 35-45 is considered a beginner average, ﻿46-55 ﻿a pub player average,
    56-70 a super league/county player average,
    71-85 a PDC Challenge Tour average,
    86-99 a PDC tour player average,
    and 100 and above a world-class average.
     */
    //TODO : upgrade java 23, rework to switch
    public double getMinimumAverageCurrentLevel() {
        if (first9avg <= 45.00) {
            // 35-45 is considered a beginner average
            return 0.00;
        } else if (first9avg() <= 55.00) {
            // 46-55 pub player
            return 46.00;
        } else if (first9avg() <= 70.00) {
            // super league / county player
            return 56.00;
        } else if (first9avg() <= 85.00) {
            // PDC Challenge Tour
            return 71.00;
        } else if (first9avg() <= 99.00) {
            // PDC Tour
            return 86.00;
        } else {
            // world class
            return 87.00;
        }
    }

}
