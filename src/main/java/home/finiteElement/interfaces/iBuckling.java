package home.finiteElement.interfaces;

import Jama.Matrix;

public interface iBuckling extends iStrength {

    Matrix getPotentialMatrix();

    default Matrix getPotentialMatrixTr() {
        Matrix tr = getTr();
        Matrix gr = getPotentialMatrix();
        return ((tr.transpose().times(gr)).times(tr));
    }

}
