package home.FemElements;

import home.Other.FemPoint;
import jama.Matrix;

public class FemBendBeam2d extends FemElement {
    private final double elacity;
    private final double momentInertia;

    @SuppressWarnings("SameParameterValue")
    public FemBendBeam2d(double elacity, double momentInertia, FemPoint[] point) {
        super(point);
        this.elacity = elacity;
        this.momentInertia = momentInertia;
    }

    @Override
    protected int getAmountAxes() {
        return 4;
    }

    @Override
    protected Matrix getTr() {
        double lambda_xx = (point[1].getX() - point[0].getX()) / getLength();
        return new Matrix(new double[][]{
                {lambda_xx, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, lambda_xx, 0},
                {0, 0, 0, 1}
        });
    }

    @Override
    protected Matrix getStiffenerMatrix() {
        double[][] stiffener = new double[4][4];
        double l = getLength();
        double EJ = elacity * momentInertia;
        stiffener[0][0] = stiffener[2][2] = 12.0 * EJ / Math.pow(l, 3);
        stiffener[1][1] = stiffener[3][3] = 4.00 * EJ / l;
        stiffener[0][1] = stiffener[1][0] = 6.00 * EJ / Math.pow(l, 2);
        stiffener[0][3] = stiffener[3][0] = 6.00 * EJ / Math.pow(l, 2);
        stiffener[0][2] = stiffener[2][0] = -12. * EJ / Math.pow(l, 3);
        stiffener[1][2] = stiffener[2][1] = -6.0 * EJ / Math.pow(l, 2);
        stiffener[2][3] = stiffener[3][2] = -6.0 * EJ / Math.pow(l, 2);
        stiffener[1][3] = stiffener[3][1] = 2.00 * EJ / l;
        return new Matrix(stiffener);
    }

    @Override
    protected void setGlobalDisplacementInPoint(double[] localDisplacement) {
        point[0].setGlobalDisplacement(new double[]{0,localDisplacement[0],localDisplacement[1]});
        point[1].setGlobalDisplacement(new double[]{0,localDisplacement[2],localDisplacement[3]});
    }
}
