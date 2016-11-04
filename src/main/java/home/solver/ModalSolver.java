package home.solver;

import Jama.Matrix;
import home.finiteElements.interfaces.FemElement;
import home.other.FemPoint;
import home.other.Force;
import home.other.Support;
import home.solver.matrixes.SparseZeroOneMatrix;

public class ModalSolver extends Solver {

    public static Matrix[] calculate(
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
        SparseZeroOneMatrix A = generateMatrixCompliance(femPoints, femElements);
        //List<Integer>[] A2 = generateMatrixCompliance2(femPoints, femElements);
        if (DEBUG) A.convert().print(3, 0);

        if (DEBUG) System.out.println("Start calc Kok");
        Matrix Kok = generateMatrixQuasiStiffener(femElements).convert();
        if (DEBUG) Kok.print(15, 1);

        if (DEBUG) System.out.println("Start calc Ko");
        //Matrix Ko = calculate(A2, Kok);
        //TODO optimize
        Matrix Ko = (A.convert().transpose().times(Kok)).times(A.convert());
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

        Matrix Mok = generateQuasiMatrixMass(femElements);
        if (DEBUG) System.out.println("Mok");
        if (DEBUG) Mok.print(12, 1);

        Matrix Mo = (A.convert().transpose().times(Mok)).times(A.convert());
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

                Mo.getArray()[axe][axe] += forceAmplitude;
            }
        }

        //Matrix M = putZeroInSupportRowColumns(addPointMass(Mo, forces), supports);
        Matrix M = putZeroInSupportRowColumns(Mo, supports);
        if (DEBUG) System.out.println("M");
        if (DEBUG) M.print(12, 1);

        M = M.times(1./9.81);

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

//        System.out.println("N\tvalue\t\t2*PI/sqrt(value)");
//        for (int i = 0; i < value[0].getArray().length; i++) {
//            System.out.println(String.format("%3d", i)
//                    + " = "
//                    + String.format("%20.2f", value[0].getArray()[i][0])
//                    + " ---> "
//                    + String.format("%.5f", 2. * Math.PI /Math.sqrt(value[0].getArray()[i][0])) + " sec."
//            );
//        }
//
//        System.out.println("First wave");
//        for (int i = 0; i < value[1].getArray().length; i++) {
//            System.out.println(String.format("%10.3f",value[1].getArray()[i][0]));
//        }

        FemElement.dropNumeration();
        FemPoint.dropNumeration();

        return value;
    }
}
