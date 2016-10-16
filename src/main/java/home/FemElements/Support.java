package home.FemElements;

public class Support {
    private final FemPoint femPoint;
    private final boolean supportByX;
    private final boolean supportByY;

    @SuppressWarnings("SameParameterValue")
    public Support(FemPoint femPoint, boolean supportByX, boolean supportByY) {
        this.femPoint = femPoint;
        this.supportByX = supportByX;
        this.supportByY = supportByY;
    }

    public FemPoint getFemPoint() {
        return femPoint;
    }

    public boolean isSupportByX() {
        return supportByX;
    }

    public boolean isSupportByY() {
        return supportByY;
    }
}
