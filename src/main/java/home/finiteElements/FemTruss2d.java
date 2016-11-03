package home.finiteElements;

import Jama.Matrix;
import home.finiteElements.interfaces.FemElement;
import home.other.FemPoint;

public class FemTruss2d extends FemElement {
    private final double elacity;
    private final double area;

    public FemTruss2d(double elacity, double area, FemPoint[] point) {
        super(point);
        this.elacity = elacity;
        this.area = area;
    }
//
//    @Override
//    protected int getAmountLocalAxes() {
//        return 4;
//    }

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

    @Override
    public Matrix getPotentialMatrix() {
        return null;
    }

    @Override
    public Matrix getMatrixMass() {
        return null;
    }
//
//    @Override
//    public void setGlobalDisplacementInPoint(double[] localDisplacement) {
//        point[0].setGlobalDisplacement(new double[]{localDisplacement[0], 0, 0});
//        point[1].setGlobalDisplacement(new double[]{localDisplacement[1], 0, 0});
//    }

    final static boolean[] axes = new boolean[]{true, false, false, true, false, false};

    @Override
    public boolean[] getAxeAllowable() {
        return axes;
    }
}
