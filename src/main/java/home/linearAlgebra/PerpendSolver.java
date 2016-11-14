package home.linearAlgebra;

import Jama.Matrix;

public class PerpendSolver implements SolverSystemOfLinearEquations {
    @Override
    public Matrix solve(Matrix A, Matrix v) {
        int size = A.getColumnDimension();
        double[] x = new double[size];
//        for (int i = 0; i < size; i++) {
//            x[i] = 0.001;
//        }

        int[] factors = new int[size];
        for (int i = 0; i < size; i++) {
            factors[i] = 1;
        }

//        A.print(15,1);

        for (int i = 0; i < size; i++) {
            double min = +Double.MAX_VALUE;
            double max = -Double.MAX_VALUE;
            for (int j = 0; j < size; j++) {
                double value = Math.abs(A.get(j, i));
                if(value == 0D)
                    continue;
                min = Math.min(min, value);
                max = Math.max(max, value);
            }
//            System.out.println("||" + min + "," + max + "||");
            min = Math.log10(min);
            max = Math.log10(max);
            int average = (int)((min + max) / 2.);
            factors[i] = average;
            for (int j = 0; j < size; j++) {
                A.set(j, i, A.get(j, i) * Math.pow(10, -factors[i]));
            }
//            System.out.println("Log10 = ||" + min + "," + max + "|| -- >"+factors[i]);
        }

//        A.print(10,1);

        for (int i = 0; i < 25; i++) {
            double[] x_last = new double[size];
            for (int j = 0; j < size; j++) {
                x_last[j] = x[j];
            }
//            print
//            System.out.println("------------------------");
//            System.out.println("Iteration :" + i);
//            for (int h = 0; h < size; h++) {
//                System.out.println("[" + x[h] + "]");
//            }
//            System.out.println("------------------------");
//

            for (int j = 0; j < size; j++) {
                // Coordinate t = 0
//                double[] x0 = new double[size];
//                for (int k = 0; k < size; k++) {
//                    x0[k] = x[k];
//                }
                double p0 = 0;
                for (int k = 0; k < size; k++) {
                    p0 += A.get(j, k) * x[k];//x0[k];
                }
                p0 -= v.get(j, 0);
                // Coordinate t = t1
                double t1 = 0.001;
                double[] x1 = new double[size];
                for (int k = 0; k < size; k++) {
                    x1[k] = A.get(j, k) * t1 + x[k];
                }
                double p1 = 0;
                for (int k = 0; k < size; k++) {
                    p1 += A.get(j, k) * x1[k];
                }
                p1 -= v.get(j, 0);
                // Interpolation. find t*
                double c = -p0;
                double b = (-c - p1) / t1;
                double t_star = -c / b;
                // Calculate coordinates
                for (int k = 0; k < size; k++) {
                    x[k] = A.get(j, k) * t_star + x[k];
                }
            }
//            System.out.println("------------------------");
//            System.out.println("ERROR:");
//            for (int j = 0; j < size; j++) {
//                System.out.println(x[j] - x_last[j]);
//            }
//            System.out.println("------------------------");
        }

        for (int i = 0; i < size; i++) {
            x[i] = x[i] / Math.pow(10, factors[i]);
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
