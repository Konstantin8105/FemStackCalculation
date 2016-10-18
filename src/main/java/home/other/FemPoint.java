package home.other;

import java.util.Arrays;

public class FemPoint {

    static private int global_number = 0;
    private final double[] globalDisplacement = new double[3];

    private static int getGlobalNumber() {
        return global_number++;
    }

    public static void dropNumeration() {
        global_number = 0;
    }

    private final int number;
    private final int[] numberGlobalAxe = new int[3];
    private final double x;
    private final double y;

    public FemPoint(int number, double x, double y) {
        this.number = number;
        this.x = x;
        this.y = y;
        for (int i = 0; i < numberGlobalAxe.length; i++) {
            numberGlobalAxe[i] = getGlobalNumber();
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "FemPoint{" +
                "number=" + number +
                ", numberGlobalAxe=" + Arrays.toString(numberGlobalAxe) +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    public int[] getNumberGlobalAxe() {
        return numberGlobalAxe;
    }

    public void setGlobalDisplacement(double[] globalDisplacement) {
        System.arraycopy(globalDisplacement, 0, this.globalDisplacement, 0, globalDisplacement.length);
    }

    public double[] getGlobalDisplacement() {
        return globalDisplacement;
    }
}
