package be.rommens.darts.simulator;

import be.rommens.darts.simulator.model.Dart;
import be.rommens.darts.simulator.model.Player;
import be.rommens.darts.simulator.model.Throw;
import org.springframework.stereotype.Component;

@Component
public class Source {

    private final Player[] players = {
            new Player("Humphries",25,50,95,42,44, 43),
            new Player("Joe",41,50,95,38,42, 39)
    };

    private final Score[] scores = {new Score(), new Score()};
    private final ThrowSimulator simulator;

    private static final int totalSets = 13;		   //total sets played in each match

    private boolean playerZeroTurn = true;         //Sid is player zero and starts
    private int currentPlayer = -1;          //this later holds whether Sid (0) is the current player or Joe (1)

    public Source(ThrowSimulator simulator) {
        this.simulator = simulator;
    }

    private void play() {
        playerZeroTurn = true;									  //Sid is player zero and starts each match
        playMatch(0);
    }


    //fn simulates 1 match
    private void playMatch(int matchNo) {
        //reset sets won to 0
        scores[1].setSetsWon(0);
        scores[0].setSetsWon(0);

        for (int setNo = 0; setNo < totalSets; setNo++) {
            playSet(setNo);
            playerZeroTurn = !playerZeroTurn;  //flip bool value and hence who throws first in next set
        }

        if (scores[1].getSetsWon() < scores[0].getSetsWon()) {
            int won = scores[0].getMatchesWon();
            scores[0].setMatchesWon(won+1);
        }
        else if (scores[0].getSetsWon() < scores[1].getSetsWon()) {
            int won = scores[1].getMatchesWon();
            scores[1].setMatchesWon(won+1);
        }
        else {
            throw new IllegalStateException("error in playMatch");
        }
    }

    //fn to simulate 1 set
    private void playSet(int setNo) {
        //reset games won to 0
        scores[1].setGamesWon(0);
        scores[0].setGamesWon(0);

        while ((scores[1].getGamesWon() < 3) && (scores[0].getGamesWon() < 3)) {
            playGame();

            //if player 1 has reached 3 game wins first, increase their number of sets won by 1
            if (scores[1].getGamesWon() == 3) {
                int setsWon1 = scores[1].getSetsWon();
                scores[1].setSetsWon(setsWon1+1);
            }
            //if player 0 has reached 3 game wins first, increase their number of sets won by 1
            else if (scores[0].getGamesWon() == 3) {
                int setsWon0 = scores[0].getSetsWon();
                scores[0].setSetsWon(setsWon0+1);
            }
            else if (scores[0].getGamesWon() > 3 || scores[1].getGamesWon() > 3)  {
                throw new IllegalStateException("error in playSet");
            }
        }
    }

    //fn to simulate a game
    void playGame()
    {
        //initially, who's turn it is is dictated from the simulation number of the sets (since who starts alternates between sets)
        //MISLEADING: CALL PLAYERZEORTHROWS
        boolean playerZeroThrows = playerZeroTurn;
        scores[1].setCurrentGameScore(501);
        scores[0].setCurrentGameScore(501);

        //repeat procedure of throwing dart until one player has 0 points
        while ((scores[1].getCurrentGameScore() != 0) && (scores[0].getCurrentGameScore() != 0)) {
            //initiate score before turn, used later to store score before throws to make sure it can be reset
            int scoreBeforeThreeThrows = -1;

            //decide who's turn it is
            if (playerZeroThrows) {
                currentPlayer = 0;
            }  //Sid is Player 0; Joe Player 1
            else {
                currentPlayer = 1;
            }

            //get current player's current score
            Player currentTurnPlayer = players[currentPlayer];
            Throw scoreAchieved;
            scoreBeforeThreeThrows = scores[currentPlayer].getCurrentGameScore(); //store score before throws to make sure it can be reset

            //simulates three throws; exits earlier if won or invalid throw
            for (Dart dart : Dart.values()) {
                int scoreCurrPlayer = scores[currentPlayer].getCurrentGameScore();  //score of current player

                scoreAchieved = simulator.throwDart(dart, scoreCurrPlayer, currentTurnPlayer);  //returns score hit

                System.out.println(String.format("%s : %s - %s", currentPlayer, scoreCurrPlayer, scoreAchieved.score()));

                //If score outcome large than 2 or equal to 2, update by subtraction
                if (scoreCurrPlayer - scoreAchieved.score() >= 2) {
                    scores[currentPlayer].setCurrentGameScore(scoreCurrPlayer - scoreAchieved.score());
                    //change over player only if third throw done
                    if(dart == Dart.SECOND) {
                        playerZeroThrows = !playerZeroThrows;
                    }
                }
                //if invalid, reset to previus and...
                else if ((scoreCurrPlayer - scoreAchieved.score() == 1 || scoreCurrPlayer - scoreAchieved.score() < 0) || (scoreCurrPlayer - scoreAchieved.score() == 0 && !scoreAchieved.isFinishingThrow())) {
                    scores[currentPlayer].setCurrentGameScore(scoreBeforeThreeThrows);
                    playerZeroThrows = !playerZeroThrows; //...change over player
                    break;
                }
                else if (scoreCurrPlayer - scoreAchieved.score() == 0 && scoreAchieved.isFinishingThrow()) {
                    System.out.println(String.format("%s WON", currentPlayer));

                    scores[currentPlayer].setCurrentGameScore(0); //set current player's score to zero
                    //update winnings by increasing games won by 1
                    int won = scores[currentPlayer].getGamesWon();
                    scores[currentPlayer].setGamesWon(won + 1);
                    playerZeroThrows = !playerZeroThrows; //change over player for next game
                    break;
                }
                else {
                    throw new IllegalStateException("error scoring");
                }
            }
        }
    }
}
