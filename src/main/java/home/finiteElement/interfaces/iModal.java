package home.finiteElement.interfaces;

import Jama.Matrix;

public interface iModal extends iStrength {

    Matrix getMatrixMass();

    default Matrix getMatrixMassTr() {
        Matrix tr = getTr();
        Matrix gr = getMatrixMass();
        return ((tr.transpose().times(gr)).times(tr));
    }
}
