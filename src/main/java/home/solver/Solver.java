package home.solver;

import home.finiteElement.FemBeam2d;
import home.finiteElement.FemElement;
import home.other.FemPoint;
import home.other.Force;
import home.other.Support;
import jama.Matrix;

import java.util.*;

public class Solver {

    static final boolean DEBUG = true;

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
        for (FemElement element : femElements) {
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
                } else throw new Exception("Fem element not supported");
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


        int amountAxes = 0;
        for (FemElement line : lines) {
            amountAxes += line.getAxes().length;
        }

        int sizeAxes = lines[0].getAxes().length / 2;
        double[][] a = new double[amountAxes][sizeAxes * femPoints.length];

        for (FemElement line : lines) {
            int row;
            int column;

            if (line instanceof FemBeam2d) {
                {
                    row = convertLineGlobalAxeToNumber.get(line.getAxes()[0]);
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[0].getNumberGlobalAxe()[0]);
                    a[row][column] = 1;
                }
                {
                    row = convertLineGlobalAxeToNumber.get(line.getAxes()[1]);
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[0].getNumberGlobalAxe()[1]);
                    a[row][column] = 1;
                }
                {
                    row = convertLineGlobalAxeToNumber.get(line.getAxes()[2]);
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[0].getNumberGlobalAxe()[2]);
                    a[row][column] = 1;
                }
                {
                    row = convertLineGlobalAxeToNumber.get(line.getAxes()[3]);
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[1].getNumberGlobalAxe()[0]);
                    a[row][column] = 1;
                }
                {
                    row = convertLineGlobalAxeToNumber.get(line.getAxes()[4]);
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[1].getNumberGlobalAxe()[1]);
                    a[row][column] = 1;
                }
                {
                    row = convertLineGlobalAxeToNumber.get(line.getAxes()[5]);
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[1].getNumberGlobalAxe()[2]);
                    a[row][column] = 1;
                }
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

        //TODO optimize to diagonal matrix
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

    static Matrix generateMatrixQuasiPotentialStiffener(FemElement[] femElements) {

        //TODO optimize to diagonal matrix
        int sizeAxes = femElements[0].getAxes().length;

        double[][] gok = new double[femElements.length * sizeAxes][femElements.length * sizeAxes];
        for (int i = 0; i < femElements.length; i++) {
            int positionBaseLine = i * sizeAxes;
            Matrix ks = femElements[i].getPotentialMatrixTr();
            for (int j = 0; j < sizeAxes; j++) {
                for (int k = 0; k < sizeAxes; k++) {
                    gok[positionBaseLine + j][positionBaseLine + k] = ks.getArray()[j][k];
                }
            }
        }

        return new Matrix(gok);
    }

    static Matrix generateMatrixQuasiStiffener2(FemElement[] femElements) {

        //TODO optimize to diagonal matrix
        int sizeAxes = femElements[0].getAxes().length;

        double[][] kok = new double[femElements.length * sizeAxes][femElements.length * sizeAxes];
        for (int i = 0; i < femElements.length; i++) {
            int positionBaseLine = i * sizeAxes;
            Matrix ks = femElements[i].getStiffenerMatrixTr2();
            for (int j = 0; j < sizeAxes; j++) {
                for (int k = 0; k < sizeAxes; k++) {
                    kok[positionBaseLine + j][positionBaseLine + k] = ks.getArray()[j][k];
                }
            }
        }

        return new Matrix(kok);
    }

    static Matrix generateMatrixQuasiPotentialStiffener2(FemElement[] femElements) {

        //TODO optimize to diagonal matrix
        int sizeAxes = femElements[0].getAxes().length;

        double[][] gok = new double[femElements.length * sizeAxes][femElements.length * sizeAxes];
        for (int i = 0; i < femElements.length; i++) {
            int positionBaseLine = i * sizeAxes;
            Matrix ks = femElements[i].getPotentialMatrixTr2();
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

    static Matrix generateMatrixStiffener(Matrix matrix, Support[] supports) {
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
        double[][] result = new double [matrix.getRowDimension()-supports.length][matrix.getColumnDimension()-supports.length];
        List<Integer> delete = new ArrayList<>();
        for(Support support:supports){
            delete.add(convertPointGlobalAxeToNumber.get(support.getFemPoint().getNumberGlobalAxe()[support.getDirection().getPosition()]));
        }
        int k = 0;
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            if(isFound(i,delete)){
                continue;
            }
            int f = 0;
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                if(isFound(j,delete)){
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
        for (int j = 0; j < delete.size(); j++) {
            if(i == delete.get(j))
                return true;
        }
        return false;
    }

    static Matrix deleteBad(Matrix h) {
        List<Integer> delete = new ArrayList<>();
        for (int i = 0; i < h.getColumnDimension(); i++) {
            boolean bad = true;
            for (int j = 0; j < h.getRowDimension(); j++) {
                if(Math.abs(h.getArray()[i][j]) > 1e-6){
                    bad = false;
                }
            }
            if(bad)
                delete.add(i);
        }
        double[][] result = new double [h.getRowDimension()-delete.size()][h.getColumnDimension()-delete.size()];
        int k = 0;
        for (int i = 0; i < h.getRowDimension(); i++) {
            if(isFound(i,delete)){
                continue;
            }
            int f = 0;
            for (int j = 0; j < h.getColumnDimension(); j++) {
                if(isFound(j,delete)){
                    continue;
                }
                result[k][f] = h.getArray()[i][j];
                f++;
            }
            k++;
        }
        return new Matrix(result);
    }

}
