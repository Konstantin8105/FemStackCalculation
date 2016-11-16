package home.linearAlgebra;

import Jama.Matrix;

public class VectorSolver implements SolverSystemOfLinearEquations {
    private static final double epsilon = 1e-9;

    double errorRo(double[] x, Matrix A, Matrix v) {
        // ro = A*x-b //
        double averageRo = 0;
        for (int i = 0; i < A.getRowDimension(); i++) {
            double ro = 0;
            for (int j = 0; j < A.getColumnDimension(); j++) {
                ro += A.get(i, j) * x[j];
            }
            ro = ro - v.get(i, 0);
            averageRo += ro / A.getRowDimension();
        }
        return averageRo;
    }

    @Override
    public Matrix solve(Matrix A, Matrix v) {
        int size = A.getColumnDimension();
        double[] x = new double[size];

        // Find preliminary solution //
        boolean goodPreliminaryValues = false;
        while (!goodPreliminaryValues) {
            double ro = errorRo(x, A, v);
            if (ro == Double.NaN || ro == Double.NEGATIVE_INFINITY || ro == Double.POSITIVE_INFINITY) {
                System.out.println("Wrong input data x[]");
            } else {
                goodPreliminaryValues = true;
            }
        }

        for (int i = 0; i < 25; i++) {
            // Calculate next point //

            double[] x1 = calculateNextPoint(x, A, v);

            // Calculate errors ro //

            double ro_0 = errorRo(x, A, v);
            double ro_1 = errorRo(x1, A, v);

            x = calculatePrecisionPoint(x, ro_0, x1, ro_1);
        }

        double[][] result = new double[size][1];
        for (int i = 0; i < size; i++) {
            result[i][0] = x[i];
        }
        return new Matrix(result);
    }

    private double[] calculateNextPoint(double[] x, Matrix a, Matrix v) {
        int size = a.getColumnDimension();
        double[][] roots = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                double xTemp = 0;
                for (int k = 0; k < size; k++) {
                    xTemp = x[k] + a.get(j, k);
                }
                roots[j][i] = xTemp;
            }
        }


        return new double[0];
    }

    private double[] calculatePrecisionPoint(double[] x, double ro_0, double[] x1, double ro_1) {
        ;
    }
}
