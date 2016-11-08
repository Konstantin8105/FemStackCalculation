package home.solver;

import Jama.Matrix;
import home.finiteElements.interfaces.FemElement;
import home.other.FemPoint;
import home.loads.Force;
import home.other.Support;
import home.solver.matrixes.SparseZeroOneMatrix;

public class BucklingSolver extends Solver {

    public BucklingSolver(
            FemPoint[] femPoints,
            FemElement[] femElements,
            Force[] forces,
            Support[] supports) throws Exception {

        FemElement.dropNumeration();
        FemPoint.dropNumeration();

        convertPointGlobalAxeToNumber = convertPointAxeToSequenceAxe(femElements);
        convertLineGlobalAxeToNumber = convertLineAxeToSequenceAxe(femElements);

        SparseZeroOneMatrix A = generateMatrixCompliance(femPoints, femElements);
        Matrix Ko = Solver.matrixStiffeners(femPoints, femElements);

        Matrix K = putZeroInSupportRowColumns(Ko, supports);

        if (forces.length > 0) {
            if (DEBUG) System.out.println("Start calc forceVector");
            Matrix forceVector = generateForceVector(femPoints, forces, femElements[0].getLocalAxes().length / 2);
            if (DEBUG) forceVector.print(10, 1);

            if (DEBUG) System.out.println("Start calc Z0");
            //TODO optimize
            Matrix Z0 = K.solve(forceVector);
            if (DEBUG) Z0.print(10, 6);


            if (DEBUG) System.out.println("Start calc Z0k");
            //TODO optimize
            Matrix Z0k = A.convert().times(Z0);
            if (DEBUG) Z0k.print(10, 6);
        }

        FemElement.dropNumeration();
        FemPoint.dropNumeration();
    }

}
