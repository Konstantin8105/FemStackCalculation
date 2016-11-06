package home.loads;

import home.other.FemPoint;

public class MassPoint {
    private final FemPoint femPoint;
    private final double amplitude;

    @SuppressWarnings("SameParameterValue")
    public MassPoint(FemPoint femPoint, double amplitude) {
        this.femPoint = femPoint;
        this.amplitude = amplitude;
    }

    public FemPoint getFemPoint() {
        return femPoint;
    }

    public double getAmplitude() {
        return amplitude;
    }
}
