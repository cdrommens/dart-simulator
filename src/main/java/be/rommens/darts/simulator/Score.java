package be.rommens.darts.simulator;

public class Score {
    private int gamesWon = 0;
    private int setsWon = 0;
    private int matchesWon = 0;
    private int currentGameScore = 501;

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public int getSetsWon() {
        return setsWon;
    }

    public void setSetsWon(int setsWon) {
        this.setsWon = setsWon;
    }

    public int getMatchesWon() {
        return matchesWon;
    }

    public void setMatchesWon(int matchesWon) {
        this.matchesWon = matchesWon;
    }

    public int getCurrentGameScore() {
        return currentGameScore;
    }

    public void setCurrentGameScore(int currentGameScore) {
        this.currentGameScore = currentGameScore;
    }
}
