package home.linearAlgebra;

import Jama.Matrix;
import org.junit.Test;

public class SolversLATest {

    @Test
    public void matrix1() {
        double[][] a = new double[][]{
                {1, -2, 3},
                {-2, 2, 7},
                {3, 7, 5}
        };
        double[][] v = new double[][]{{2}, {7}, {15}};
        for (SolverSystemOfLinearEquations solver : SolversLA.solvers) {
            solve(solver, a, v);
        }
    }

    @Test
    public void matrix2() {
        double[][] a = new double[][]{
                {0, 1, -2},
                {1, 4, 3},
                {-2, 3, 4}
        };
        double[][] v = new double[][]{{-1}, {8}, {5}};
        for (SolverSystemOfLinearEquations solver : SolversLA.solvers) {
            solve(solver, a, v);
        }
    }

    @Test
    public void matrix3() {
        double[][] a = new double[][]{
                {1, 1e-100},
                {1e-100, 1}
        };
        double[][] v = new double[][]{{1 + 1e-100}, {1 + 1e-100}};
        for (SolverSystemOfLinearEquations solver : SolversLA.solvers) {
            solve(solver, a, v);
        }
    }

    @Test
    public void matrix4() {
        double[][] a = new double[][]{
                {1e100, 1e-100},
                {1e-100, 1e100}
        };
        double[][] v = new double[][]{{1e100 + 1e-100}, {1e100 + 1e-100}};
        for (SolverSystemOfLinearEquations solver : SolversLA.solvers) {
            solve(solver, a, v);
        }
    }

    @Test
    public void matrix5() {
        double[][] a = new double[][]{
                {1e-100, 1e100},
                {1e100, 1e-100}
        };
        double[][] v = new double[][]{{1e100 + 1e-100}, {1e100 + 1e-100}};
        for (SolverSystemOfLinearEquations solver : SolversLA.solvers) {
            solve(solver, a, v);
        }
    }

    @Test
    public void matrix6() {
        double[][] a = new double[][]{
                {1e-200, 1e200},
                {1e200, 1e-200}
        };
        double[][] v = new double[][]{{1e200 + 1e-200}, {1e200 + 1e-200}};
        for (SolverSystemOfLinearEquations solver : SolversLA.solvers) {
            solve(solver, a, v);
        }
    }

    @Test
    public void matrix7() {
        // Matrix A //
        double[][] a = new double[][]{
                {1.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,},
                {0.00000000e+00,1.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,},
                {0.00000000e+00,0.00000000e+00,1.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,},
                {0.00000000e+00,0.00000000e+00,0.00000000e+00,2.40000000e+11,0.00000000e+00,0.00000000e+00,-1.20000000e+11,0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,},
                {0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,1.03680000e+07,0.00000000e+00,0.00000000e+00,-5.18400000e+06,4.32000000e+06,0.00000000e+00,0.00000000e+00,0.00000000e+00,},
                {0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,9.60000000e+06,0.00000000e+00,-4.32000000e+06,2.40000000e+06,0.00000000e+00,0.00000000e+00,0.00000000e+00,},
                {0.00000000e+00,0.00000000e+00,0.00000000e+00,-1.20000000e+11,0.00000000e+00,0.00000000e+00,2.40000000e+11,0.00000000e+00,0.00000000e+00,-1.20000000e+11,0.00000000e+00,0.00000000e+00,},
                {0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,-5.18400000e+06,-4.32000000e+06,0.00000000e+00,1.03680000e+07,1.86264515e-09,0.00000000e+00,-5.18400000e+06,4.32000000e+06,},
                {0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,4.32000000e+06,2.40000000e+06,0.00000000e+00,1.86264515e-09,9.60000000e+06,0.00000000e+00,-4.32000000e+06,2.40000000e+06,},
                {0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,-1.20000000e+11,0.00000000e+00,0.00000000e+00,1.20000000e+11,0.00000000e+00,0.00000000e+00,},
                {0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,-5.18400000e+06,-4.32000000e+06,0.00000000e+00,5.18400000e+06,-4.32000000e+06,},
                {0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,0.00000000e+00,4.32000000e+06,2.40000000e+06,0.00000000e+00,-4.32000000e+06,4.80000000e+06,},
        };
        // Vector V //
        double[][] v = new double[][]{
                {0.00000000e+00},{0.00000000e+00},{0.00000000e+00},{0.00000000e+00},{0.00000000e+00},{0.00000000e+00},{0.00000000e+00},{0.00000000e+00},{0.00000000e+00},{0.00000000e+00},{1.00000000e+04},{0.00000000e+00},};
        for (SolverSystemOfLinearEquations solver : SolversLA.solvers) {
            solve(solver, a, v);
        }
    }

    public void solve(SolverSystemOfLinearEquations solver, double[][] a, double[][] v) {
        try {
            solver.solve(new Matrix(a), new Matrix(v)).print(5, 12);
        } catch (Exception e) {
            System.out.println(solver.getClass().toString() + " is error");
            e.printStackTrace();
        }
    }
}