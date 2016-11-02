package home.finiteElement.interfaces;

import Jama.Matrix;

public interface iStrength {

    Matrix getTr();

    Matrix getStiffenerMatrix();

    void setGlobalDisplacementInPoint(double[] localDisplacement);

    default Matrix getStiffenerMatrixTr() {
        Matrix tr = getTr();
        Matrix kr = getStiffenerMatrix();
        return ((tr.transpose().times(kr)).times(tr));
    }
}
