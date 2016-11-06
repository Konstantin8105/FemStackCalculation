package home.solver;

import Jama.Matrix;
import home.finiteElements.interfaces.FemElement;
import home.other.FemPoint;
import home.other.MassPoint;
import home.other.Support;
import home.solver.matrixes.SparseSquareSymmetricMatrix;
import home.solver.matrixes.SparseZeroOneMatrix;

import java.util.ArrayList;
import java.util.List;

public class ModalSolver extends Solver {

    private static final double THE_ACCELERATION_OF_GRAVITY = 9.806;

    public class Mode {
        double period;
        double[] waveFactor;

        public double getPeriod() {
            return period;
        }

        public double[] getWaveFactor() {
            return waveFactor;
        }
    }

    List<Mode> modes = new ArrayList<>();

    public ModalSolver(
            FemPoint[] femPoints,
            FemElement[] femElements,
            MassPoint[] massPoints,
            Support[] supports) throws Exception {

        FemElement.dropNumeration();
        FemPoint.dropNumeration();

        convertPointGlobalAxeToNumber = convertPointAxeToSequenceAxe(femElements);
        convertLineGlobalAxeToNumber = convertLineAxeToSequenceAxe(femElements);

        SparseZeroOneMatrix A = generateMatrixCompliance(femPoints, femElements);
        Matrix Ko = Solver.matrixStiffeners(femPoints, femElements);

        SparseSquareSymmetricMatrix Mok = generateQuasiMatrixMass(femElements);
        Matrix Mo = SparseZeroOneMatrix.multiplyWithSquareSymmetric(A.transpose().times(Mok), A);

        if (massPoints != null) {
            for (MassPoint massPoint : massPoints) {
                double forceAmplitude = massPoint.getAmplitude();
                for (int i = 0; i < 2; i++) {
                    int axe = massPoint.getFemPoint().getNumberGlobalAxe()[i];
                    axe = convertPointGlobalAxeToNumber.get(axe);
                    Mo.getArray()[axe][axe] += forceAmplitude;
                }
            }
        }

        Matrix K = putZeroInSupportRowColumns(Ko, supports);
        Matrix M = putZeroInSupportRowColumns(Mo, supports);

        K = deleteFewColumnsRows(K, supports);
        M = deleteFewColumnsRows(M, supports);

//        Matrix K = deleteFewColumnsRows(Ko, supports);
//        Matrix M = deleteFewColumnsRows(Mo, supports);

        M = M.times(1. / THE_ACCELERATION_OF_GRAVITY);

//        int[] deleteColumnsRows = getZeroColumnsRows(M);
//        K = deleteFewColumnsRows(K, deleteColumnsRows);
//        M = deleteFewColumnsRows(M, deleteColumnsRows);

        Matrix[] value = Solver.calculateEigen(K, M);
        for (int i = 0; i < value[0].getRowDimension(); i++) {
            Mode mode = new Mode();
            mode.period = 2. * Math.PI / Math.sqrt(value[0].get(i, 0));
            // todo eigen vector
            modes.add(mode);
        }
    }


    static private SparseSquareSymmetricMatrix generateQuasiMatrixMass(final FemElement[] femElements) {
        int amount = 0;
        int maxSizeInternalMatrix = 0;
        for (int i = 0; i < femElements.length; i++) {
            int size = femElements[i].getPoint().length * FemPoint.AMOUNT_POINT_AXES;
            amount += size;
            maxSizeInternalMatrix = maxSizeInternalMatrix < size ? size : maxSizeInternalMatrix;
        }

        SparseSquareSymmetricMatrix kok = new SparseSquareSymmetricMatrix(amount, maxSizeInternalMatrix);
        for (int i = 0; i < femElements.length; i++) {
            int sizeAxes = femElements[i].getPoint().length * FemPoint.AMOUNT_POINT_AXES;
            int positionBaseLine = i * sizeAxes;
            Matrix ks = femElements[i].getMatrixMassTr();
            kok.add(positionBaseLine, ks);
        }

        return kok;
    }

    public List<Mode> getModes() {
        return modes;
    }
}
