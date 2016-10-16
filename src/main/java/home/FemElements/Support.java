package home.FemElements;

public class Support {
    FemPoint femPoint;
    boolean supportByX;
    boolean supportByY;

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
