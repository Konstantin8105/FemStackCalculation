package home.linearAlgebra;

import Jama.Matrix;

public class SimpleIterationSolver implements SolverSystemOfLinearEquations {
    @Override
    public Matrix solve(Matrix A, Matrix v) {
        int size = A.getColumnDimension();
        double[] x = new double[size];

        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < size; j++) {
                double xi = v.get(j, 0);
                for (int k = 0; k < size; k++) {
                    if (k != j)
                        xi -= A.get(j, k) * x[k];
                }
                x[j] = xi / A.get(j, j);
            }
        }

        System.out.println("------------------------");
        System.out.println("Result:");
        for (int h = 0; h < size; h++) {
            System.out.println("[" + String.format("%.10f", x[h]) + "]");
        }
        System.out.println("------------------------");

        double[][] result = new double[size][1];
        for (int i = 0; i < size; i++) {
            result[i][0] = x[i];
        }
        return new Matrix(result);
    }
}
