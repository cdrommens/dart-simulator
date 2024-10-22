package be.rommens.darts.simulator.model;

import java.util.List;

public class Statistics {

    private int numberOfDartsThrown;
    private double average;
    private int first9Average;
    private double checkoutPercentage;
    private int number180s;
    private int number140s;
    private int numberTonPlus;

    private Statistics() {
        //private constructor
    }

    //TODO : Treble less visits: Het aantal beurten dat gegooid is met een score onder de 60. Dus zonder een triple 20, 19, 18 of 17. Wanneer men onder de 200 punten komt en richting het wegzetten gaat, worden deze ‘visits’ niet meer geteld.
    public static Statistics calculate(List<Turn> turns) {
        Statistics statistics = new Statistics();
        statistics.numberOfDartsThrown = calculateNumberOfDartsThrown(turns);
        statistics.average = calculateAverage(turns);
        statistics.first9Average = calculateFirst9Average(turns);
        statistics.checkoutPercentage = calculateCheckoutPercentage(turns);
        statistics.number180s = calculateNumberOf180s(turns);
        statistics.number140s = calculateNumberOf140s(turns);
        statistics.numberTonPlus = calculateNumberOfTonPlus(turns);
        return statistics;
    }

    public void write() {
        System.out.println("Number of darts thrown : " + numberOfDartsThrown);
        System.out.println("Average : " + average);
        System.out.println("First 9 Average : " + first9Average);
        System.out.println("Checkout% : " + checkoutPercentage);
        System.out.println("180's : " + number180s);
        System.out.println("140's : " + number140s);
        System.out.println("100+ : " + numberTonPlus);
    }

    private static int calculateNumberOfDartsThrown(List<Turn> turns) {
        return turns.stream().map(Turn::getNumberOfDartsThrown).reduce(0, Integer::sum);
    }

    private static double calculateAverage(List<Turn> turns) {
        return 501.00 / turns.stream().map(Turn::getNumberOfDartsThrown).reduce(0, Integer::sum) * 3;
    }

    private static int calculateFirst9Average(List<Turn> turns) {
        return turns.stream()
                .limit(3)
                .map(Turn::getScoreThrown)
                .reduce(0, Integer::sum)/ 9 * 3;
    }

    private static double calculateCheckoutPercentage(List<Turn> turns) {
        return (1.0 / turns.stream()
                .flatMap(Turn::getThrows)
                .filter(Throw::isFinishingShot)
                .toList().size()) * 100;
    }

    private static int calculateNumberOf180s(List<Turn> turns) {
        return (int)turns.stream()
                .filter(Turn::is180)
                .count();
    }

    private static int calculateNumberOf140s(List<Turn> turns) {
        return (int)turns.stream()
                .filter(Turn::is140)
                .count();
    }

    private static int calculateNumberOfTonPlus(List<Turn> turns) {
        return (int)turns.stream()
                .filter(Turn::isTonPlus)
                .count();
    }
}
