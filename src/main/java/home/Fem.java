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

    static Map<Integer, Integer> convertPointGlobalAxeToNumber;

    public static void calculate(FemPoint[] femPoints, FemElement[] femElements, Force[] forces, Support[] supports) {
//        for (FemElement line1 : femElements) {
//            line1.getStiffenerMatrix().print(10, 2);
//            line1.getTr().print(10, 2);
//            line1.getStiffenerMatrixTr().print(10, 2);
//            System.out.println("--------------");
//        }

        convertPointGlobalAxeToNumber = convertPointAxeToSequenceAxe(femElements);


        Matrix A = generateMatrixCompliance(femPoints, femElements);
//        A.print(3, 1);

        Matrix Kok = generateMatrixQuasiStiffener(femElements);
//        Kok.print(10, 1);

        Matrix Ko = (A.transpose().times(Kok)).times(A);
//        Ko.print(10, 1);

        Matrix displacementVector = generateDisplacementMatrix(femPoints, forces, femElements[0].getAxes().length / 2);
//        displacementVector.print(10, 1);

        Matrix K = generateMatrixStiffener(Ko, supports);
//        K.print(10, 1);

        Matrix Z0 = K.solve(displacementVector);
//        Z0.print(10, 5);

        Matrix Z0k = A.times(Z0);
//        Z0k.print(10, 5);

        int sizeAxes = femElements[0].getAxes().length;
        for (int i = 0; i < femElements.length; i++) {
            double[] localDisplacement = new double[sizeAxes];
            for (int j = 0; j < localDisplacement.length; j++) {
                localDisplacement[j] = Z0k.getArray()[i * sizeAxes + j][0];
            }
            femElements[i].addInGlobalDisplacementCoordinate(localDisplacement);
        }

//        for (FemElement line : femElements) {
//            System.out.println(line);
//            line.getDisplacementInGlobalSystem().print(5, 6);
//            line.getDisplacementInLocalSystem().print(5, 6);
//            line.getInternalForce().print(5, 6);
//        }
    }

    static Map<Integer, Integer> convertPointAxeToSequenceAxe(FemElement[] lines) {
        Set<Integer> listOfLineAxes = new HashSet<>();
        for (FemElement line : lines) {
            for (int i = 0; i < line.getPoint().length; i++) {
                if (line instanceof FemTruss2d) {
                    listOfLineAxes.add(line.getPoint()[i].getNumberGlobalAxe()[0]);
                    listOfLineAxes.add(line.getPoint()[i].getNumberGlobalAxe()[1]);
                } else if (line instanceof FemBeam2d) {
                    for (int j = 0; j < 3; j++) {
                        listOfLineAxes.add(line.getPoint()[i].getNumberGlobalAxe()[j]);
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
                    row = line.getAxes()[0];
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[0].getNumberGlobalAxe()[0]);
                    a[row][column] = 1;
                }
                {
                    row = line.getAxes()[1];
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[0].getNumberGlobalAxe()[1]);
                    a[row][column] = 1;
                }
                {
                    row = line.getAxes()[2];
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[1].getNumberGlobalAxe()[0]);
                    a[row][column] = 1;
                }
                {
                    row = line.getAxes()[3];
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[1].getNumberGlobalAxe()[1]);
                    a[row][column] = 1;
                }
            } else if (line instanceof FemBeam2d) {
                {
                    row = line.getAxes()[0];
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[0].getNumberGlobalAxe()[0]);
                    a[row][column] = 1;
                }
                {
                    row = line.getAxes()[1];
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[0].getNumberGlobalAxe()[1]);
                    a[row][column] = 1;
                }
                {
                    row = line.getAxes()[2];
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[0].getNumberGlobalAxe()[2]);
                    a[row][column] = 1;
                }
                {
                    row = line.getAxes()[3];
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[1].getNumberGlobalAxe()[0]);
                    a[row][column] = 1;
                }
                {
                    row = line.getAxes()[4];
                    column = convertPointGlobalAxeToNumber.get(line.getPoint()[1].getNumberGlobalAxe()[1]);
                    a[row][column] = 1;
                }
                {
                    row = line.getAxes()[5];
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
