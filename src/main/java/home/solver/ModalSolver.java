package home.solver;

import Jama.Matrix;
import home.finiteElement.FemElement;
import home.finiteElement.ModalFemElement;
import home.other.FemPoint;
import home.other.Force;
import home.other.Support;

public class ModalSolver extends Solver {

    public static Matrix[] calculate(
            FemPoint[] femPoints,
            ModalFemElement[] femElements,
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

        // modal analyze
        A = generateMatrixCompliance(femPoints, femElements);

        Matrix Mok = generateQuasiMatrixMass(femElements);//, forces
        if (DEBUG) System.out.println("Mok");
        if (DEBUG) Mok.print(12, 1);

/*
        if(forces != null) {
            for (Force force : forces) {
                double forceAmplitude = force.getAmplitude();
                FemPoint point = force.getFemPoint();
                int axes[] = point.getNumberGlobalAxe();
                //todo wrong because force in local system but not in global system
//                switch (force.getDirection()){
//                    case DIRECTION_X:
//                Mok.getArray()[convertPointGlobalAxeToNumber.get(axes[0])][convertPointGlobalAxeToNumber.get(axes[0])] += forceAmplitude;
//                        break;
//                    case DIRECTION_Y:
                Mok.getArray()[convertPointGlobalAxeToNumber.get(axes[1])][convertPointGlobalAxeToNumber.get(axes[1])] += forceAmplitude;
//                        break;
//                    case ROTATE:
//                Mok.getArray()[convertPointGlobalAxeToNumber.get(axes[2])][convertPointGlobalAxeToNumber.get(axes[2])] += forceAmplitude;
//                        break;
//                }
            }
        }
        */

        Matrix Mo = (A.transpose().times(Mok)).times(A);
        if (DEBUG) System.out.println("Mo");
        if (DEBUG) Mo.print(12, 1);


        if (forces != null) {
            for (Force force : forces) {
                double forceAmplitude = force.getAmplitude();
                int axe = -1;
                switch (force.getDirection()) {
                    case DIRECTION_X:
                        axe = force.getFemPoint().getNumberGlobalAxe()[0];
                        break;
                    case DIRECTION_Y:
                        axe = force.getFemPoint().getNumberGlobalAxe()[1];
                        break;
                    case ROTATE:
                        axe = force.getFemPoint().getNumberGlobalAxe()[2];
                        break;
                }
                axe = convertPointGlobalAxeToNumber.get(axe);

                //todo wrong because force in local system but not in global system
                Mo.getArray()[axe][axe] += forceAmplitude;
            }
        }

        //Matrix M = putZeroInSupportRowColumns(addPointMass(Mo, forces), supports);
        Matrix M = putZeroInSupportRowColumns(Mo, supports);
        if (DEBUG) System.out.println("M");
        if (DEBUG) M.print(12, 1);

        K = deleteFewColumnsRows(K, supports);
        M = deleteFewColumnsRows(M, supports);

//        int[] deleteColumnsRows = getZeroColumnsRows(M);
//        K = deleteFewColumnsRows(K, deleteColumnsRows);
//        M = deleteFewColumnsRows(M, deleteColumnsRows);

        if (DEBUG) System.out.println("M");
        if (DEBUG) M.print(12, 1);

        if (DEBUG) System.out.println("K");
        if (DEBUG) K.print(12, 1);

        Matrix[] value = Solver.calculateEigen(K, M);

//        System.out.println("N\tvalue\t\t1/sqrt(value)\t\tsqrt(value)");
//        for (int i = 0; i < value[0].getArray().length; i++) {
//            System.out.println(String.format("%3d", i)
//                    + " = "
//                    + String.format("%.3e", value[0].getArray()[i][0])
//                    + " ---> "
//                    + String.format("%.3f", (1. / Math.sqrt(value[0].getArray()[i][0])))
//                    + " ---> "
//                    + String.format("%.3e", (Math.sqrt(value[0].getArray()[i][0])))
//            );
//        }
//
//        System.out.println("First wave");
//        for (int i = 0; i < value[1].getArray().length; i++) {
//            System.out.println(value[1].getArray()[i][0]);
//        }

        FemElement.dropNumeration();
        FemPoint.dropNumeration();

        return value;
    }
}
