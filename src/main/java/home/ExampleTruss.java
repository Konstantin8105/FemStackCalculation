package home;

import home.FemElements.*;
import jama.Matrix;

class ExampleTruss {

    public static void main(String[] args) {
        FemPoint[] femPoints = new FemPoint[]{
                new FemPoint(0, 0, 0),
                new FemPoint(1, 0, 1.2),
                new FemPoint(2, 0.4, 0),
                new FemPoint(3, 0.4, 0.6),
                new FemPoint(4, 0.8, 0),
        };

        FemElement[] lines = new FemTruss2d[]{
                new FemTruss2d(2e11, 40e-4, new FemPoint[]{femPoints[0], femPoints[1]}),
                new FemTruss2d(2e11, 64e-4, new FemPoint[]{femPoints[0], femPoints[2]}),
                new FemTruss2d(2e11, 60e-4, new FemPoint[]{femPoints[0], femPoints[3]}),
                new FemTruss2d(2e11, 60e-4, new FemPoint[]{femPoints[1], femPoints[3]}),
                new FemTruss2d(2e11, 40e-4, new FemPoint[]{femPoints[2], femPoints[3]}),
                new FemTruss2d(2e11, 64e-4, new FemPoint[]{femPoints[2], femPoints[4]}),
                new FemTruss2d(2e11, 60e-4, new FemPoint[]{femPoints[3], femPoints[4]})
        };

        Force[] forces = new Force[]{
                new Force(femPoints[1], -70000, 0),
                new Force(femPoints[3], 42000, 0),
        };

        Support[] supports = new Support[]{
                new Support(femPoints[0], true, true),
                new Support(femPoints[2], false, true),
                new Support(femPoints[4], false, true),
        };

        //=========================//
        calculate(femPoints,lines,forces,supports);
    }

    private static void calculate(FemPoint[] femPoints, FemElement[] lines, Force[] forces, Support[] supports) {
        for (FemElement line1 : lines) {
            line1.getStiffenerMatrix().print(10, 2);
            line1.getTr().print(10, 2);
            line1.getStiffenerMatrixTr().print(10, 2);
            System.out.println("--------------");
        }

        Matrix A = generateMatrixCompliance(femPoints, lines);
        A.print(3, 1);

        Matrix Kok = generateMatrixQuasiStiffener(lines);
        Kok.print(10, 1);

        Matrix Ko = (A.transpose().times(Kok)).times(A);
        Ko.print(10, 1);

        Matrix displacementVector = generateDisplacementMatrix(femPoints, forces);
        displacementVector.print(10, 1);

        Matrix K = generateMatrixStiffener(Ko, supports);
        K.print(10, 1);

        Matrix Z0 = K.solve(displacementVector);
        Z0.print(10, 5);

        Matrix Z0k = A.times(Z0);
        Z0k.print(10, 5);

        for (int i = 0; i < lines.length; i++) {
            double[] localDisplacement = new double[4];
            for (int j = 0; j < localDisplacement.length; j++) {
                localDisplacement[j] = Z0k.getArray()[i * 4 + j][0];
            }
            lines[i].addInGlobalDisplacementCoordinate(localDisplacement);
        }

        for(FemElement line:lines){
            System.out.println(line);
            line.getDisplacementInGlobalSystem().print(5,6);
            line.getDisplacementInLocalSystem().print(5,6);
            line.getInternalForce().print(5,6);
        }
    }

    private static Matrix generateMatrixStiffener(Matrix ko, Support[] supports) {
        for (Support support : supports) {
            int size = ko.getArray().length;
            if (support.isSupportByX()) {
                for (int i = 0; i < size; i++) {
                    ko.getArray()[i][support.getFemPoint().getNumberGlobalAxeX()] = 0;
                }
                for (int i = 0; i < size; i++) {
                    ko.getArray()[support.getFemPoint().getNumberGlobalAxeX()][i] = 0;
                }
                ko.getArray()[support.getFemPoint().getNumberGlobalAxeX()][support.getFemPoint().getNumberGlobalAxeX()] = 1;
            }
            if (support.isSupportByY()) {
                for (int i = 0; i < size; i++) {
                    ko.getArray()[i][support.getFemPoint().getNumberGlobalAxeY()] = 0;
                }
                for (int i = 0; i < size; i++) {
                    ko.getArray()[support.getFemPoint().getNumberGlobalAxeY()][i] = 0;
                }
                ko.getArray()[support.getFemPoint().getNumberGlobalAxeY()][support.getFemPoint().getNumberGlobalAxeY()] = 1;
            }
        }
        return ko;
    }

    private static Matrix generateDisplacementMatrix(FemPoint[] femPoints, Force[] forces) {
        double[][] displacementVector = new double[femPoints.length * 2][1];
        for (Force force : forces) {
            displacementVector[force.getFemPoint().getNumberGlobalAxeX()][0] = force.getFx();
            displacementVector[force.getFemPoint().getNumberGlobalAxeY()][0] = force.getFy();
        }
        return new Matrix(displacementVector);
    }

    private static Matrix generateMatrixQuasiStiffener(FemElement[] lines) {

        for (FemElement line : lines) {
            if (!(line instanceof FemTruss2d)) {
                System.out.println("WRONG - I CAN NOT CALCULATE ANOTHER TYPE");
            }
        }

        double[][] kok = new double[lines.length * 4][lines.length * 4];
        for (int i = 0; i < lines.length; i++) {
            int positionBaseLine = i * 4;
            Matrix ks = lines[i].getStiffenerMatrixTr();
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    kok[positionBaseLine + j][positionBaseLine + k] = ks.getArray()[j][k];
                }
            }
        }

        return new Matrix(kok);
    }

    private static Matrix generateMatrixCompliance(FemPoint[] femPoints, FemElement[] lines) {
        //Y0
        //...^
        //...|
        //...|
        //...^1
        //...|....0
        //...+---->---> X0

        for (FemElement line1 : lines) {
            if (!(line1 instanceof FemTruss2d)) {
                System.out.println("WRONG - I CAN NOT CALCULATE ANOTHER TYPE");
            }
        }

        double[][] a = new double[lines.length * 4][2 * femPoints.length];

        for (FemElement line : lines) {
            int row;
            int column;
            {
                row = line.getAxes()[0];
                column = line.getPoint()[0].getNumberGlobalAxeX();
                a[row][column] = 1;
            }
            {
                row = line.getAxes()[1];
                column = line.getPoint()[0].getNumberGlobalAxeY();
                a[row][column] = 1;
            }
            {
                row = line.getAxes()[2];
                column = line.getPoint()[1].getNumberGlobalAxeX();
                a[row][column] = 1;
            }
            {
                row = line.getAxes()[3];
                column = line.getPoint()[1].getNumberGlobalAxeY();
                a[row][column] = 1;
            }
        }

        return new Matrix(a);
    }
}
