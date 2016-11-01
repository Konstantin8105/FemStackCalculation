package home.finiteElement;

import Jama.Matrix;
import home.other.FemPoint;

public abstract class FemElement {

    private static int global_number = 0;

    private static int getGlobalNumber() {
        return global_number++;
    }

    public static void dropNumeration() {
        global_number = 0;
    }

    private final int[] axes;
    final FemPoint[] point;
    private final double length;

    FemElement(FemPoint[] point) {
        this.point = point;
        axes = new int[getAmountAxes()];
        for (int i = 0; i < axes.length; i++) {
            axes[i] = getGlobalNumber();
        }
        length = Math.sqrt(Math.pow(point[0].getX() - point[1].getX(), 2.) + Math.pow(point[0].getY() - point[1].getY(), 2.));
        нам нужен конвертор от осей точек на оси линии
    }

    double getLength() {
        return length;
    }

    abstract protected int getAmountAxes();

    abstract protected Matrix getTr();

    abstract protected Matrix getStiffenerMatrix();
    public int[] getAxes() {
        return axes;
    }

    public FemPoint[] getPoint() {
        return point;
    }

    private Matrix displacementInGlobalSystem;
    public Matrix getDisplacementInGlobalSystem() {
        return displacementInGlobalSystem;
    }

    private Matrix displacementInLocalSystem;
    public Matrix getDisplacementInLocalSystem() {
        return displacementInLocalSystem;
    }

    private Matrix internalForce;
    public Matrix getInternalForce() {
        return internalForce;
    }

    public Matrix getStiffenerMatrixTr() {
        Matrix tr = getTr();
        Matrix kr = getStiffenerMatrix();
        return ((tr.transpose().times(kr)).times(tr));
    }
//
//    public Matrix getStiffenerMatrixTr2() {
//        Matrix tr = getTr();
//        Matrix kr = getStiffenerMatrix2();
//        return ((tr.transpose().times(kr)).times(tr));
//    }

//    public Matrix getPotentialMatrixTr2() {
//        Matrix tr = getTr();
//        Matrix gr = getPotentialMatrix2();
//        return ((tr.transpose().times(gr)).times(tr));
//    }

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

//    private double bucklingFactor;
//
//    public void setBucklingFactor(double bucklingFactor) {
//        this.bucklingFactor = bucklingFactor;
//    }
//
//    public double getBucklingFactor() {
//        return bucklingFactor;
//    }
//
//    public double getBucklingAxialLoad() {
//        return bucklingFactor * internalForce.getArray()[0][0];
//    }

//    public abstract Matrix getStiffenerMatrix2();
//
//    protected abstract Matrix getPotentialMatrix2();

    protected abstract void setGlobalDisplacementInPoint(double[] localDisplacement);

}
