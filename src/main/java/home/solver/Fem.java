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

        if (DEBUG) System.out.println("Start calc forceVector");
        Matrix forceVector = generateForceVector(femPoints, forces, femElements[0].getAxes().length / 2);
        if (DEBUG) forceVector.print(10, 1);

        if (DEBUG) System.out.println("Start calc K");
        Matrix K = generateMatrixStiffener(Ko, supports);
        if (DEBUG) K.print(15, 1);

        if(DEBUG){
            for (int i = 0; i < K.getArray().length; i++) {
                System.out.println("["+i+","+i+"]="+K.getArray()[i][i]);
            }
        }
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

        if(DEBUG){
            Matrix Z01 = K.solve(forceVector);
            Matrix G0 = K.solve(Z01);
            System.out.println("G0");
            G0.print(15,10);
        }

        if (haveBucklingAnalyze) {
//            if (DEBUG) System.out.println("Start buckling analyze");
//            Matrix Gok = generateMatrixQuasiPotentialStiffener(femElements);
//            System.out.println("Gok");Gok.print(13,10);
//            Matrix Go = (A.transpose().times(Gok)).times(A);
//            System.out.println("Go");Go.print(13,10);
//            Matrix G = generateMatrixStiffener(Go, supports);
//            //G = G.times(-7000);
//            System.out.println("G");G.print(13,10);
//            Matrix H = K.solve(G);
//            //Matrix H = K.inverse().times(G);
//            System.out.println("H");H.print(13,10);
//            EigenvalueDecomposition ei = H.eig();//new EigenvalueDecomposition(H);
//            System.out.println("ei.getD()");ei.getD().print(12,9);
//            System.out.println("ei.getV()");ei.getV().print(10,5);
//            //System.out.println("ei.getD().times(Z0)");ei.getD().times(Z0).print(10,5);
//            //System.out.println("ei.getV().times(Z0)");ei.getV().times(Z0).print(10,5);
//            for (int i = 0; i < ei.getRealEigenvalues().length; i++) {
//                System.out.println("Real "+ei.getRealEigenvalues()[i]);
//                //if(ei.getRealEigenvalues()[i]!=0)System.out.println("1/Real ="+1./ei.getRealEigenvalues()[i]);
//            }
//
//            for (int i = 0; i < ei.getImagEigenvalues().length; i++) {
//                System.out.println("Image "+ei.getImagEigenvalues()[i]);
//            }
        }
        if(haveBucklingAnalyze){

            A = generateMatrixCompliance(femPoints, femElements);

            if (DEBUG) System.out.println("\n\n\n\n\n\n\n\n\n\n");
            Matrix Kok2 = generateMatrixQuasiStiffener2(femElements);
            Matrix Ko2 = (A.transpose().times(Kok2)).times(A);
            if (DEBUG) System.out.println("Ko2");
            if (DEBUG) Ko2.print(12,1);

            Matrix K2 = generateMatrixStiffener(Ko2, supports);
            if (DEBUG) System.out.println("K2");
            if (DEBUG) K2.print(12,1);

            K2 = deleteFewColumnsRows(K2,supports);
            //K2 = generateMatrixStiffener(K2, supports);
            if (DEBUG) System.out.println("K2");
            if (DEBUG) K2.print(12,1);

            Matrix Gok = generateMatrixQuasiPotentialStiffener2(femElements);
            if (DEBUG) System.out.println("Gok");
            if (DEBUG) Gok.print(12,1);

            Matrix Go = (A.transpose().times(Gok)).times(A);
            if (DEBUG) System.out.println("Go");
            if (DEBUG) Go.print(12,1);
            Matrix G = generateMatrixStiffener(Go, supports);
            if (DEBUG) System.out.println("G");
            if (DEBUG) G.print(12,1);

            G = deleteFewColumnsRows(G,supports);
            //G = generateMatrixStiffener(G, supports);
            if (DEBUG) System.out.println("G");
            if (DEBUG) G.print(12,1);

            //todo optimize
            Matrix H = K2.solve(G);
            if (DEBUG) System.out.println("H");
            if (DEBUG) H.print(12,1);
            H = deleteBad(H);
            if (DEBUG) System.out.println("H");
            if (DEBUG) H.print(12,1);

            //todo optimize
            EigenvalueDecomposition ei = H.eig();//new EigenvalueDecomposition(H);
            if (DEBUG) System.out.println("ei.getD()");
            if (DEBUG) ei.getD().print(12,9);
            if (DEBUG) System.out.println("ei.getV()");
            if (DEBUG) ei.getV().print(10,5);
            //System.out.println("ei.getD().times(Z0)");ei.getD().times(Z0).print(10,5);
            //System.out.println("ei.getV().times(Z0)");ei.getV().times(Z0).print(10,5);
            if (DEBUG) for (int i = 0; i < ei.getRealEigenvalues().length; i++) {
                System.out.println("Real "+ei.getRealEigenvalues()[i]);
                if(ei.getRealEigenvalues()[i]!=0)System.out.println("1/Real ="+1./ei.getRealEigenvalues()[i]);
            }

            double maxRe = ei.getRealEigenvalues()[0];
            for (int i = 0; i < ei.getRealEigenvalues().length; i++) {
                maxRe = Math.max(maxRe,ei.getRealEigenvalues()[i]);
            }

            for (int i = 0; i < femElements.length; i++) {
                femElements[i].setBucklingFactor(1./maxRe);
            }

//            for (int i = 0; i < ei.getImagEigenvalues().length; i++) {
//                System.out.println("Image "+ei.getImagEigenvalues()[i]);
//            }
        }

        FemElement.dropNumeration();
        FemPoint.dropNumeration();
    }

}
