package home;

import jama.Matrix;

public class BooleanMatrix {
    /* ------------------------
       Class variables
     * ------------------------ */

    /**
     * Array for internal storage of elements.
     *
     * @serial internal array storage.
     */
    private final boolean[][] A;

    /**
     * Row and column dimensions.
     *
     * @serial row dimension.
     * @serial column dimension.
     */
    private final int m, n;

    /* ------------------------
       Constructors
     * ------------------------ */

    /**
     * Construct a matrix from a 2-D array.
     *
     * @param A Two-dimensional array of doubles.
     * @throws IllegalArgumentException All rows must have the same length
     */

    public BooleanMatrix(boolean[][] A) {
        m = A.length;
        n = A[0].length;
        for (int i = 0; i < m; i++) {
            if (A[i].length != n) {
                throw new IllegalArgumentException("All rows must have the same length.");
            }
        }
        this.A = A;
    }


    /**
     * Matrix transpose.
     *
     * @return A'
     */

    public BooleanMatrix transpose() {
        boolean[][] matrixNew = new boolean[n][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                matrixNew[j][i] = A[i][j];
            }
        }
        return new BooleanMatrix(matrixNew);
    }


    /**
     * Linear algebraic matrix multiplication, A * B
     *
     * @param B another matrix
     * @return Matrix product, A * B
     * @throws IllegalArgumentException Matrix inner dimensions must agree.
     */

    public Matrix times(Matrix B) {
        if (B.getRowDimension() != n) {
            throw new IllegalArgumentException(
                    "Matrix inner dimensions must agree. (B.m != n) " + B.getRowDimension() + " != " + n);
        }
        Matrix X = new Matrix(m, B.getColumnDimension());
        double[][] C = X.getArray();
        double[] Bcolj = new double[n];
        for (int j = 0; j < B.getColumnDimension(); j++) {
            for (int k = 0; k < n; k++) {
                Bcolj[k] = B.getArray()[k][j];
            }
            for (int i = 0; i < m; i++) {
                //double[] Arowi = A[i];
                double s = 0;
                for (int k = 0; k < n; k++) {
                    //s += Arowi[k] * Bcolj[k];
                    if (A[i][k])
                        s += Bcolj[k];
                }
                C[i][j] = s;
            }
        }
        return X;
    }

    /**
     * Get row dimension.
     *
     * @return m, the number of rows.
     */

    public int getRowDimension() {
        return m;
    }

    /**
     * Get column dimension.
     *
     * @return n, the number of columns.
     */

    public int getColumnDimension() {
        return n;
    }

    public Matrix convert() {
        Matrix matrix = new Matrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (A[i][j]) matrix.getArray()[i][j] = 1;
                else matrix.getArray()[i][j] = 0;
            }
        }
        return matrix;
    }

    public Matrix transposeAndMultiply(Matrix B) {
        if (B.getRowDimension() != m) {
            throw new IllegalArgumentException(
                    "Matrix inner dimensions must agree. (B.m != n) " + B.getRowDimension() + " != " + m);
        }
        Matrix X = new Matrix(n, B.getColumnDimension());
        double[][] C = X.getArray();
        double[] Bcolj = new double[m];
        for (int j = 0; j < B.getColumnDimension(); j++) {
            for (int k = 0; k < m; k++) {
                Bcolj[k] = B.getArray()[k][j];
            }
            for (int i = 0; i < n; i++) {
                double s = 0;
                for (int k = 0; k < m; k++) {
                    if (A[k][i])
                        s += Bcolj[k];
                }
                C[i][j] = s;
            }
        }
        return X;
    }
}
