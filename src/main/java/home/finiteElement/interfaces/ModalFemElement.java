package home.finiteElement.interfaces;

import Jama.Matrix;
import home.other.FemPoint;

public abstract class ModalFemElement extends FemElement {

    protected ModalFemElement(FemPoint[] point) {
        super(point);
    }

    protected abstract Matrix getMatrixMass();

    public Matrix getMatrixMassTr() {//Force force
        Matrix tr = getTr();
        Matrix gr = getMatrixMass();
//        double forceAmplitude = Math.abs(force.getAmplitude());
//        if (forceAmplitude > 0) {
//            double halfForce = forceAmplitude / 2.;
//            gr.getArray()[0][0] += halfForce;
//            gr.getArray()[1][1] += halfForce;
//            gr.getArray()[3][3] += halfForce;
//            gr.getArray()[4][4] += halfForce;
//        }
        return ((tr.transpose().times(gr)).times(tr));
    }

}
