package home.Other;

import home.Direction;

public class Force {
    private final FemPoint femPoint;
    private final double amplitude;
    private final Direction direction;

    @SuppressWarnings("SameParameterValue")
    public Force(FemPoint femPoint, Direction direction, double amplitude) {
        this.femPoint = femPoint;
        this.direction = direction;
        this.amplitude = amplitude;
    }

    public FemPoint getFemPoint() {
        return femPoint;
    }

    public double getAmplitude() {
        return amplitude;
    }

    public Direction getDirection() {
        return direction;
    }
}
