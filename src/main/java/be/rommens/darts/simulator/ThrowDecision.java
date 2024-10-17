package be.rommens.darts.simulator;

import org.springframework.stereotype.Component;

@Component
public class ThrowDecision {

    private final OptimalPath optimalPath;

    public ThrowDecision(OptimalPath optimalPath) {
        this.optimalPath = optimalPath;
    }

    //TODO : improve optimal path with input from https://dartscheckoutassistant.com/2021/01/26/a-guide-to-reaching-a-checkout/
    //calculate optimal path for dart 1, 2, 3 starting from 501 -> 2
    //TODO : improve with number of darts left (64 with 2 darts is different than 64 with 3 darts)
    //TODO : rework to map (map THREE_DARTS_LEFT, map TWO_DARTS_LEFT)
    //fn returns target SCORE to aim for, given a current score of s;
    // used the Out Chart for 01 Dart Games (bargames101.com/wp-content/uploads/2016/10/The-01-Out-Chart_750x970.png);
    // fn is called by actualTargetHit()
    private int throwDecision(Dart dart, int s) {
        return switch (dart) {
            case FIRST -> optimalPath.getFirstAim(s);
            case SECOND -> optimalPath.getSecondAim(s);
            case THIRD -> optimalPath.getThirdAim(s);
        };
    }

    //fn returns score of target actually hit
    public SingleScore actualTargetHit(Dart dart, int currentScore, Board board, Player player) {
        //takes score of current player, board object, current player object,
        //ThrowDecision object, and bool for validity check in case this is a final throw

        //if it remains an even number between 2 and 40
        if ((currentScore <= 40 && currentScore >= 2) && (currentScore % 2 == 0)) {
            //throw double aiming at s/2 with hitting prob. of the respective player and return whatever was hit
            return board.throwDouble(currentScore / 2, player.accuracyDouble(), true);
        } else if (currentScore == 50) {
            //throw bull
            return board.throwBull(player.accuracyBull(), true);
        } else {
            //ask throwDecision() what to aim for
            //int to aim at is suggested via throwDecision()-function, which takes real advice into account
            int aim = throwDecision(dart, currentScore);
            if (aim == 60 || aim == 57 || aim == 54 || aim == 51 || aim == 48 || aim == 45 || aim == 42 || aim == 39 || aim == 33 || aim == 30) {
                //throw treble aiming at aim/3 with hitting prob. of the respective player
                return board.throwTreble(aim / 3, player.accuracyTreble());
            } else {
                //throw single aiming at aim with hitting prob. of the respective player, which is different if aim is 25 than otherwise
                return board.throwSingle(aim, player.accuracySingle(), player.accuracyOuter());
            }
        }
    }
}
