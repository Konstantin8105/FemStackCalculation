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
            solver.solve(new Matrix(a), new Matrix(v)).print(5, 12);
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
            solver.solve(new Matrix(a), new Matrix(v)).print(5, 12);
        }
    }
}