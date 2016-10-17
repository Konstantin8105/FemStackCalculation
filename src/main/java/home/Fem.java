package home;

import home.FemElements.FemBeam2d;
import home.FemElements.FemBendBeam2d;
import home.FemElements.FemElement;
import home.FemElements.FemTruss2d;
import home.Other.FemPoint;
import home.Other.Force;
import home.Other.Support;
import jama.Matrix;

import java.util.*;

public class Fem {

    private static final boolean DEBUG = false;

    private static Map<Integer, Integer> convertPointGlobalAxeToNumber;
    private static Map<Integer, Integer> convertLineGlobalAxeToNumber;

    public static void calculate(
            FemPoint[] femPoints,
            FemElement[] femElements,
            Force[] forces,
            Support[] supports) throws Exception {

        convertPointGlobalAxeToNumber = convertPointAxeToSequenceAxe(femElements);
        convertLineGlobalAxeToNumber = convertLineAxeToSequenceAxe(femElements);

        if (DEBUG) System.out.println("Start calc A");
        //TODO optimize
        Matrix A = generateMatrixCompliance(femPoints, femElements);
        List<Integer>[] A2 = generateMatrixCompliance2(femPoints, femElements);

        if (DEBUG) System.out.println("Start calc Kok");
        Matrix Kok = generateMatrixQuasiStiffener(femElements);

        if (DEBUG) System.out.println("Start calc Ko");
        Matrix Ko = calculate(A2, Kok);

        if (DEBUG) System.out.println("Start calc displacementVector");
        Matrix displacementVector = generateDisplacementMatrix(femPoints, forces, femElements[0].getAxes().length / 2);

        if (DEBUG) System.out.println("Start calc K");
        Matrix K = generateMatrixStiffener(Ko, supports);

        if (DEBUG) System.out.println("Start calc Z0");
        //TODO optimize
        Matrix Z0 = K.solve(displacementVector);

        if (DEBUG) System.out.println("Start calc Z0k");
        //TODO optimize
        Matrix Z0k = A.times(Z0);

        if (DEBUG) System.out.println("Start calc localDisplacement");
        int sizeAxes = femElements[0].getAxes().length;
        for (int i = 0; i < femElements.length; i++) {
            double[] localDisplacement = new double[sizeAxes];
            for (int j = 0; j < localDisplacement.length; j++) {
                localDisplacement[j] = Z0k.getArray()[i * sizeAxes + j][0];
            }
            femElements[i].addInGlobalDisplacementCoordinate(localDisplacement);
        }
        FemElement.dropNumeration();
        FemPoint.dropNumeration();
    }

    private static Matrix calculate(List<Integer>[] a, Matrix kok) {
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

    private static Map<Integer, Integer> convertLineAxeToSequenceAxe(FemElement[] femElements) {
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

    private static Map<Integer, Integer> convertPointAxeToSequenceAxe(FemElement[] femElements) throws Exception {
        Set<Integer> listOfLineAxes = new HashSet<>();
        for (FemElement element : femElements) {
            for (int i = 0; i < element.getPoint().length; i++) {
                if (element instanceof FemTruss2d) {
                    listOfLineAxes.add(element.getPoint()[i].getNumberGlobalAxe()[0]);
                    listOfLineAxes.add(element.getPoint()[i].getNumberGlobalAxe()[1]);
                } else if (element instanceof FemBeam2d) {
                    for (int j = 0; j < 3; j++) {
                        listOfLineAxes.add(element.getPoint()[i].getNumberGlobalAxe()[j]);
                    }
                } else if(element instanceof FemBendBeam2d){
                    listOfLineAxes.add(element.getPoint()[i].getNumberGlobalAxe()[1]);
                    listOfLineAxes.add(element.getPoint()[i].getNumberGlobalAxe()[2]);
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

    private static Matrix generateMatrixCompliance(FemPoint[] femPoints, FemElement[] lines) throws Exception {
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

            if (line instanceof FemTruss2d) {
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
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[1].getNumberGlobalAxe()[0]);
                    a[row][column] = 1;
                }
                {
                    row = convertLineGlobalAxeToNumber.get(line.getAxes()[3]);
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[1].getNumberGlobalAxe()[1]);
                    a[row][column] = 1;
                }
            } else if (line instanceof FemBeam2d) {
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
            } else if (line instanceof FemBendBeam2d) {
                {
                    row = convertLineGlobalAxeToNumber.get(line.getAxes()[0]);
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[0].getNumberGlobalAxe()[1]);
                    a[row][column] = 1;
                }
                {
                    row = convertLineGlobalAxeToNumber.get(line.getAxes()[1]);
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[0].getNumberGlobalAxe()[2]);
                    a[row][column] = 1;
                }
                {
                    row = convertLineGlobalAxeToNumber.get(line.getAxes()[2]);
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[1].getNumberGlobalAxe()[1]);
                    a[row][column] = 1;
                }
                {
                    row = convertLineGlobalAxeToNumber.get(line.getAxes()[3]);
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

    private static List<Integer>[] generateMatrixCompliance2(FemPoint[] femPoints, FemElement[] lines) throws Exception {
        //Y0
        //...^
        //...|
        //...|
        //...^1
        //...|....0
        //...+---->---> X0

        int sizeAxes = lines[0].getAxes().length / 2;

        List<Integer>[] array = new ArrayList[sizeAxes * femPoints.length];
        for (int i = 0; i < array.length; i++) {
            array[i] = new ArrayList<>();
        }

        for (FemElement line : lines) {
            int row;
            int column;

            if (line instanceof FemTruss2d) {
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
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[1].getNumberGlobalAxe()[0]);
                    array[column].add(row);
                }
                {
                    row = convertLineGlobalAxeToNumber.get(line.getAxes()[3]);
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[1].getNumberGlobalAxe()[1]);
                    array[column].add(row);
                }
            } else if (line instanceof FemBeam2d) {
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
            } else if (line instanceof FemBendBeam2d) {
                {
                    row = convertLineGlobalAxeToNumber.get(line.getAxes()[0]);
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[0].getNumberGlobalAxe()[1]);
                    array[column].add(row);
                }
                {
                    row = convertLineGlobalAxeToNumber.get(line.getAxes()[1]);
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[0].getNumberGlobalAxe()[2]);
                    array[column].add(row);
                }
                {
                    row = convertLineGlobalAxeToNumber.get(line.getAxes()[2]);
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[1].getNumberGlobalAxe()[1]);
                    array[column].add(row);
                }
                {
                    row = convertLineGlobalAxeToNumber.get(line.getAxes()[3]);
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[1].getNumberGlobalAxe()[2]);
                    array[column].add(row);
                }
            } else throw new Exception("FEM Element is not support");
        }

        return array;
    }

    private static Matrix generateMatrixQuasiStiffener(FemElement[] lines) {

        int sizeAxes = lines[0].getAxes().length;

        double[][] kok = new double[lines.length * sizeAxes][lines.length * sizeAxes];
        for (int i = 0; i < lines.length; i++) {
            int positionBaseLine = i * sizeAxes;
            Matrix ks = lines[i].getStiffenerMatrixTr();
            for (int j = 0; j < sizeAxes; j++) {
                for (int k = 0; k < sizeAxes; k++) {
                    kok[positionBaseLine + j][positionBaseLine + k] = ks.getArray()[j][k];
                }
            }
        }

        return new Matrix(kok);
    }

    private static Matrix generateDisplacementMatrix(FemPoint[] femPoints, Force[] forces, int amountAxesInPoint) {
        double[][] displacementVector = new double[femPoints.length * amountAxesInPoint][1];
        for (Force force : forces) {
            displacementVector
                    [convertPointGlobalAxeToNumber.get(force.getFemPoint().getNumberGlobalAxe()[force.getDirection().getPosition()])][0]
                    = force.getAmplitude();
        }
        return new Matrix(displacementVector);
    }

    private static Matrix generateMatrixStiffener(Matrix ko, Support[] supports) {
        for (Support support : supports) {
            int size = ko.getArray().length;
            int numberGlobalAxe = convertPointGlobalAxeToNumber.get(support.getFemPoint().getNumberGlobalAxe()[support.getDirection().getPosition()]);
            for (int i = 0; i < size; i++) {
                ko.getArray()[i][numberGlobalAxe] = 0;
            }
            for (int i = 0; i < size; i++) {
                ko.getArray()[numberGlobalAxe][i] = 0;
            }
            ko.getArray()[numberGlobalAxe][numberGlobalAxe] = 1;

        }
        return ko;
    }


}
