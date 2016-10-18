package home.finiteElement;

import home.other.FemPoint;
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
    FemPoint[] point;
    private final double length;

    FemElement(FemPoint[] point) {
        this.point = point;
        axes = new int[getAmountAxes()];
        for (int i = 0; i < axes.length; i++) {
            axes[i] = getGlobalNumber();
        }
        length = Math.sqrt(Math.pow(point[0].getX() - point[1].getX(), 2.) + Math.pow(point[0].getY() - point[1].getY(), 2.));
    }

    double getLength(){
        return length;
    }

    abstract protected int getAmountAxes();

    abstract protected Matrix getTr();

    abstract protected Matrix getStiffenerMatrix();

    abstract protected Matrix getPotentialMatrix();

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

    public Matrix getPotentialMatrixTr() {
        Matrix tr = getTr();
        Matrix gr = getPotentialMatrix();
        return ((tr.transpose().times(gr)).times(tr));
    }

    public void addInGlobalDisplacementCoordinate(double[] localDisplacement) {
        double[][] temp = new double[localDisplacement.length][1];
        for (int i = 0; i < localDisplacement.length; i++) {
            temp[i][0] = localDisplacement[i];
        }
        setGlobalDisplacementInPoint(localDisplacement);
        displacementInGlobalSystem = new Matrix(temp);
        displacementInLocalSystem = getTr().times(displacementInGlobalSystem);
        internalForce = getStiffenerMatrix().times(displacementInLocalSystem);
    }

    protected abstract void setGlobalDisplacementInPoint(double[] localDisplacement);


}
