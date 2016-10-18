package home.solver;

import home.finiteElement.FemElement;
import home.other.FemPoint;
import home.other.Force;
import home.other.Support;
import jama.EigenvalueDecomposition;
import jama.Matrix;

public class Fem extends Solver {


    public static void calculate(
            FemPoint[] femPoints,
            FemElement[] femElements,
            Force[] forces,
            Support[] supports) throws Exception {
        calculate(femPoints,
                femElements,
                forces,
                supports,
                false);
    }

    public static void calculate(
            FemPoint[] femPoints,
            FemElement[] femElements,
            Force[] forces,
            Support[] supports,
            boolean haveBucklingAnalyze) throws Exception {

        convertPointGlobalAxeToNumber = convertPointAxeToSequenceAxe(femElements);
        convertLineGlobalAxeToNumber = convertLineAxeToSequenceAxe(femElements);

        if (DEBUG) System.out.println("Start calc A");
        //TODO optimize
        Matrix A = generateMatrixCompliance(femPoints, femElements);
        //List<Integer>[] A2 = generateMatrixCompliance2(femPoints, femElements);
        if (DEBUG) A.print(5, 0);

        if (DEBUG) System.out.println("Start calc Kok");
        Matrix Kok = generateMatrixQuasiStiffener(femElements);

        if (DEBUG) System.out.println("Start calc Ko");
        //Matrix Ko = calculate(A2, Kok);
        //TODO optimize
        Matrix Ko = (A.transpose().times(Kok)).times(A);

        if (DEBUG) System.out.println("Start calc forceVector");
        Matrix forceVector = generateForceVector(femPoints, forces, femElements[0].getAxes().length / 2);

        if (DEBUG) System.out.println("Start calc K");
        Matrix K = generateMatrixStiffener(Ko, supports);
        if (DEBUG) K.print(10, 1);

        if (DEBUG) System.out.println("Start calc Z0");
        //TODO optimize
        Matrix Z0 = K.solve(forceVector);
        if (DEBUG) Z0.print(10, 6);

        if (DEBUG) System.out.println("Start calc Z0k");
        //TODO optimize
        Matrix Z0k = A.times(Z0);
        if (DEBUG) Z0k.print(10, 6);

        if (DEBUG) System.out.println("Start calc localDisplacement");
        int sizeAxes = femElements[0].getAxes().length;
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

        if (haveBucklingAnalyze) {
            if (DEBUG) System.out.println("Start buckling analyze");
            Matrix Gok = generateMatrixQuasiPotentialStiffener(femElements);
            Matrix Go = (A.transpose().times(Gok)).times(A);
            System.out.println("Go");Go.print(15,10);
            Matrix G = generateMatrixStiffener(Go, supports);
            System.out.println("G");G.print(15,10);
            Matrix H = K.solve(G);
            System.out.println("H");H.print(15,10);
            EigenvalueDecomposition ei = new EigenvalueDecomposition(H);
            System.out.println("ei");ei.getD().print(12,9);
//            ei.getV().print(10,5);
//            ei.getD().times(Z0).print(10,5);
//            ei.getV().times(Z0).print(10,5);
            for (int i = 0; i < ei.getRealEigenvalues().length; i++) {
                System.out.println("Real "+ei.getRealEigenvalues()[i]);
            }

//            for (int i = 0; i < ei.getImagEigenvalues().length; i++) {
//                System.out.println("Image "+ei.getImagEigenvalues()[i]);
//            }
//            Matrix p = ei.getV().times(Z0);
//            p.print(10,10);
//            for (int i = 0; i < p.getColumnDimension(); i++) {
//                for (int j = 0; j < p.getRowDimension(); j++) {
//                    if(p.getArray()[j][i] > 0.0000001 || -0.0000001 > p.getArray()[j][i])
//                        System.out.println("["+j+","+i+"] = "+p.getArray()[j][i]+" ---> "+(1./p.getArray()[j][i]));
//                    else
//                        System.out.println("["+j+","+i+"] = "+p.getArray()[j][i]);
//                }
//            }
        }

        FemElement.dropNumeration();
        FemPoint.dropNumeration();
    }


}
