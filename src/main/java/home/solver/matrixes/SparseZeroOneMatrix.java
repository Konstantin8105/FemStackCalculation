package home.solver.matrixes;

import Jama.Matrix;

import java.util.ArrayList;
import java.util.List;

public class SparseZeroOneMatrix {

    private final static int MINIMAL_ARRAY_ALLOCATION = 2;

    private enum MatrixOrientation {
        MATRIX_ORIENTATION_ROW,
        MATRIX_ORIENTATION_COLUMN
    }

    private MatrixOrientation orientation;
    private List<Integer>[] array;
    private int rows;
    private int columns;

    public SparseZeroOneMatrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        orientation = MatrixOrientation.MATRIX_ORIENTATION_ROW;
        array = new List[rows];
        for (int i = 0; i < rows; i++) {
            array[i] = new ArrayList<>(MINIMAL_ARRAY_ALLOCATION);
        }
    }

    public void addOne(int row, int column) {
        switch (orientation) {
            case MATRIX_ORIENTATION_COLUMN:
                array[column].add(row);
                break;
            case MATRIX_ORIENTATION_ROW:
                array[row].add(column);
                break;
        }
    }

    public Matrix convert() {
        double[][] matrix = new double[rows][columns];
        switch (orientation) {
            case MATRIX_ORIENTATION_COLUMN: {
                for (int i = 0; i < columns; i++) {
                    for (int j = 0; j < array[i].size(); j++) {
                        matrix[array[i].get(j)][i] = 1;
                    }
                }
                break;
            }
            case MATRIX_ORIENTATION_ROW: {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < array[i].size(); j++) {
                        matrix[i][array[i].get(j)] = 1;
                    }
                }
                break;
            }
        }
        return new Matrix(matrix);
    }


    private List<Integer>[] rotateFromColumnToRow() {
        List<Integer>[] rotateArray = new List[rows];
        for (int i = 0; i < rows; i++) {
            rotateArray[i] = new ArrayList<>(MINIMAL_ARRAY_ALLOCATION);
        }
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < array[i].size(); j++) {
                int row = array[i].get(j);
                int column = i;
                rotateArray[row].add(column);
            }
        }
        return rotateArray;
    }

    private List<Integer>[] rotateFromRowToColumn() {
        List<Integer>[] rotateArray = new List[columns];
        for (int i = 0; i < columns; i++) {
            rotateArray[i] = new ArrayList<>(MINIMAL_ARRAY_ALLOCATION);
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < array[i].size(); j++) {
                int row = i;
                int column = array[i].get(j);
                rotateArray[column].add(row);
            }
        }
        return rotateArray;
    }

    public Matrix times(Matrix B) {
        if (B.getRowDimension() != columns) {
            throw new IllegalArgumentException("Matrix inner dimensions must agree.");
        }
        double[][] result = new double[rows][B.getColumnDimension()];

        List<Integer>[] matrix = null;

        switch (orientation) {
            case MATRIX_ORIENTATION_ROW: {
                matrix = array;
                break;
            }
            case MATRIX_ORIENTATION_COLUMN: {
                matrix = rotateFromColumnToRow();
                break;
            }
        }

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < B.getColumnDimension(); j++) {
                double sum = 0;
                for (int h = 0; h < matrix[i].size(); h++) {
                    sum += B.getArray()[matrix[i].get(h)][j];
                }
                result[i][j] = sum;
            }
        }
        return new Matrix(result);
    }

    public Matrix times(SparseSquareSymmetricMatrix B) {
        if (B.getRowDimension() != columns) {
            throw new IllegalArgumentException("Matrix inner dimensions must agree.");
        }
        double[][] result = new double[rows][B.getColumnDimension()];

        List<Integer>[] matrix = null;

        switch (orientation) {
            case MATRIX_ORIENTATION_ROW: {
                matrix = array;
                break;
            }
            case MATRIX_ORIENTATION_COLUMN: {
                matrix = rotateFromColumnToRow();
                break;
            }
        }

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < B.getColumnDimension(); j++) {
                double sum = 0;
                for (int h = 0; h < matrix[i].size(); h++) {
                    sum += B.get(matrix[i].get(h),j);
                }
                result[i][j] = sum;
            }
        }
        return new Matrix(result);
    }

    public SparseZeroOneMatrix transpose() {
        SparseZeroOneMatrix result = new SparseZeroOneMatrix(rows, columns);
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].size(); j++) {
                result.array[i].add(array[i].get(j));
            }
        }
        switch (orientation){
            case MATRIX_ORIENTATION_COLUMN:{
                result.orientation = MatrixOrientation.MATRIX_ORIENTATION_ROW;
                break;
            }
            case MATRIX_ORIENTATION_ROW:{
                result.orientation = MatrixOrientation.MATRIX_ORIENTATION_COLUMN;
            }
        }
        result.rows = columns;
        result.columns = rows;
        return result;
    }


    public static Matrix multiply(Matrix A, SparseZeroOneMatrix B) {
        if (B.rows != A.getColumnDimension()) {
            throw new IllegalArgumentException("Matrix inner dimensions must agree.");
        }

        double[][] result = new double[A.getRowDimension()][B.columns];

        List<Integer>[] matrix = null;

        switch (B.orientation) {
            case MATRIX_ORIENTATION_ROW: {
                matrix = B.rotateFromRowToColumn();
                break;
            }
            case MATRIX_ORIENTATION_COLUMN: {
                matrix = B.array;
                break;
            }
        }

        for (int i = 0; i < A.getRowDimension(); i++) {
            for (int j = 0; j < matrix.length; j++) {
                double sum = 0;
                for (int k = 0; k < matrix[j].size(); k++) {
                    sum += A.getArray()[i][matrix[j].get(k)];
                }
                result[i][j] = sum;
            }
        }

        return new Matrix(result);
    }


    public static Matrix multiplyWithSquareSymmetric(Matrix A, SparseZeroOneMatrix B) {
        if (A.getColumnDimension() != B.rows) {
            throw new IllegalArgumentException("Matrix inner dimensions must agree.");
        }
        if(A.getRowDimension() != B.columns) {
            throw new IllegalArgumentException("Matrix inner dimensions must agree.");
        }

        int size = A.getRowDimension();
        double[][] result = new double[size][size];

        List<Integer>[] matrix = null;

        switch (B.orientation) {
            case MATRIX_ORIENTATION_ROW: {
                matrix = B.rotateFromRowToColumn();
                break;
            }
            case MATRIX_ORIENTATION_COLUMN: {
                matrix = B.array;
                break;
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
                double sum = 0;
                for (int k = 0; k < matrix[j].size(); k++) {
                    sum += A.getArray()[i][matrix[j].get(k)];
                }
                result[i][j] = sum;
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < i; j++) {
                result[i][j] = result[j][i];
            }
        }

        return new Matrix(result);
    }

}
