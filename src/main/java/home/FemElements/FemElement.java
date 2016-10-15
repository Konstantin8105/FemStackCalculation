package home.FemElements;

import home.MKEPoint;
import jama.Matrix;

public interface FemElement {
    Matrix getTr();

    Matrix getStiffenerMatrix();

    int[] getAxes();

    MKEPoint[] getPoint();
}
