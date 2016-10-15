package home;

import home.FemElements.FemElement;
import home.FemElements.FemTruss2d;
import jama.Matrix;

public class ExampleTruss {
    public static void main(String[] args) {
        MKEPoint[] mkePoints = new MKEPoint[]{
                new MKEPoint(0, 0, 0),
                new MKEPoint(1, 0, 1.2),
                new MKEPoint(2, 0.4, 0),
                new MKEPoint(3, 0.4, 0.6),
                new MKEPoint(4, 0.8, 0),
        };

        for (int i = 0; i < mkePoints.length; i++) {
            System.out.println("[" + i + "] = " + mkePoints[i]);
        }

//        double[][] array = {{1.,2.,3},{4.,5.,6.},{7.,8.,10.}};
//        Matrix A = new Matrix(array);
//        Matrix b = Matrix.random(3,1);
//        Matrix x = A.solve(b);
//        Matrix Residual = A.times(x).minus(b);
//        double rnorm = Residual.normInf();
//        x.print(10,5);

        FemElement[] lines = new FemTruss2d[]{
                new FemTruss2d(2e7, 40e-4, new MKEPoint[]{mkePoints[0], mkePoints[1]}),
                new FemTruss2d(2e7, 64e-4, new MKEPoint[]{mkePoints[0], mkePoints[2]}),
                new FemTruss2d(2e7, 60e-4, new MKEPoint[]{mkePoints[0], mkePoints[3]}),
                new FemTruss2d(2e7, 60e-4, new MKEPoint[]{mkePoints[1], mkePoints[3]}),
                new FemTruss2d(2e7, 40e-4, new MKEPoint[]{mkePoints[2], mkePoints[3]}),
                new FemTruss2d(2e7, 64e-4, new MKEPoint[]{mkePoints[2], mkePoints[4]}),
                new FemTruss2d(2e7, 60e-4, new MKEPoint[]{mkePoints[3], mkePoints[4]})
        };

        Matrix A = generateMatrixCompliance(mkePoints, lines);

        for (int i = 0; i < lines.length; i++) {
            lines[i].getStiffenerMatrix().print(10, 2);
            lines[i].getTr().print(10, 2);
            Matrix tr = lines[i].getTr();
            Matrix kr = lines[i].getStiffenerMatrix();
            Matrix trt = tr.transpose();

            Matrix kr0 = ((trt.times(kr)).times(tr));

            kr0.print(10, 2);

            System.out.println("--------------");
        }

        A.print(3, 1);
    }

    private static Matrix generateMatrixCompliance(MKEPoint[] mkePoints, FemElement[] lines) {
        //Y0
        //...^
        //...|
        //...|
        //...^1
        //...|....0
        //...+---->---> X0

        for (int i = 0; i < lines.length; i++) {
            if (!(lines[i] instanceof FemTruss2d)) {
                System.out.println("WRONG - I CAN NOT CALCULATE ANOTHER TYPE");
            }
        }

        double[][] a = new double[lines.length * 4][2 * mkePoints.length];

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
