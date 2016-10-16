package home.FemElements;

public class FemPoint {

    static private int global_number = 0;

    private static int getGlobalNumber() {
        return global_number++;
    }

    private final int number;
    private final int numberGlobalAxeX;
    private final int numberGlobalAxeY;
    private final double x;
    private final double y;

    public FemPoint(int number, double x, double y) {
        this.number = number;
        this.x = x;
        this.y = y;
        numberGlobalAxeX = getGlobalNumber();
        numberGlobalAxeY = getGlobalNumber();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getNumberGlobalAxeX() {
        return numberGlobalAxeX;
    }

    public int getNumberGlobalAxeY() {
        return numberGlobalAxeY;
    }

    @Override
    public String toString() {
        return "FemPoint{" +
                "number=" + number +
                ", numberGlobalAxeX=" + numberGlobalAxeX +
                ", numberGlobalAxeY=" + numberGlobalAxeY +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
