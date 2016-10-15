package home.FemElements;

import home.MKEPoint;
import jama.Matrix;

public class FemTruss2d implements FemElement {
    private double elacity;
    private double area;
    private MKEPoint point[] = new MKEPoint[2];
    private double length;
    private int axes[] = new int[4];

    static private int global_number = 0;

    private static int getGlobalNumber() {
        return global_number++;
    }

    public FemTruss2d(double elacity, double area, MKEPoint[] point) {
        this.elacity = elacity;
        this.area = area;
        this.point = point;
        length = Math.sqrt(Math.pow(point[0].getX() - point[1].getX(), 2.) + Math.pow(point[0].getY() - point[1].getY(), 2.));
        for (int i = 0; i < axes.length; i++) {
            axes[i] = getGlobalNumber();
        }
    }

    @Override
    public Matrix getTr() {
        double lambda_xx = (point[1].getX() - point[0].getX()) / length;
        double lambda_xy = (point[1].getY() - point[0].getY()) / length;
        return new Matrix(new double[][]{
                {lambda_xx, lambda_xy, 0, 0},
                {0, 0, lambda_xx, lambda_xy}
        });
    }

    @Override
    public Matrix getStiffenerMatrix() {
        double EFL = elacity * area / length;
        return new Matrix(new double[][]{
                {EFL, -EFL},
                {-EFL, EFL}
        });
    }

    @Override
    public int[] getAxes() {
        return axes;
    }

    @Override
    public MKEPoint[] getPoint() {
        return point;
    }
}
