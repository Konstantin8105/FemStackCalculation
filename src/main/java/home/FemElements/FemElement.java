package home.FemElements;

import home.MKEPoint;
import jama.Matrix;

public interface FemElement {
    Matrix getTr();

    Matrix getStiffenerMatrix();

    Matrix getStiffenerMatrixTr();

    int[] getAxes();

    MKEPoint[] getPoint();

    void addInGlobalDisplacementCoordinate(double[] localDisplacement);

    Matrix getDisplacementInGlobalSystem();

    Matrix getDisplacementInLocalSystem();

    Matrix getInternalForce();
}
