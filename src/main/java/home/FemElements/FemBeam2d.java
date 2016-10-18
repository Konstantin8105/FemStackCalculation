package home.FemElements;

import home.Other.FemPoint;
import jama.Matrix;

public class FemBeam2d extends FemElement {
    private final double elacity;
    private final double area;
    private final double momentInertia;

    @SuppressWarnings("SameParameterValue")
    public FemBeam2d(double elacity, double area, double momentInertia, FemPoint[] point) {
        super(point);
        this.elacity = elacity;
        this.area = area;
        this.momentInertia = momentInertia;
    }

    @Override
    protected int getAmountAxes() {
        return 6;
    }

    @Override
    public Matrix getTr() {
        double lambda_xx = (point[1].getX() - point[0].getX()) / getLength();
        double lambda_xy = (point[1].getY() - point[0].getY()) / getLength();
        return new Matrix(new double[][]{
                {+lambda_xx, -lambda_xy, 0, 0, 0, 0},
                {+lambda_xy, +lambda_xx, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0},
                {0, 0, 0, +lambda_xx, -lambda_xy, 0},
                {0, 0, 0, +lambda_xy, +lambda_xx, 0},
                {0, 0, 0, 0, 0, 1}
        });
    }

    @Override
    public Matrix getStiffenerMatrix() {
        double[][] stiffener = new double[6][6];
        double l = getLength();
        double EFL = elacity * area / getLength();
        double EJ = elacity * momentInertia;
        stiffener[0][0] = stiffener[3][3] = +EFL;
        stiffener[0][3] = stiffener[3][0] = -EFL;
        stiffener[1][1] = stiffener[4][4] = 12.0 * EJ / Math.pow(l, 3);
        stiffener[2][2] = stiffener[5][5] = 4.00 * EJ / l;
        stiffener[1][2] = stiffener[2][1] = 6.00 * EJ / Math.pow(l, 2);
        stiffener[1][5] = stiffener[5][1] = 6.00 * EJ / Math.pow(l, 2);
        stiffener[4][5] = stiffener[5][4] = -6.0 * EJ / Math.pow(l, 2);
        stiffener[2][4] = stiffener[4][2] = -6.0 * EJ / Math.pow(l, 2);
        stiffener[1][4] = stiffener[4][1] = -12. * EJ / Math.pow(l, 3);
        stiffener[2][5] = stiffener[5][2] = 2.00 * EJ / l;
        return new Matrix(stiffener);
    }

    @Override
    protected Matrix getPotentialMatrix() {
        double[][] stiffener = new double[6][6];
        double l = getLength();
        stiffener[1][1] = stiffener[4][4] = 6. / 5.0 / l;
        stiffener[2][2] = stiffener[5][5] = 2. / 15. / l;
        stiffener[1][2] = stiffener[2][1] = 0.1;
        stiffener[2][4] = stiffener[4][2] = -0.1;
        stiffener[4][5] = stiffener[5][4] = -0.1;
        stiffener[4][1] = stiffener[1][4] = -6. / 5. / l;
        stiffener[5][2] = stiffener[2][5] = -l / 30.;
        stiffener[1][5] = stiffener[5][1] = 0.1;
        return new Matrix(stiffener);
    }

    @Override
    protected void setGlobalDisplacementInPoint(double[] localDisplacement) {
        point[0].setGlobalDisplacement(new double[]{localDisplacement[0], localDisplacement[1], localDisplacement[2]});
        point[1].setGlobalDisplacement(new double[]{localDisplacement[3], localDisplacement[4], localDisplacement[5]});
    }
}
