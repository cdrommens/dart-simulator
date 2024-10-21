package be.rommens.darts.simulator;

import org.springframework.stereotype.Component;

@Component
public class ThrowDecision {

    private final OptimalPath optimalPath;
    private final Board board;

    public ThrowDecision(OptimalPath optimalPath, Board board) {
        this.optimalPath = optimalPath;
        this.board = board;
    }

    //fn returns score of target actually hit
    public Throw simulateThrow(Dart dart, int currentScore, Player player) {
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
            int aim = optimalPath.decideAim(dart, currentScore);
            if (aim == 60 || aim == 57 || aim == 54 || aim == 51 || aim == 48 || aim == 45 || aim == 42 || aim == 39 || aim == 33 || aim == 30) {
                //throw treble aiming at aim/3 with hitting prob. of the respective player
                return board.throwTreble(aim / 3, player.accuracyTreble());
            } else {
                //throw single aiming at aim with hitting prob. of the respective player, which is different if aim is 25 than otherwise
                if (aim > 20 && aim % 2 == 0) { //see last dart in optimal path (double should now be single)
                    return board.throwSingle(aim / 2, player.accuracyBull(), player.accuracyOuter());
                } else {
                    return board.throwSingle(aim, player.accuracySingle(), player.accuracyOuter());
                }
            }
        }
    }
}
