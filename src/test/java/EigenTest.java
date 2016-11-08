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
        massM = massM.times(1500);

        double[][] stiff = new double[][]{
                {1., -1, 0., 0.},
                {-1, 3., -2, 0.},
                {0., -2, 5., -3},
                {0., 0., -3, 7.}
        };

        Matrix stiffM = new Matrix(stiff);
        stiffM = stiffM.times(800 * 1000);

        Matrix[] values = Solver.calculateEigen(stiffM, massM);

        Assert.assertEquals(values[0].getArray()[0][0], 117.8, 1e-1);
        Assert.assertEquals(values[0].getArray()[1][0], 586.5, 1e-1);
        Assert.assertEquals(values[0].getArray()[2][0], 1125, 1e-1);
    }

    @Test
    public void alladinTestBuckling() {
        //https://www.isr.umd.edu/~austin/aladdin.d/matrix-appl-buckling.html
        /* [a] : Define section/material properties Buckling Problem */

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

        Matrix[] values = Solver.calculateEigen(new Matrix(A), new Matrix(B));

        Assert.assertEquals(values[0].getArray()[0][0], 0.02284, 1e-1);
        Assert.assertEquals(values[0].getArray()[1][0], 0.09091, 1e-1);
    }


    @Test
    public void alladinTestBuckling2() {
        //https://www.isr.umd.edu/~austin/aladdin.d/matrix-appl-buckling.html
        /* [a] : Define section/material properties Buckling Problem */

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
        Matrix[] values = Solver.calculateEigen(new Matrix(A), new Matrix(B));

        Assert.assertEquals(values[0].getArray()[0][0], 0.02284, 1e-1);
        Assert.assertEquals(values[0].getArray()[1][0], 0.09091, 1e-1);
    }

    @Test
    public void alladinTestEigen() {
        double[][] k = new double[][]{
                {2, -2},
                {-2, 4}
        };
        Matrix K = new Matrix(k);

        double[][] m = new double[][]{
                {1, 0},
                {0, 1}
        };
        Matrix M = new Matrix(m);

        Matrix[] values = Solver.calculateEigen(K, M);

        Assert.assertEquals(values[0].getArray()[0][0], 7.63932e-01, 1e-1);
        Assert.assertEquals(values[0].getArray()[1][0], 5.23607e0, 1e-1);
    }
}
