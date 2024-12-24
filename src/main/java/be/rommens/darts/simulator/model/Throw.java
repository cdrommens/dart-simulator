package be.rommens.darts.simulator.model;

import org.apache.commons.geometry.euclidean.twod.PolarCoordinates;

public record Throw(PolarCoordinates point, int score, boolean isFinishingThrow, boolean isFinishingShot, boolean isBusted, boolean isHitAsIntended) {

}
