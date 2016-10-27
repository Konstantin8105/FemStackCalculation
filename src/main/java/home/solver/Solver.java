package home.solver;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import home.finiteElement.FemBeam2d;
import home.finiteElement.FemBending2d;
import home.finiteElement.FemElement;
import home.finiteElement.ModalFemElement;
import home.other.FemPoint;
import home.other.Force;
import home.other.Support;

import java.util.*;

public class Solver {

    static boolean DEBUG = false;

    static Map<Integer, Integer> convertPointGlobalAxeToNumber;
    static Map<Integer, Integer> convertLineGlobalAxeToNumber;

    static Matrix calculate(List<Integer>[] a, Matrix kok) {
        //TODO create beautiful method
        // A.transpose().times(Kok)
        Matrix aK = new Matrix(a.length, kok.getColumnDimension());
        for (int i = 0; i < aK.getRowDimension(); i++) {
            for (int j = 0; j < aK.getColumnDimension(); j++) {
                aK.getArray()[i][j] = 0.0;
            }
        }
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < kok.getColumnDimension(); j++) {
                //a[i].get(j) is row. After transpose - this is column.
                double sum = 0;
                for (int k = 0; k < a[i].size(); k++) {
                    sum += kok.getArray()[j][a[i].get(k)];
                }
                aK.getArray()[i][j] = sum;
            }
        }
        Matrix aKa = new Matrix(a.length, a.length);
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                //a[i].get(j) is row.
                double sum = 0;
                for (int k = 0; k < a[i].size(); k++) {
                    sum += aK.getArray()[j][a[i].get(k)];
                }
                aKa.getArray()[i][j] = sum;
            }
        }
        return aKa;
    }

    static Map<Integer, Integer> convertLineAxeToSequenceAxe(FemElement[] femElements) {
        Set<Integer> listOfLineAxes = new HashSet<>();
        for (int h = femElements.length - 1; h >= 0; h--) {
            FemElement element = femElements[h];
            for (int i = 0; i < element.getPoint().length; i++) {
                for (int j = 0; j < element.getAxes().length; j++) {
                    listOfLineAxes.add(element.getAxes()[j]);
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
        Set<Integer> listOfLineAxes = new HashSet<>();
        for (FemElement element : femElements) {
            for (int i = 0; i < element.getPoint().length; i++) {
                if (element instanceof FemBeam2d) {
                    for (int j = 0; j < 3; j++) {
                        listOfLineAxes.add(element.getPoint()[i].getNumberGlobalAxe()[j]);
                    }
                } else if (element instanceof FemBending2d) {
                    listOfLineAxes.add(element.getPoint()[i].getNumberGlobalAxe()[1]);
                    listOfLineAxes.add(element.getPoint()[i].getNumberGlobalAxe()[2]);
                } else throw new Exception("ModalSolver element not supported");
            }
        }
        Map<Integer, Integer> map = new TreeMap<>();
        int position = 0;
        for (Integer number : listOfLineAxes) {
            map.put(number, position++);
        }
        return map;
    }

    static Matrix generateMatrixCompliance(FemPoint[] femPoints, FemElement[] lines) throws Exception {
        //Y0
        //...^
        //...|
        //...|
        //...^1
        //...|....0
        //...+---->---> X0

        int elementAxes = -1;
        int pointAxes = -1;

        if (lines[0] instanceof FemBeam2d) {
            elementAxes = 6;
            pointAxes = 3;
        } else if (lines[0] instanceof FemBending2d) {
            elementAxes = 4;
            pointAxes = 2;
        } else throw new Exception("FEM Element is not support");

        double[][] a = new double[lines.length * elementAxes][femPoints.length * pointAxes];

        if (DEBUG) {
            System.out.println("Amount fem elements = " + lines.length);
            System.out.println("Amount columns(fem elements*6) = " + lines.length * 6);
            System.out.println("Amount points elements = " + femPoints.length);
            System.out.println("Amount rows(points elements*3) = " + femPoints.length * 3);
            System.out.println("Matrix A size = [" + a.length + "," + a[0].length + "]");
            boolean bug = false;
            for (double[] anA : a) {
                for (int j = 0; j < a[0].length; j++) {
                    if (anA[j] != 0 && anA[j] != 1) {
                        bug = true;
                    }
                }
            }
            System.out.println("All elements A is zero or one? " + !bug);
        }

        for (FemElement line : lines) {
            int row;
            int column;
            if (line instanceof FemBeam2d) {
                for (int i = 0; i < 6; i++) {
                    row = convertLineGlobalAxeToNumber.get(line.getAxes()[i]);
                    int axe = i % 3;
                    int point = i / 3;
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[point].getNumberGlobalAxe()[axe]);
                    a[row][column] = 1;
                }
            } else if (line instanceof FemBending2d) {
                row = convertLineGlobalAxeToNumber.get(line.getAxes()[0]);
                column = convertPointGlobalAxeToNumber.get(line.getPoint()[0].getNumberGlobalAxe()[1]);
                a[row][column] = 1;

                row = convertLineGlobalAxeToNumber.get(line.getAxes()[1]);
                column = convertPointGlobalAxeToNumber.get(line.getPoint()[0].getNumberGlobalAxe()[2]);
                a[row][column] = 1;

                row = convertLineGlobalAxeToNumber.get(line.getAxes()[2]);
                column = convertPointGlobalAxeToNumber.get(line.getPoint()[1].getNumberGlobalAxe()[1]);
                a[row][column] = 1;

                row = convertLineGlobalAxeToNumber.get(line.getAxes()[3]);
                column = convertPointGlobalAxeToNumber.get(line.getPoint()[1].getNumberGlobalAxe()[2]);
                a[row][column] = 1;
            } else throw new Exception("FEM Element is not support");
        }

        if (DEBUG) {
            long amount = 0;
            for (double[] anA : a) {
                for (int j = 0; j < a[0].length; j++) {
                    if (anA[j] > 0)
                        amount++;
                }
            }
            System.out.println(
                    "Amount not zero elements :" + amount + " of "
                            + "(" + a.length + "," + a[0].length + ") = "
                            + (a.length * a[0].length) + " elements");
        }

        return new Matrix(a);
    }

    //TODO use next method for optimization
    static List<Integer>[] generateMatrixCompliance2(FemPoint[] femPoints, FemElement[] lines) throws Exception {
        //Y0
        //...^
        //...|
        //...|
        //...^1
        //...|....0
        //...+---->---> X0

        int sizeAxes = lines[0].getAxes().length / 2;

        List<Integer>[] array = new List[sizeAxes * femPoints.length];
        for (int i = 0; i < array.length; i++) {
            array[i] = new ArrayList<>();
        }

        for (FemElement line : lines) {
            int row;
            int column;

            if (line instanceof FemBeam2d) {
                {
                    row = convertLineGlobalAxeToNumber.get(line.getAxes()[0]);
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[0].getNumberGlobalAxe()[0]);
                    array[column].add(row);
                }
                {
                    row = convertLineGlobalAxeToNumber.get(line.getAxes()[1]);
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[0].getNumberGlobalAxe()[1]);
                    array[column].add(row);
                }
                {
                    row = convertLineGlobalAxeToNumber.get(line.getAxes()[2]);
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[0].getNumberGlobalAxe()[2]);
                    array[column].add(row);
                }
                {
                    row = convertLineGlobalAxeToNumber.get(line.getAxes()[3]);
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[1].getNumberGlobalAxe()[0]);
                    array[column].add(row);
                }
                {
                    row = convertLineGlobalAxeToNumber.get(line.getAxes()[4]);
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[1].getNumberGlobalAxe()[1]);
                    array[column].add(row);
                }
                {
                    row = convertLineGlobalAxeToNumber.get(line.getAxes()[5]);
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[1].getNumberGlobalAxe()[2]);
                    array[column].add(row);
                }
            } else throw new Exception("FEM Element is not support");
        }

        return array;
    }

    static Matrix generateMatrixQuasiStiffener(FemElement[] femElements) {

        //TODO optimize to diagonal matrix and symmetrical
        int sizeAxes = femElements[0].getAxes().length;

        double[][] kok = new double[femElements.length * sizeAxes][femElements.length * sizeAxes];
        for (int i = 0; i < femElements.length; i++) {
            int positionBaseLine = i * sizeAxes;
            Matrix ks = femElements[i].getStiffenerMatrixTr();
            for (int j = 0; j < sizeAxes; j++) {
                for (int k = 0; k < sizeAxes; k++) {
                    kok[positionBaseLine + j][positionBaseLine + k] = ks.getArray()[j][k];
                }
            }
        }

        return new Matrix(kok);
    }

    //
//    static Matrix generateMatrixQuasiPotentialStiffener(FemElement[] femElements) {
//
//        //TODO optimize to diagonal matrix
//        int sizeAxes = femElements[0].getAxes().length;
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
//        int sizeAxes = femElements[0].getAxes().length;
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
//        int sizeAxes = femElements[0].getAxes().length;
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
    static Matrix generateQuasiMatrixMass(ModalFemElement[] femElements) {
        //TODO optimize to diagonal matrix
        int sizeAxes = femElements[0].getAxes().length;

        double[][] gok = new double[femElements.length * sizeAxes][femElements.length * sizeAxes];
        for (int i = 0; i < femElements.length; i++) {
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


    static Matrix addPointMass(Matrix mo, Force[] forces) {
        for (Force force : forces) {
            FemPoint point = force.getFemPoint();
            mo.getArray()[point.getNumberGlobalAxe()[0]][point.getNumberGlobalAxe()[0]] += force.getAmplitude();
            mo.getArray()[point.getNumberGlobalAxe()[1]][point.getNumberGlobalAxe()[1]] += force.getAmplitude();
        }
        return mo;
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

    static Matrix deleteFewColumnsRows(Matrix matrix, int[] list) {
        double[][] result = new double[matrix.getRowDimension() - list.length][matrix.getColumnDimension() - list.length];
        List<Integer> delete = new ArrayList<>();
        for (int i = 0; i < list.length; i++) {
            delete.add(list[i]);
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

    private static boolean isFound(int i, List<Integer> delete) {
        for (Integer aDelete : delete) {
            if (i == aDelete)
                return true;
        }
        return false;
    }

    static Matrix deleteBad(Matrix h) {
        List<Integer> delete = new ArrayList<>();
        for (int i = 0; i < h.getColumnDimension(); i++) {
            boolean bad = true;
            for (int j = 0; j < h.getRowDimension(); j++) {
                if (Math.abs(h.getArray()[i][j]) > 1e-6) {
                    bad = false;
                }
            }
            if (bad)
                delete.add(i);
        }
        double[][] result = new double[h.getRowDimension() - delete.size()][h.getColumnDimension() - delete.size()];
        int k = 0;
        for (int i = 0; i < h.getRowDimension(); i++) {
            if (isFound(i, delete)) {
                continue;
            }
            int f = 0;
            for (int j = 0; j < h.getColumnDimension(); j++) {
                if (isFound(j, delete)) {
                    continue;
                }
                result[k][f] = h.getArray()[i][j];
                f++;
            }
            k++;
        }
        return new Matrix(result);
    }

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
        return new Matrix[]{values, vectors};
    }


    protected static int[] getZeroColumnsRows(Matrix matrix) {
        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < matrix.getArray().length; i++) {
            boolean onlyZeros = true;
            for (int j = 0; j < matrix.getArray()[i].length; j++) {
                if (matrix.getArray()[i][j] != 0)
                    onlyZeros = false;
            }
            if (onlyZeros)
                integers.add(i);
        }
        int[] array = new int[integers.size()];
        for (int i = 0; i < integers.size(); i++) {
            array[i] = integers.get(i);
        }
        return array;
    }
}
