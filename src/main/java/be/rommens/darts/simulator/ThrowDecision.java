package be.rommens.darts.simulator;

import org.springframework.stereotype.Component;

@Component
public class ThrowDecision {

    //TODO : improve with number of darts left (64 with 2 darts is different than 64 with 3 darts)
    //TODO : rework to map (map THREE_DARTS_LEFT, map TWO_DARTS_LEFT)
    //fn returns target SCORE to aim for, given a current score of s;
    // used the Out Chart for 01 Dart Games (bargames101.com/wp-content/uploads/2016/10/The-01-Out-Chart_750x970.png);
    // fn is called by actualTargetHit()
    private int throwDecision(int s) {
        //trebles
        if (s >= 144 || s == 141 || s == 140 || s == 138 || (s <= 136 && s >= 130) || s == 127 || s == 124 || s == 120 || s == 118 || s == 117
                || s == 116 || s == 114 || s == 112 || s == 110 || s == 109 || s == 107 || s == 106 || s == 100 || s == 98 || s == 96 || s == 92
                || s == 80 || s == 76 || s == 68) {
            return 60;  //treble 20
        } else if (s == 143 || s == 142 || s == 139 || s == 129 || s == 126 || s == 123 || s == 119 || s == 115 || s == 113 || s == 111 || s == 108
                || s == 105 || s == 102 || s == 99 || s == 97 || s == 95 || s == 93 || s == 89 || s == 81 || s == 73) {
            return 57;  //treble 19
        } else if (s == 128 || s == 125 || s == 122 || s == 104 || s == 94 || s == 90 || s == 86 || s == 78 || s == 70) {
            return 54;  //treble 18
        } else if (s == 137 || s == 121 || s == 103 || s == 101 || s == 91 || s == 87 || s == 83 || s == 75 || s == 67) {
            return 51;  //treble 17
        } else if (s == 88 || s == 72 || s == 64) {
            return 48;  //treble 16
        } else if (s == 85 || s == 77 || s == 69 || s == 61) {
            return 45;  //treble 15
        } else if (s == 82 || s == 74 || s == 66) {
            return 42;  //treble 14
        } else if (s == 79 || s == 71 || s == 63) {
            return 39;  //treble 13
        } else if (s == 65) {
            return 33;  //treble 11
        } else if (s == 62) {
            return 30;  //treble 10
        }
        //doubles
        else if (s == 84) {
            return 40;  //double 20
        }
        //Singles (defined by aim)
        //reduce to double 20
        // reduce to be able to end on double 20 by playing appropriate single
        else if ((s <= 60 && s >= 51) || s == 49 || s == 48) {
            return s - 40;  //suggests to aim for single 20 to single 11 or 9 or 8, respectively so that 40 are left over
        }
        //reduce to double 16
        // reduce to be able to end on double 16 by playing appropriate single
        else if (s <= 47 && s >= 33) {
            return s - 32;  //single 15 to single 3, respectively
        }
        //reduce to double 12
        // reduce to be able to end on double 12 by playing single 7
        else if (s == 31) {
            return 7;  //single 7
        }
        //reduce to double 8
        // reduce to be able to end on double 8 by playing appropriate single
        else if (s <= 29 && s >= 19) {
            return s - 16;  //single 13 to single 3, respectively
        }
        //reduce to double 4
        // reduce to be able to end on double 4 by playing appropriate single
        else if (s <= 17 && s >= 9) {
            return s - 8;  //single 9 to single 1, respectively
        }
        //reduce to double 2
        // reduce to be able to end on double 2 by playing appropriate single
        else if (s == 7 || s == 5) {
            return s - 4;  //single 3 to single 1, respectively
        }
        //reduce to double 1
        // reduce to be able to end on double 1 by playing appropriate single
        else if (s == 3) {
            return 1;  // single 1
        } else {
            throw new IllegalStateException("Error when deciding throw");
        }
    }

    //fn returns score of target actually hit
    public SingleScore actualTargetHit(int currentScore, Board board, Player player, ThrowDecision decide) {
        //takes score of current player, board object, current player object,
        //ThrowDecision object, and bool for validity check in case this is a final throw

        //if it remains an even number between 2 and 40
        if ((currentScore <= 40 && currentScore >= 2) && (currentScore % 2 == 0)) {
            //throw double aiming at s/2 with hitting prob. of the respective player and return whatever was hit
            return board.throwDouble(currentScore / 2, player.accuracyDouble());
        } else if (currentScore == 50) {
            //throw bull
            return board.throwBull(player.accuracyBull());
        } else {
            //ask throwDecision() what to aim for
            //int to aim at is suggested via throwDecision()-function, which takes real advice into account
            int aim = decide.throwDecision(currentScore);
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
