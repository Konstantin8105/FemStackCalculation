package home.solver;

import Jama.Matrix;
import home.finiteElement.FemElement;
import home.other.FemPoint;
import home.other.Force;
import home.other.Support;

public class StrengthSolver extends Solver {

    public static void calculate(
            FemPoint[] femPoints,
            FemElement[] femElements,
            Force[] forces,
            Support[] supports) throws Exception {

        FemElement.dropNumeration();
        FemPoint.dropNumeration();

        convertPointGlobalAxeToNumber = convertPointAxeToSequenceAxe(femElements);
        convertLineGlobalAxeToNumber = convertLineAxeToSequenceAxe(femElements);

        if (DEBUG) System.out.println("Start calc A");
        //TODO optimize
        Matrix A = generateMatrixCompliance(femPoints, femElements);
        //List<Integer>[] A2 = generateMatrixCompliance2(femPoints, femElements);
        if (DEBUG) A.print(3, 0);

        if (DEBUG) System.out.println("Start calc Kok");
        Matrix Kok = generateMatrixQuasiStiffener(femElements);
        if (DEBUG) Kok.print(15, 1);

        if (DEBUG) System.out.println("Start calc Ko");
        //Matrix Ko = calculate(A2, Kok);
        //TODO optimize
        Matrix Ko = (A.transpose().times(Kok)).times(A);
        if (DEBUG) Ko.print(15, 1);

        if (DEBUG) System.out.println("Start calc K");
        Matrix K = putZeroInSupportRowColumns(Ko, supports);
        if (DEBUG) K.print(15, 1);

        if (DEBUG) {
            for (int i = 0; i < K.getArray().length; i++) {
                System.out.println("[" + i + "," + i + "]=" + K.getArray()[i][i]);
            }
        }

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
            Matrix Z0k = A.times(Z0);
            if (DEBUG) Z0k.print(10, 6);

            if (DEBUG) System.out.println("Start calc localDisplacement");
            int sizeAxes = femElements[0].getLocalAxes().length;
            for (int i = 0; i < femElements.length; i++) {
                double[] localDisplacement = new double[sizeAxes];
                for (int j = 0; j < localDisplacement.length; j++) {
                    localDisplacement[j] = Z0k.getArray()[i * sizeAxes + j][0];
                }
                femElements[i].addInGlobalDisplacementCoordinate(localDisplacement);
            }
            if (DEBUG) {
                for (FemElement femElement : femElements) {
                    System.out.println(femElement);
                    femElement.getInternalForce().print(10, 1);
                }
            }

            if (DEBUG) {
                Matrix Z01 = K.solve(forceVector);
                System.out.println("Z01");
                Z01.print(15, 10);
                Matrix G0 = K.solve(Z01);
                System.out.println("G0");
                G0.print(15, 10);
            }

            if (DEBUG) {
                for (int i = 0; i < femPoints.length; i++) {
                    System.out.println("[" + i + "]");
                    for (int j = 0; j < femPoints[i].getGlobalDisplacement().length; j++) {
                        System.out.println("\t " + femPoints[i].getGlobalDisplacement()[j]);
                    }
                }
            }
        }

        FemElement.dropNumeration();
        FemPoint.dropNumeration();
    }

}
