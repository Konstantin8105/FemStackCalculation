package home;

import home.FemElements.FemBeam2d;
import home.FemElements.FemElement;
import home.FemElements.FemTruss2d;
import home.Other.FemPoint;
import home.Other.Force;
import home.Other.Support;
import jama.Matrix;

import java.util.*;

public class Fem {

    private static Map<Integer, Integer> convertPointGlobalAxeToNumber;
    private static Map<Integer, Integer> convertLineGlobalAxeToNumber;

    public static void calculate(
            FemPoint[] femPoints,
            FemElement[] femElements,
            Force[] forces,
            Support[] supports) {

        convertPointGlobalAxeToNumber = convertPointAxeToSequenceAxe(femElements);
        convertLineGlobalAxeToNumber = convertLineAxeToSequenceAxe(femElements);


        Matrix A = generateMatrixCompliance(femPoints, femElements);

        Matrix Kok = generateMatrixQuasiStiffener(femElements);

        Matrix Ko = (A.transpose().times(Kok)).times(A);

        Matrix displacementVector = generateDisplacementMatrix(femPoints, forces, femElements[0].getAxes().length / 2);

        Matrix K = generateMatrixStiffener(Ko, supports);

        Matrix Z0 = K.solve(displacementVector);

        Matrix Z0k = A.times(Z0);

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

    private static Map<Integer, Integer> convertPointAxeToSequenceAxe(FemElement[] femElements) {
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

    private static Matrix generateMatrixCompliance(FemPoint[] femPoints, FemElement[] lines) {
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

            }
        }

        return new Matrix(a);
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
