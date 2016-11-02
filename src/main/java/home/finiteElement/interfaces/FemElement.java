package home.finiteElement.interfaces;

import Jama.Matrix;
import home.other.FemPoint;

public abstract class FemElement {

    static private int global_number_id = -1;

    private static int global_number = 0;

    private static int getGlobalNumber() {
        return global_number++;
    }

    public static void dropNumeration() {
        global_number = 0;
    }

    private final int[] localAxes;
    final protected FemPoint[] point;
    private final double length;
    private int id;

    protected FemElement(FemPoint[] point) {
        this.point = point;
        localAxes = new int[6];
        for (int i = 0; i < localAxes.length; i++) {
            if (getAxeAllowable()[i])
                localAxes[i] = getGlobalNumber();
            else localAxes[i] = -1;
        }
        length = Math.sqrt(Math.pow(point[0].getX() - point[1].getX(), 2.) + Math.pow(point[0].getY() - point[1].getY(), 2.));
        id = global_number_id++;
    }

    protected double getLength() {
        return length;
    }

    public abstract boolean[] getAxeAllowable();//return boolean array with size = 6

//    public int getAmountLocalAxes() {
//        int amount = 0;
//        for (int i = 0; i < getAxeAllowable().length; i++) {
//            amount++;
//        }
//        return amount;
//    }

    public int getId() {
        return id;
    }

    public int[] getLocalAxes() {
        return localAxes;
    }

    public FemPoint[] getPoint() {
        return point;
    }


    // Strength

    public abstract Matrix getTr();

    public abstract Matrix getStiffenerMatrix();

    public Matrix getStiffenerMatrixTr() {
        Matrix tr = getTr();
        Matrix kr = getStiffenerMatrix();
        return ((tr.transpose().times(kr)).times(tr));
    }

    // Buckling

    public abstract Matrix getPotentialMatrix();

    Matrix getPotentialMatrixTr() {
        Matrix tr = getTr();
        Matrix gr = getPotentialMatrix();
        return ((tr.transpose().times(gr)).times(tr));
    }

    // Modal

    public abstract Matrix getMatrixMass();

    public Matrix getMatrixMassTr() {
        Matrix tr = getTr();
        Matrix gr = getMatrixMass();
        return ((tr.transpose().times(gr)).times(tr));
    }
}
