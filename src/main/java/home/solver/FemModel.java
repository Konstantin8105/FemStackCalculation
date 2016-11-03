package home.solver;

import Jama.Matrix;
import home.finiteElements.FemTruss2d;
import home.finiteElements.interfaces.FemElement;
import home.other.FemPoint;
import home.other.Support;

public class FemModel {

    public FemModel(
            FemPoint[] femPoints,
            FemElement[] femElements,
            Support[] supports) throws Exception {

        int aColumn = 3 * femPoints.length;
        int aRow = 3 * femElements.length;


        for (FemElement element : femElements) {
            Matrix globalStiffener = element.getStiffenerMatrixTr();
            globalStiffener.print(15, 1);


        }

    }

    public static void main(String[] args) throws Exception {

        FemPoint[] femPoints = new FemPoint[2];
        femPoints[0] = new FemPoint(0, 0, 0);
        femPoints[1] = new FemPoint(0, 1, 0);

        FemTruss2d fem = new FemTruss2d(1, 1, new FemPoint[]{femPoints[0], femPoints[1]});

        Matrix globalStiffener = fem.getStiffenerMatrixTr();
        globalStiffener.print(15, 1);

    }
}
