package home;

public class Support {
    MKEPoint mkePoint;
    boolean supportByX;
    boolean supportByY;

    public Support(MKEPoint mkePoint, boolean supportByX, boolean supportByY) {
        this.mkePoint = mkePoint;
        this.supportByX = supportByX;
        this.supportByY = supportByY;
    }

    public MKEPoint getMkePoint() {
        return mkePoint;
    }

    public boolean isSupportByX() {
        return supportByX;
    }

    public boolean isSupportByY() {
        return supportByY;
    }
}
