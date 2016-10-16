package home.FemElements;

import home.Other.FemPoint;
import jama.Matrix;

public class FemTruss2d extends FemElement {
    private final double elacity;
    private final double area;

    @SuppressWarnings("SameParameterValue")
    public FemTruss2d(double elacity, double area, FemPoint[] point) {
        super(point);
        this.elacity = elacity;
        this.area = area;
        this.point = point;
    }

    @Override
    protected int getAmountAxes() {
        return 4;
    }

    @Override
    public Matrix getTr() {
        double lambda_xx = (point[1].getX() - point[0].getX()) / getLength();
        double lambda_xy = (point[1].getY() - point[0].getY()) / getLength();
        return new Matrix(new double[][]{
                {lambda_xx, lambda_xy, 0, 0},
                {0, 0, lambda_xx, lambda_xy}
        });
    }

    @Override
    public Matrix getStiffenerMatrix() {
        double EFL = elacity * area / getLength();
        return new Matrix(new double[][]{
                {EFL, -EFL},
                {-EFL, EFL}
        });
    }
}
