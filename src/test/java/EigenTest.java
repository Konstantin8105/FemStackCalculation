import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import home.solver.Solver;
import org.junit.Assert;
import org.junit.Test;

public class EigenTest {
    @Test
    public void eigenMatrix2x2() {
        double[][] m = new double[][]{
                {-1., -6.},
                {2., 6.}
        };
        Matrix A = new Matrix(m);
        EigenvalueDecomposition ei = new EigenvalueDecomposition(A);
        ei.getD().print(10, 5);
        ei.getV().print(10, 5);
        for (int i = 0; i < ei.getRealEigenvalues().length; i++) {
            System.out.println("Real  " + ei.getRealEigenvalues()[i]);
        }
        for (int i = 0; i < ei.getImagEigenvalues().length; i++) {
            System.out.println("Image " + ei.getImagEigenvalues()[i]);
        }
        Assert.assertEquals(ei.getD().getArray()[0][0], 2., 1e-5);
        Assert.assertEquals(ei.getD().getArray()[1][1], 3., 1e-5);
    }

    @Test
    public void eigenMatrix2x2_2() {
        double[][] m = new double[][]{
                {1.46, -0.72},
                {-0.72, 1.04},
        };
        Matrix A = new Matrix(m);
        EigenvalueDecomposition ei = new EigenvalueDecomposition(A);
        ei.getD().print(10, 5);
        ei.getV().print(10, 5);
        for (int i = 0; i < ei.getRealEigenvalues().length; i++) {
            System.out.println("Real  " + ei.getRealEigenvalues()[i]);
        }
        for (int i = 0; i < ei.getImagEigenvalues().length; i++) {
            System.out.println("Image " + ei.getImagEigenvalues()[i]);
        }
        Assert.assertEquals(ei.getD().getArray()[0][0], 0.5, 1e-5);
        Assert.assertEquals(ei.getD().getArray()[1][1], 2.0, 1e-5);
    }

    @Test
    public void eigenMatrix3x3() {
        double[][] m = new double[][]{
                {3, 6, 3},
                {-1, -2, 1},
                {1, 2, -3}
        };
        Matrix A = new Matrix(m);
        EigenvalueDecomposition ei = new EigenvalueDecomposition(A);
        ei.getD().print(10, 5);
        ei.getV().print(10, 5);
        for (int i = 0; i < ei.getRealEigenvalues().length; i++) {
            System.out.println("Real  " + ei.getRealEigenvalues()[i]);
        }
        for (int i = 0; i < ei.getImagEigenvalues().length; i++) {
            System.out.println("Image " + ei.getImagEigenvalues()[i]);
        }
        Assert.assertEquals(ei.getD().getArray()[0][0], 2., 1e-5);
        Assert.assertEquals(ei.getD().getArray()[1][1], 0., 1e-5);
        Assert.assertEquals(ei.getD().getArray()[2][2], -4., 1e-5);
    }

    @Test
    public void eigenMatrix3x3_2() {
        double[][] m = new double[][]{
                {3.1568, 1.3824, -1.728},
                {1.3824, 3.9632, 1.296},
                {-1.728, 1.296, 3.38}
        };
        Matrix A = new Matrix(m);
        EigenvalueDecomposition ei = new EigenvalueDecomposition(A);
        ei.getD().print(10, 5);
        ei.getV().print(10, 5);
        for (int i = 0; i < ei.getRealEigenvalues().length; i++) {
            System.out.println("Real  " + ei.getRealEigenvalues()[i]);
        }
        for (int i = 0; i < ei.getImagEigenvalues().length; i++) {
            System.out.println("Image " + ei.getImagEigenvalues()[i]);
        }
        Assert.assertEquals(ei.getD().getArray()[0][0], 0.5, 1e-5);
        Assert.assertEquals(ei.getD().getArray()[1][1], 5, 1e-5);
        Assert.assertEquals(ei.getD().getArray()[2][2], 5, 1e-5);
    }

    @Test
    public void eigenMatrix3x3_3() {
        double[][] m = new double[][]{
                {-149, -50, -154},
                {537, 180, 546},
                {-27, -9, -25}
        };
        Matrix A = new Matrix(m);
        EigenvalueDecomposition ei = new EigenvalueDecomposition(A);
        ei.getD().print(10, 5);
        ei.getV().print(10, 5);
        for (int i = 0; i < ei.getRealEigenvalues().length; i++) {
            System.out.println("Real  " + ei.getRealEigenvalues()[i]);
        }
        for (int i = 0; i < ei.getImagEigenvalues().length; i++) {
            System.out.println("Image " + ei.getImagEigenvalues()[i]);
        }
        Assert.assertEquals(ei.getRealEigenvalues()[0], 0.9999999, 1e-3);
        Assert.assertEquals(ei.getRealEigenvalues()[2], 2.9998854, 1e-3);
    }

    @Test
    public void alladinTestModal() {
        //https://www.isr.umd.edu/~austin/aladdin.d/matrix-appl-building.html

        double[][] mass = new double[][]{
                {1, 0, 0, 0},
                {0, 2, 0, 0},
                {0, 0, 2, 0},
                {0, 0, 0, 3}
        };
        Matrix massM = new Matrix(mass);
        massM = massM.times(1500 * 10);

        System.out.println("Matrix mass");
        massM.print(8, 0);

        double[][] stiff = new double[][]{
                {1., -1, 0., 0.},
                {-1, 3., -2, 0.},
                {0., -2, 5., -3},
                {0., 0., -3, 7.}
        };

        Matrix stiffM = new Matrix(stiff);
        stiffM = stiffM.times(800 * 1000);

        System.out.println("Matrix stiff");
        stiffM.print(8, 0);

        // TODO: 26.10.2016 eigenvector --- divide each element to maximal
        // TODO: 26.10.2016 eigenvalue at 10 times less

        Matrix values = Solver.calculateEigen(stiffM, massM);

        Assert.assertEquals(values.getArray()[0][0], 117.8, 1e-1);
        Assert.assertEquals(values.getArray()[1][0], 586.5, 1e-1);
        Assert.assertEquals(values.getArray()[2][0], 1125, 1e-1);
    }

    @Test
    public void alladinTestBuckling() {
        //https://www.isr.umd.edu/~austin/aladdin.d/matrix-appl-buckling.html
        /* [a] : Define section/material properties Buckling Problem */

        double E = 200e9;
        double I = 1000e-8;
        double L = 5;

        /* [b] : Define a (5x5) matrices for finite difference approximation */

        double[][] A = new double[][]{
                {2, -1, 0, 0, 0},
                {-1, 2, -1, 0, 0},
                {0, -1, 2, -1, 0},
                {0, 0, -1, 2, -1},
                {0, 0, 0, -1, 2},
        };

        double[][] B = new double[][]{
                {10, 1, 0, 0, 0},
                {1, 10, 1, 0, 0},
                {0, 1, 10, 1, 0},
                {0, 0, 1, 10, 1},
                {0, 0, 0, 1, 10},
        };

        Matrix values = Solver.calculateEigen(new Matrix(A), new Matrix(B));

        Assert.assertEquals(values.getArray()[0][0], 0.02284, 1e-1);
        Assert.assertEquals(values.getArray()[1][0], 0.09091, 1e-1);
    }
}
