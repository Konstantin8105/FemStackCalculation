package home.FemElements;

import jama.Matrix;

public class FemTruss2d extends FemElement {
    private final double elacity;
    private final double area;
    private final double length;
    private static final int SIZE_OF_AXES = 4;

    @SuppressWarnings("SameParameterValue")
    public FemTruss2d(double elacity, double area, FemPoint[] point) {
        this.elacity = elacity;
        this.area = area;
        this.point = point;
        length = Math.sqrt(Math.pow(point[0].getX() - point[1].getX(), 2.) + Math.pow(point[0].getY() - point[1].getY(), 2.));
        axes = new int[SIZE_OF_AXES];
        for (int i = 0; i < SIZE_OF_AXES; i++) {
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
    public Matrix getStiffenerMatrixTr() {
        Matrix tr = getTr();
        Matrix kr = getStiffenerMatrix();
        return ((tr.transpose().times(kr)).times(tr));
    }


    @Override
    public void addInGlobalDisplacementCoordinate(double[] localDisplacement) {
        double[][] temp = new double[localDisplacement.length][1];
        for (int i = 0; i < localDisplacement.length; i++) {
            temp[i][0] = localDisplacement[i];
        }
        displacementInGlobalSystem = new Matrix(temp);
        displacementInLocalSystem = getTr().times(displacementInGlobalSystem);
        internalForce = getStiffenerMatrix().times(displacementInLocalSystem);
    }

}
