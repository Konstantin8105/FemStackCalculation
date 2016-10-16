package home.FemElements;

import home.Other.FemPoint;
import jama.Matrix;

public abstract class FemElement {

    private static int global_number = 0;

    private static int getGlobalNumber() {
        return global_number++;
    }

    private Matrix displacementInGlobalSystem;
    private Matrix displacementInLocalSystem;
    private Matrix internalForce;
    private final int[] axes;
    protected FemPoint[] point;
    private final double length;

    public FemElement(FemPoint[] point) {
        this.point = point;
        axes = new int[getAmountAxes()];
        for (int i = 0; i < axes.length; i++) {
            axes[i] = getGlobalNumber();
        }
        length = Math.sqrt(Math.pow(point[0].getX() - point[1].getX(), 2.) + Math.pow(point[0].getY() - point[1].getY(), 2.));
    }

    protected double getLength(){
        return length;
    }

    abstract protected int getAmountAxes();

    abstract public Matrix getTr();

    abstract public Matrix getStiffenerMatrix();

    public int[] getAxes() {
        return axes;
    }

    public FemPoint[] getPoint() {
        return point;
    }

    public Matrix getDisplacementInGlobalSystem() {
        return displacementInGlobalSystem;
    }

    public Matrix getDisplacementInLocalSystem() {
        return displacementInLocalSystem;
    }

    public Matrix getInternalForce() {
        return internalForce;
    }

    public Matrix getStiffenerMatrixTr() {
        Matrix tr = getTr();
        Matrix kr = getStiffenerMatrix();
        return ((tr.transpose().times(kr)).times(tr));
    }

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
