package home.FemElements;

import home.Other.FemPoint;
import jama.Matrix;

public abstract class FemElement {

    private static int global_number = 0;

    private static int getGlobalNumber() {
        return global_number++;
    }

    public static void dropNumeration() {
        global_number = 0;
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

    protected abstract Matrix getTr();

    protected abstract Matrix getStiffenerMatrix();

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
        for (int i = 0; i < 2; i++) {
            double[] displacementPointInGlobalSystem = new double[getAmountAxes()/2];
            for (int j = 0; j < getAmountAxes()/2; j++) {
                displacementPointInGlobalSystem[j] = localDisplacement[j+i*getAmountAxes()/2];
            }
            point[i].setGlobalDisplacement(displacementPointInGlobalSystem);
        }
        displacementInGlobalSystem = new Matrix(temp);
        displacementInLocalSystem = getTr().times(displacementInGlobalSystem);
        internalForce = getStiffenerMatrix().times(displacementInLocalSystem);
    }

}
