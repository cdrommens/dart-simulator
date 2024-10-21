package be.rommens.darts.simulator;

import static be.rommens.darts.simulator.strategy.AimType.BULL;
import static be.rommens.darts.simulator.strategy.AimType.DOUBLE;
import static be.rommens.darts.simulator.strategy.AimType.SINGLE;
import static be.rommens.darts.simulator.strategy.AimType.TREBLE;

import be.rommens.darts.simulator.guide.OptimalPath;
import be.rommens.darts.simulator.model.Dart;
import be.rommens.darts.simulator.model.Player;
import be.rommens.darts.simulator.model.Throw;
import be.rommens.darts.simulator.strategy.ThrowSimulationStrategy;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ThrowSimulator {

    private final OptimalPath optimalPath;
    private final Map<String, ThrowSimulationStrategy> throwSimulationStrategies;

    public ThrowSimulator(OptimalPath optimalPath, Map<String, ThrowSimulationStrategy> throwSimulationStrategies) {
        this.optimalPath = optimalPath;
        this.throwSimulationStrategies = throwSimulationStrategies;
    }

    //fn returns score of target actually hit
    public Throw throwDart(Dart dart, int currentScore, Player player) {
        //takes score of current player, board object, current player object,
        //ThrowDecision object, and bool for validity check in case this is a final throw

        //if it remains an even number between 2 and 40
        if ((currentScore <= 40 && currentScore >= 2) && (currentScore % 2 == 0)) {
            //throw double aiming at s/2 with hitting prob. of the respective player and return whatever was hit
            return throwDart(DOUBLE, currentScore / 2, player, true);
        } else if (currentScore == 50) {
            //throw bull
            return throwDart(BULL, 50, player, true);
        } else {
            //ask throwDecision() what to aim for
            //int to aim at is suggested via throwDecision()-function, which takes real advice into account
            int aim = optimalPath.decideAim(dart, currentScore);
            if (aim == 60 || aim == 57 || aim == 54 || aim == 51 || aim == 48 || aim == 45 || aim == 42 || aim == 39 || aim == 33 || aim == 30) {
                //throw treble aiming at aim/3 with hitting prob. of the respective player
                return throwDart(TREBLE, aim / 3, player, false);
            } else {
                //throw single aiming at aim with hitting prob. of the respective player, which is different if aim is 25 than otherwise
                if (aim > 20 && aim % 2 == 0) { //see last dart in optimal path (double should now be single)
                    return throwDart(SINGLE, aim / 2, player, false);
                } else {
                    return throwDart(SINGLE, aim, player, false);
                }
            }
        }
    }

    private Throw throwDart(String aimType, int scoreToAim, Player player, boolean isFinishingShot) {
        if (throwSimulationStrategies.containsKey(aimType.toString())) {
            return throwSimulationStrategies.get(aimType).simulateThrow(scoreToAim, player, isFinishingShot);
        }
        throw new IllegalArgumentException("Unknown aim type: " + aimType);
    }
}
