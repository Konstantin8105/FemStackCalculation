package home.solver.matrixes;

import Jama.Matrix;

import java.util.ArrayList;
import java.util.List;

public class SparseZeroOneMatrix {
    List<Integer>[] array;
    int rows;
    int columns;

    public SparseZeroOneMatrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        array = new List[columns];
        for (int i = 0; i < columns; i++) {
            array[i] = new ArrayList<>(6);
        }
    }

    public void addOne(int row, int column) {
        array[column].add(row);
    }

    public Matrix convert() {
        double[][] matrix = new double[rows][columns];
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < array[i].size(); j++) {
                matrix[array[i].get(j)][i] = 1;
            }
        }
        return new Matrix(matrix);
    }

//    public Matrix times(Matrix B) {
//        /** Construct an m-by-n matrix of zeros.
//         @param m    Number of rows.
//         @param n    Number of colums.
//         */
//
//        if (B.getRowDimension() != columns) {
//            throw new IllegalArgumentException("Matrix inner dimensions must agree.");
//        }
//        Matrix X = new Matrix(rows, B.getColumnDimension());
//        double[][] C = X.getArray();
//        double[] Bcolj = new double[columns];
//        for (int j = 0; j < B.getColumnDimension(); j++) {
//            for (int k = 0; k < columns; k++) {
//                Bcolj[k] = B.getArray()[k][j];
//            }
//            for (int i = 0; i < rows; i++) {
//                double s = 0;
//                for (int k = 0; k < columns; k++) {
//                    s += A[i][k] * Bcolj[k];
//                }
//                C[i][j] = s;
//            }
//        }
//        return X;
//    }
}
