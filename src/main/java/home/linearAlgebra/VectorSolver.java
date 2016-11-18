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
//            System.out.println("ro[" + i + "] = " + ro);
//            averageRo = Math.max(Math.abs(ro), averageRo);//+= ro / A.getRowDimension();
            averageRo += ro * ro;
        }
        averageRo = Math.sqrt(averageRo);
//        System.out.println("Max = " + averageRo + "\n");
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

        double[] x_last = null;
        for (int i = 0; i < 250; i++) {
            // Calculate next point //
            x = calculateNextPoint(x, A, v);
//            if (x_last == null) {
//                x_last = x;
//            } else {
//                x = calculatePrecisionPoint(
//                        x, errorRo(x, A, v),
//                        x_last, errorRo(x_last, A, v));
////                x_last = calculatePrecisionPoint(
////                        x, errorRo(x, A, v),
////                        x_last, errorRo(x_last, A, v));
////                if(errorRo(x_last,A,v) < errorRo(x, A, v))
////                    x = x_last;
//                x_last = null;
//            }
            if (errorRo(x, A, v) < 1e-9)
                break;

            System.out.println("ERROR : " + errorRo(x, A, v));
        }

        double[][] result = new double[size][1];
        for (int i = 0; i < size; i++) {
            result[i][0] = x[i];
        }
        System.out.println("ERROR : " + errorRo(x, A, v));
        return new Matrix(result);
    }

    private double[] calculatePrecisionPoint(double[] x, double v, double[] x1, double v1) {
        boolean isSame = true;
        for (int i = 0; i < x.length; i++) {
            if (x[i] != x1[i]) {
                isSame = false;
                break;
            }
        }
        if (isSame) {
            return x1;
        }


        double factor = 1;//.01;//0.75;// must be 1

        double[] result = new double[x.length];
        for (int i = 0; i < result.length; i++) {
            double a = (v1 - v) / (x[i] - x1[i]);
//            double b = v + a * x[i];
//            result[i] = b / a;
            result[i] = v / a * factor + x[i];
        }

        for (int i = 0; i < result.length; i++) {
            if (result[i] == Double.NaN || result[i] == Double.POSITIVE_INFINITY || result[i] == Double.NEGATIVE_INFINITY) {
                System.out.println("Wrong result");
                return x;
            }
        }


//        int size = x.length;
//        System.out.println("calculatePrecisionPoint()");
//        for (int i = 0; i < size; i++) {
//            System.out.println(
//                    "x [" + i + "] == " + x[i] + "\t" +
//                            "x1[" + i + "] == " + x1[i] + "\t" +
//                            "res[" + i + "] == " + result[i]
//            );
//        }
        System.out.println("shoot");
        return result;
    }

    private double[] calculateNextPoint(double[] x, Matrix a, Matrix v) {

//        if (Math.abs(errorRo(x, a, v)) < 1e-10)
//            return x;

        int size = a.getColumnDimension();


        for (int i = 0; i < size; i++) {
            System.out.println("BEGIN : Coordinate " + i + " == " + x[i]);
        }


        for (int i = 0; i < size; i++) {
            double up = 0;
            for (int j = 0; j < size; j++) {
                up += a.get(i, j) * x[j];
            }
            up -= v.get(i, 0);
            double down = 0;
            for (int j = 0; j < size; j++) {
                down += Math.pow(a.get(i, j), 2.);
            }
            double distance = up / Math.sqrt(down);
//            System.out.println("Distance " + i + " = " + distance);
        }
        double[] result = new double[size];
        for (int i = 0; i < size; i++) {
            double up = 0;
            for (int j = 0; j < size; j++) {
                up += a.get(i, j) * x[j];
            }
            up -= v.get(i, 0);
            double down = 0;
            for (int j = 0; j < size; j++) {
                down += Math.pow(a.get(i, j), 2.);
            }
            double t = -up / down;
//            System.out.println("Factor " + i + " = " + t);
//            System.out.println("Coordinates:");
            for (int j = 0; j < size; j++) {
                result[j] += (a.get(i, j) * t + x[j]);
//                System.out.println("[x" + j + "] = " + (a.get(i, j) * t + x[j]));
            }
        }
        for (int i = 0; i < size; i++) {
            result[i] /= (double) size;
//            System.out.println("END: Coordinate " + i + " == " + result[i]);
        }
//        double[][] roots = new double[size][size];
//        for (int i = 0; i < size; i++) {
//            for (int j = 0; j < size; j++) {
//                double xTemp = 0;
//                for (int k = 0; k < size; k++) {
//                    xTemp = x[k] + a.get(j, k);
//                }
//                roots[j][i] = xTemp;
//            }
//        }
//        double[] result = new double[size];
//        for (int i = 0; i < size; i++) {
//            result[i] = 0;
//            for (int j = 0; j < size; j++) {
//                result[i] += roots[i][j] / (double) size;
//            }
//        }
//        System.out.println("\n\n\n\n");
        return result;
    }

}
