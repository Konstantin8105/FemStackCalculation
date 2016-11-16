package home.linearAlgebra;

import Jama.Matrix;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SolversLATest {

    @Test
    public void matrix1() {
        double[][] a = new double[][]{
                {1, -2, 3},
                {-2, 2, 7},
                {3, 7, 5}
        };
        double[][] v = new double[][]{{2}, {7}, {15}};
        solve(a, v, 1, 1, 1);
    }

    @Test
    public void matrix1a() {
        double[][] a = new double[][]{
                {6, -2, 3},
                {-2, 8, 2},
                {3, 2, 10}
        };
        double[][] v = new double[][]{{7}, {8}, {15}};
        solve(a, v, 1, 1, 1);
    }

    @Test
    public void matrix2() {
        double[][] a = new double[][]{
                {0, 1, -2},
                {1, 4, 3},
                {-2, 3, 4}
        };
        double[][] v = new double[][]{{-1}, {8}, {5}};
        solve(a, v, 1, 1, 1);
    }

    @Test
    public void matrix3() {
        double[][] a = new double[][]{
                {1, 1e-100},
                {1e-100, 1}
        };
        double[][] v = new double[][]{{1 + 1e-100}, {1 + 1e-100}};
        solve(a, v, 1, 1);
    }

    @Test
    public void matrix4() {
        double[][] a = new double[][]{
                {1e100, 1e-100},
                {1e-100, 1e100}
        };
        double[][] v = new double[][]{{1e100 + 1e-100}, {1e100 + 1e-100}};
        solve(a, v, 1, 1);
    }

    @Test
    public void matrix5() {
        double[][] a = new double[][]{
                {1e-100, 1e100},
                {1e100, 1e-100}
        };
        double[][] v = new double[][]{{1e100 + 1e-100}, {1e100 + 1e-100}};
        solve(a, v, 1, 1);
    }

    @Test
    public void matrix6() {
        double[][] a = new double[][]{
                {1e-100, 1e100},
                {1e100, 1e-100}
        };
        double[][] v = new double[][]{{1e100 + 1e-100}, {1e100 + 1e-100}};
        solve(a, v, 1, 1);
    }

    @Test
    public void matrix6a() {
        double[][] a = new double[][]{
                {-1e-100, 1},
                {1e-100, 1}
        };
        double[][] v = new double[][]{{0}, {2}};
        solve(a, v, 1e100, 1);
    }

    @Test
    public void matrix6b() {
        double[][] a = new double[][]{
                {1, 1e-5},
                {1, -1e-5}
        };
        double[][] v = new double[][]{{1e5 + 1}, {1e5 - 1}};
        solve(a, v, 1e5, 1e5);
    }

    @Test
    public void matrix6c() {
        double[][] a = new double[][]{
                {1e-20, 1},
                {1, 1e-20}
        };
        double[][] v = new double[][]{{1e-20 + 1}, {1e-20 + 1}};
        solve(a, v, 1, 1);
    }

    @Test
    public void matrix6d() {
        double[][] a = new double[][]{
                {1, 1e-20},
                {1e-20, 1}
        };
        double[][] v = new double[][]{{1e-20 + 1}, {1e-20 + 1}};
        solve(a, v, 1, 1);
    }

    @Test
    public void matrix7() {
        // Matrix A //
        double[][] a = new double[][]{
                {1.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 1.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 0.00000000e+00, 1.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 2.40000000e+11, 0.00000000e+00, 0.00000000e+00, -1.20000000e+11, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 1.03680000e+07, 0.00000000e+00, 0.00000000e+00, -5.18400000e+06, 4.32000000e+06, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 9.60000000e+06, 0.00000000e+00, -4.32000000e+06, 2.40000000e+06, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, -1.20000000e+11, 0.00000000e+00, 0.00000000e+00, 2.40000000e+11, 0.00000000e+00, 0.00000000e+00, -1.20000000e+11, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, -5.18400000e+06, -4.32000000e+06, 0.00000000e+00, 1.03680000e+07, 1.86264515e-09, 0.00000000e+00, -5.18400000e+06, 4.32000000e+06,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 4.32000000e+06, 2.40000000e+06, 0.00000000e+00, 1.86264515e-09, 9.60000000e+06, 0.00000000e+00, -4.32000000e+06, 2.40000000e+06,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, -1.20000000e+11, 0.00000000e+00, 0.00000000e+00, 1.20000000e+11, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, -5.18400000e+06, -4.32000000e+06, 0.00000000e+00, 5.18400000e+06, -4.32000000e+06,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 4.32000000e+06, 2.40000000e+06, 0.00000000e+00, -4.32000000e+06, 4.80000000e+06,},
        };
        // Vector V //
        double[][] v = new double[][]{
                {0.00000000e+00}, {0.00000000e+00}, {0.00000000e+00}, {0.00000000e+00}, {0.00000000e+00}, {0.00000000e+00}, {0.00000000e+00}, {0.00000000e+00}, {0.00000000e+00}, {0.00000000e+00}, {1.00000000e+04}, {0.00000000e+00},};
        solve(a, v);
    }

    @Test
    public void matrix8() {
        // Matrix A //
        double[][] a = new double[][]{
                {1.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 1.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 0.00000000e+00, 1.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 7.20000000e+11, 0.00000000e+00, 0.00000000e+00, -3.60000000e+11, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 2.79936000e+08, 0.00000000e+00, 0.00000000e+00, -1.39968000e+08, 3.88800000e+07, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 2.88000000e+07, 0.00000000e+00, -3.88800000e+07, 7.20000000e+06, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, -3.60000000e+11, 0.00000000e+00, 0.00000000e+00, 7.20000000e+11, 0.00000000e+00, 0.00000000e+00, -3.60000000e+11, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, -1.39968000e+08, -3.88800000e+07, 0.00000000e+00, 2.79936000e+08, 0.00000000e+00, 0.00000000e+00, -1.39968000e+08, 3.88800000e+07, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 3.88800000e+07, 7.20000000e+06, 0.00000000e+00, 0.00000000e+00, 2.88000000e+07, 0.00000000e+00, -3.88800000e+07, 7.20000000e+06, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, -3.60000000e+11, 0.00000000e+00, 0.00000000e+00, 7.20000000e+11, 0.00000000e+00, 0.00000000e+00, -3.60000000e+11, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, -1.39968000e+08, -3.88800000e+07, 0.00000000e+00, 2.79936000e+08, 0.00000000e+00, 0.00000000e+00, -1.39968000e+08, 3.88800000e+07, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 3.88800000e+07, 7.20000000e+06, 0.00000000e+00, 0.00000000e+00, 2.88000000e+07, 0.00000000e+00, -3.88800000e+07, 7.20000000e+06, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, -3.60000000e+11, 0.00000000e+00, 0.00000000e+00, 7.20000000e+11, 0.00000000e+00, 0.00000000e+00, -3.60000000e+11, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, -1.39968000e+08, -3.88800000e+07, 0.00000000e+00, 2.79936000e+08, 3.72529030e-08, 0.00000000e+00, -1.39968000e+08, 3.88800000e+07, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 3.88800000e+07, 7.20000000e+06, 0.00000000e+00, 3.72529030e-08, 2.88000000e+07, 0.00000000e+00, -3.88800000e+07, 7.20000000e+06, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, -3.60000000e+11, 0.00000000e+00, 0.00000000e+00, 7.20000000e+11, 0.00000000e+00, 0.00000000e+00, -3.60000000e+11, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, -1.39968000e+08, -3.88800000e+07, 0.00000000e+00, 2.79936000e+08, -6.70552254e-08, 0.00000000e+00, -1.39968000e+08, 3.88800000e+07, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 3.88800000e+07, 7.20000000e+06, 0.00000000e+00, -6.70552254e-08, 2.88000000e+07, 0.00000000e+00, -3.88800000e+07, 7.20000000e+06, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, -3.60000000e+11, 0.00000000e+00, 0.00000000e+00, 7.20000000e+11, 0.00000000e+00, 0.00000000e+00, -3.60000000e+11, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, -1.39968000e+08, -3.88800000e+07, 0.00000000e+00, 2.79936000e+08, 6.70552254e-08, 0.00000000e+00, -1.39968000e+08, 3.88800000e+07, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 3.88800000e+07, 7.20000000e+06, 0.00000000e+00, 6.70552254e-08, 2.88000000e+07, 0.00000000e+00, -3.88800000e+07, 7.20000000e+06, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, -3.60000000e+11, 0.00000000e+00, 0.00000000e+00, 7.20000000e+11, 0.00000000e+00, 0.00000000e+00, -3.60000000e+11, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, -1.39968000e+08, -3.88800000e+07, 0.00000000e+00, 2.79936000e+08, -6.70552254e-08, 0.00000000e+00, -1.39968000e+08, 3.88800000e+07, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 3.88800000e+07, 7.20000000e+06, 0.00000000e+00, -6.70552254e-08, 2.88000000e+07, 0.00000000e+00, -3.88800000e+07, 7.20000000e+06, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, -3.60000000e+11, 0.00000000e+00, 0.00000000e+00, 7.20000000e+11, 0.00000000e+00, 0.00000000e+00, -3.60000000e+11, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, -1.39968000e+08, -3.88800000e+07, 0.00000000e+00, 2.79936000e+08, 6.70552254e-08, 0.00000000e+00, -1.39968000e+08, 3.88800000e+07,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 3.88800000e+07, 7.20000000e+06, 0.00000000e+00, 6.70552254e-08, 2.88000000e+07, 0.00000000e+00, -3.88800000e+07, 7.20000000e+06,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, -3.60000000e+11, 0.00000000e+00, 0.00000000e+00, 3.60000000e+11, 0.00000000e+00, 0.00000000e+00,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, -1.39968000e+08, -3.88800000e+07, 0.00000000e+00, 1.39968000e+08, -3.88800000e+07,},
                {0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 0.00000000e+00, 3.88800000e+07, 7.20000000e+06, 0.00000000e+00, -3.88800000e+07, 1.44000000e+07,},
        };
        // Vector V //
        double[][] v = new double[][]{
                {0.00000000e+00}, {0.00000000e+00}, {0.00000000e+00}, {0.00000000e+00}, {0.00000000e+00},
                {0.00000000e+00}, {0.00000000e+00}, {0.00000000e+00}, {0.00000000e+00}, {0.00000000e+00},
                {0.00000000e+00}, {0.00000000e+00}, {0.00000000e+00}, {0.00000000e+00}, {0.00000000e+00},
                {0.00000000e+00}, {0.00000000e+00}, {0.00000000e+00}, {0.00000000e+00}, {0.00000000e+00},
                {0.00000000e+00}, {0.00000000e+00}, {0.00000000e+00}, {0.00000000e+00}, {0.00000000e+00},
                {0.00000000e+00}, {0.00000000e+00}, {0.00000000e+00}, {1.00000000e+04}, {0.00000000e+00},};
        solve(a, v);
    }

    public void solve(double[][] a, double[][] v, double... result) {
        boolean allIsOk = true;
        for (SolverSystemOfLinearEquations solver : SolversLA.solvers) {
            System.out.println(solver.getClass().toString());
            try {
                Matrix actual = solver.solve(new Matrix(a), new Matrix(v));
                actual.print(5, 12);
                if (result.length > 0) {
                    boolean isOk = true;
                    for (int i = 0; i < actual.getRowDimension(); i++) {
                        if (Math.abs(actual.get(i, 0) - result[i]) / result[i] > 1e-9) {
                            isOk = false;
                        }
                    }
                    if (isOk)
                        System.out.println("Correct");
                    else {
                        System.out.println("Wrong answer");
                        allIsOk = false;
                    }
                    System.out.println("==================");
                }
            } catch (Exception e) {
                System.out.println(solver.getClass().toString() + " is error");
                e.printStackTrace();
            }
        }
        assertTrue(allIsOk);
    }
}