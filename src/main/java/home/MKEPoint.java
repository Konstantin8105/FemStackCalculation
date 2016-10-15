package home;

public class MKEPoint {

    static private int global_number = 0;

    private static int getGlobalNumber() {
        return global_number++;
    }

    private int number;
    private int numberGlobalAxeX;
    private int numberGlobalAxeY;
    private double x, y;

    public MKEPoint(int number, double x, double y) {
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
        return "MKEPoint{" +
                "number=" + number +
                ", numberGlobalAxeX=" + numberGlobalAxeX +
                ", numberGlobalAxeY=" + numberGlobalAxeY +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
