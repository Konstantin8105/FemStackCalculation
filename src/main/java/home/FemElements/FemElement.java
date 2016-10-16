package home.FemElements;

import home.MKEPoint;
import jama.Matrix;

public abstract class FemElement {

    static private int global_number = 0;

    protected static int getGlobalNumber() {
        return global_number++;
    }

    protected Matrix displacementInGlobalSystem;
    protected Matrix displacementInLocalSystem;
    protected Matrix internalForce;
    protected int axes[];
    protected MKEPoint point[];

    abstract public Matrix getTr();

    abstract public Matrix getStiffenerMatrix();

    abstract public Matrix getStiffenerMatrixTr();

    abstract public void addInGlobalDisplacementCoordinate(double[] localDisplacement);

    public int[] getAxes() {
        return axes;
    }

    public MKEPoint[] getPoint() {
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
}
