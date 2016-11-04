package home.solver;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import home.finiteElements.interfaces.FemElement;
import home.other.FemPoint;
import home.other.Force;
import home.other.Support;
import home.solver.matrixes.SparseSquareSymmetricMatrix;
import home.solver.matrixes.SparseZeroOneMatrix;

import java.util.*;

public class Solver {

    static final boolean DEBUG = false;

    static Map<Integer, Integer> convertPointGlobalAxeToNumber;
    static Map<Integer, Integer> convertLineGlobalAxeToNumber;

    static Map<Integer, Integer> convertLineAxeToSequenceAxe(FemElement[] femElements) {
        Set<Integer> listOfLineAxes = new HashSet<>();
        for (int h = femElements.length - 1; h >= 0; h--) {
            FemElement element = femElements[h];
            for (int i = 0; i < element.getPoint().length; i++) {
                for (int j = 0; j < element.getLocalAxes().length; j++) {
                    listOfLineAxes.add(element.getLocalAxes()[j]);
                }
            }
        }
        Map<Integer, Integer> map = new TreeMap<>();
        int position = 0;
        for (Integer number : listOfLineAxes) {
            map.put(number, position++);
        }
        return map;
    }

    static Map<Integer, Integer> convertPointAxeToSequenceAxe(FemElement[] femElements) throws Exception {
        List<Integer> listOfLineAxes = new ArrayList<>();
        for (FemElement element : femElements) {
            for (int i = 0; i < element.getPoint().length; i++) {
                for (int j = 0; j < FemPoint.AMOUNT_POINT_AXES; j++) {
                    listOfLineAxes.add(element.getPoint()[i].getNumberGlobalAxe()[j]);
                }
            }
        }
        Collections.sort(listOfLineAxes);
        Iterator<Integer> iterator = listOfLineAxes.iterator();
        int last = -1;
        while (iterator.hasNext()) {
            int value = iterator.next();
            if (last == -1) {
                last = value;
            } else if (value == last) {
                iterator.remove();
            } else {
                last = value;
            }
        }
        Map<Integer, Integer> map = new TreeMap<>();
        int position = 0;
        for (Integer number : listOfLineAxes) {
            map.put(number, position++);
        }
        return map;
    }

    static SparseZeroOneMatrix generateMatrixCompliance(FemPoint[] femPoints, FemElement[] elements) throws Exception {
        //Y0
        //...^
        //...|
        //...|
        //...^1
        //...|....0
        //...+---->---> X0

        int elementAxes = 2 * FemPoint.AMOUNT_POINT_AXES;
        int pointAxes = FemPoint.AMOUNT_POINT_AXES;

        SparseZeroOneMatrix a = new SparseZeroOneMatrix(elements.length * elementAxes, femPoints.length * pointAxes);

        for (FemElement element : elements) {
            for (int i = 0; i < element.getPoint().length * FemPoint.AMOUNT_POINT_AXES; i++) {
                int row = convertLineGlobalAxeToNumber.get(element.getLocalAxes()[i]);
                int axe = i % 3;
                int point = i / 3;
                int column = convertPointGlobalAxeToNumber.get(element.getPoint()[point].getNumberGlobalAxe()[axe]);
                a.addOne(row, column);
            }
        }

        return a;
    }

    static SparseSquareSymmetricMatrix generateMatrixQuasiStiffener(FemElement[] femElements) {
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
            Matrix ks = femElements[i].getStiffenerMatrixTr();
            kok.add(positionBaseLine, ks);
        }

        return kok;
    }

    //
//    static Matrix generateMatrixQuasiPotentialStiffener(FemElement[] femElements) {
//
//        //TODO optimize to diagonal matrix
//        int sizeAxes = femElements[0].getLocalAxes().length;
//
//        double[][] gok = new double[femElements.length * sizeAxes][femElements.length * sizeAxes];
//        for (int i = 0; i < femElements.length; i++) {
//            int positionBaseLine = i * sizeAxes;
//            Matrix ks = femElements[i].getPotentialMatrixTr();
//            for (int j = 0; j < sizeAxes; j++) {
//                for (int k = 0; k < sizeAxes; k++) {
//                    gok[positionBaseLine + j][positionBaseLine + k] = ks.getArray()[j][k];
//                }
//            }
//        }
//
//        return new Matrix(gok);
//    }
//
//    static Matrix generateMatrixQuasiStiffener2(FemElement[] femElements) {
//
//        //TODO optimize to diagonal matrix
//        int sizeAxes = femElements[0].getLocalAxes().length;
//
//        double[][] kok = new double[femElements.length * sizeAxes][femElements.length * sizeAxes];
//        for (int i = 0; i < femElements.length; i++) {
//            int positionBaseLine = i * sizeAxes;
//            Matrix ks = femElements[i].getStiffenerMatrixTr2();
//            for (int j = 0; j < sizeAxes; j++) {
//                for (int k = 0; k < sizeAxes; k++) {
//                    kok[positionBaseLine + j][positionBaseLine + k] = ks.getArray()[j][k];
//                }
//            }
//        }
//
//        return new Matrix(kok);
//    }
//
//    static Matrix generateMatrixQuasiPotentialStiffener2(FemElement[] femElements) {
//
//        //TODO optimize to diagonal matrix
//        int sizeAxes = femElements[0].getLocalAxes().length;
//
//        double[][] gok = new double[femElements.length * sizeAxes][femElements.length * sizeAxes];
//        for (int i = 0; i < femElements.length; i++) {
//            int positionBaseLine = i * sizeAxes;
//            Matrix ks = femElements[i].getPotentialMatrixTr2();
//            for (int j = 0; j < sizeAxes; j++) {
//                for (int k = 0; k < sizeAxes; k++) {
//                    gok[positionBaseLine + j][positionBaseLine + k] = ks.getArray()[j][k];
//                }
//            }
//        }
//
//        return new Matrix(gok);
//    }
//
    static Matrix generateQuasiMatrixMass(FemElement[] femElements) {
        //TODO optimize to diagonal matrix
        int amount = 0;
        for (int i = 0; i < femElements.length; i++) {
            amount += femElements[i].getPoint().length * FemPoint.AMOUNT_POINT_AXES;
        }

//        int sizeAxes = femElements[0].getLocalAxes().length;

        double[][] gok = new double[amount][amount];
        for (int i = 0; i < femElements.length; i++) {
            int sizeAxes = femElements[i].getPoint().length * FemPoint.AMOUNT_POINT_AXES;
            int positionBaseLine = i * sizeAxes;
            Matrix ks = femElements[i].getMatrixMassTr();
            for (int j = 0; j < sizeAxes; j++) {
                for (int k = 0; k < sizeAxes; k++) {
                    gok[positionBaseLine + j][positionBaseLine + k] = ks.getArray()[j][k];
                }
            }
        }

        return new Matrix(gok);
    }

    static Matrix generateForceVector(FemPoint[] femPoints, Force[] forces, int amountAxesInPoint) {
        double[][] displacementVector = new double[femPoints.length * amountAxesInPoint][1];
        for (Force force : forces) {
            displacementVector
                    [convertPointGlobalAxeToNumber.get(force.getFemPoint().getNumberGlobalAxe()[force.getDirection().getPosition()])][0]
                    = force.getAmplitude();
        }
        return new Matrix(displacementVector);
    }

    static Matrix putZeroInSupportRowColumns(Matrix matrix, Support[] supports) {
        for (Support support : supports) {
            int size = matrix.getArray().length;
            int numberGlobalAxe = convertPointGlobalAxeToNumber.get(support.getFemPoint().getNumberGlobalAxe()[support.getDirection().getPosition()]);
            for (int i = 0; i < size; i++) {
                matrix.getArray()[i][numberGlobalAxe] = 0;
            }
            for (int i = 0; i < size; i++) {
                matrix.getArray()[numberGlobalAxe][i] = 0;
            }
            matrix.getArray()[numberGlobalAxe][numberGlobalAxe] = 1;

        }
        return matrix;
    }

    static Matrix deleteFewColumnsRows(Matrix matrix, Support[] supports) {
        double[][] result = new double[matrix.getRowDimension() - supports.length][matrix.getColumnDimension() - supports.length];
        List<Integer> delete = new ArrayList<>();
        for (Support support : supports) {
            delete.add(convertPointGlobalAxeToNumber.get(support.getFemPoint().getNumberGlobalAxe()[support.getDirection().getPosition()]));
        }
        int k = 0;
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            if (isFound(i, delete)) {
                continue;
            }
            int f = 0;
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                if (isFound(j, delete)) {
                    continue;
                }
                result[k][f] = matrix.getArray()[i][j];
                f++;
            }
            k++;
        }
        return new Matrix(result);
    }
//
//    static Matrix deleteFewColumnsRows(Matrix matrix, int[] list) {
//        double[][] result = new double[matrix.getRowDimension() - list.length][matrix.getColumnDimension() - list.length];
//        List<Integer> delete = new ArrayList<>();
//        for (int aList : list) {
//            delete.add(aList);
//        }
//        int k = 0;
//        for (int i = 0; i < matrix.getRowDimension(); i++) {
//            if (isFound(i, delete)) {
//                continue;
//            }
//            int f = 0;
//            for (int j = 0; j < matrix.getColumnDimension(); j++) {
//                if (isFound(j, delete)) {
//                    continue;
//                }
//                result[k][f] = matrix.getArray()[i][j];
//                f++;
//            }
//            k++;
//        }
//        return new Matrix(result);
//    }

    private static boolean isFound(int i, List<Integer> delete) {
        for (Integer aDelete : delete) {
            if (i == aDelete)
                return true;
        }
        return false;
    }
//
//    static Matrix deleteBad(Matrix h) {
//        List<Integer> delete = new ArrayList<>();
//        for (int i = 0; i < h.getColumnDimension(); i++) {
//            boolean bad = true;
//            for (int j = 0; j < h.getRowDimension(); j++) {
//                if (Math.abs(h.getArray()[i][j]) > 1e-6) {
//                    bad = false;
//                }
//            }
//            if (bad)
//                delete.add(i);
//        }
//        double[][] result = new double[h.getRowDimension() - delete.size()][h.getColumnDimension() - delete.size()];
//        int k = 0;
//        for (int i = 0; i < h.getRowDimension(); i++) {
//            if (isFound(i, delete)) {
//                continue;
//            }
//            int f = 0;
//            for (int j = 0; j < h.getColumnDimension(); j++) {
//                if (isFound(j, delete)) {
//                    continue;
//                }
//                result[k][f] = h.getArray()[i][j];
//                f++;
//            }
//            k++;
//        }
//        return new Matrix(result);
//    }

    public static Matrix[] calculateEigen(Matrix K, Matrix M) {

        // TODO: 26.10.2016 eigenvector --- divide each element to maximal
        // TODO: 26.10.2016 eigenvalue at 10 times less

        Matrix input = M.solve(K);

        if (DEBUG) System.out.println("input = M.solve(K)");
        if (DEBUG) input.print(12, 1);

        EigenvalueDecomposition ei = input.eig();
        class Eigen {
            double value;
            int index;

            public Eigen(double value, int index) {
                this.value = value;
                this.index = index;
            }
        }
        List<Eigen> eigens = new ArrayList<>();
        for (int i = 0; i < ei.getRealEigenvalues().length; i++) {
            eigens.add(new Eigen(ei.getRealEigenvalues()[i], i));
        }
        Collections.sort(eigens, new Comparator<Eigen>() {
            @Override
            public int compare(Eigen o1, Eigen o2) {
                if (o1.value > o2.value)
                    return 1;
                if (o1.value < o2.value)
                    return -1;
                return 0;
            }
        });
        Matrix d = ei.getV();
        Matrix values = new Matrix(eigens.size(), 1);
        Matrix vectors = new Matrix(d.getRowDimension(), d.getColumnDimension());
        for (int i = 0; i < eigens.size(); i++) {
            values.set(i, 0, eigens.get(i).value);
            for (int j = 0; j < d.getRowDimension(); j++) {
                vectors.set(j, eigens.get(i).index, d.getArray()[j][i]);
            }
        }

        if (DEBUG) System.out.println("Eigen values");
        if (DEBUG) values.print(12, 1);

        return new Matrix[]{values, vectors};
    }

//
//    protected static int[] getZeroColumnsRows(Matrix matrix) {
//        List<Integer> integers = new ArrayList<>();
//        for (int i = 0; i < matrix.getArray().length; i++) {
//            boolean onlyZeros = true;
//            for (int j = 0; j < matrix.getArray()[i].length; j++) {
//                if (matrix.getArray()[i][j] != 0)
//                    onlyZeros = false;
//            }
//            if (onlyZeros)
//                integers.add(i);
//        }
//        int[] array = new int[integers.size()];
//        for (int i = 0; i < integers.size(); i++) {
//            array[i] = integers.get(i);
//        }
//        return array;
//    }
}
