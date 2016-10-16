package home;

public class Force {
    private MKEPoint mkePoint;
    private double fx;
    private double fy;

    public Force(MKEPoint mkePoint, double fx, double fy) {
        this.mkePoint = mkePoint;
        this.fx = fx;
        this.fy = fy;
    }

    public MKEPoint getMkePoint() {
        return mkePoint;
    }

    public double getFx() {
        return fx;
    }

    public double getFy() {
        return fy;
    }
}
