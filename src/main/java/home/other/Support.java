package home.other;

public class Support {
    private final FemPoint femPoint;
    private final Direction direction;

    @SuppressWarnings("SameParameterValue")
    public Support(FemPoint femPoint, Direction direction) {
        this.femPoint = femPoint;
        this.direction = direction;
    }

    public FemPoint getFemPoint() {
        return femPoint;
    }

    public Direction getDirection() {
        return direction;
    }
}
