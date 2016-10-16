package home.FemElements;

public class Force {
    private FemPoint femPoint;
    private double fx;
    private double fy;

    public Force(FemPoint femPoint, double fx, double fy) {
        this.femPoint = femPoint;
        this.fx = fx;
        this.fy = fy;
    }

    public FemPoint getFemPoint() {
        return femPoint;
    }

    public double getFx() {
        return fx;
    }

    public double getFy() {
        return fy;
    }
}
