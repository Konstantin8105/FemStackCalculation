package home.solver.matrixes;

import Jama.Matrix;

import java.util.ArrayList;
import java.util.List;

public class SparseSquareSymmetricMatrix {

    private int amount;
    private int maxSizeInternalMatrix;

    private class InternalMatrix {
        int positionOnDiagonal;
        Matrix matrix;

        public InternalMatrix(int positionOnDiagonal, Matrix matrix) {
            this.positionOnDiagonal = positionOnDiagonal;
            this.matrix = matrix;
        }
    }

    List<InternalMatrix> internalMatrices = new ArrayList<>();

    public SparseSquareSymmetricMatrix(int amount, int maxSizeInternalMatrix) {
        this.amount = amount;
        this.maxSizeInternalMatrix = maxSizeInternalMatrix;
    }

    public void add(int positionBaseLine, Matrix matrix) {
        internalMatrices.add(new InternalMatrix(positionBaseLine, matrix));
    }

    public int getRowDimension() {
        return amount;
    }

    public int getColumnDimension() {
        return amount;
    }

    public double get(int row, int column) {
        if (column < row - maxSizeInternalMatrix / 2 && row < column - maxSizeInternalMatrix / 2) {
            return 0;
        }
        if (column > row + maxSizeInternalMatrix / 2 && row > column + maxSizeInternalMatrix / 2) {
            return 0;
        }
        InternalMatrix internalMatrix = findInternalMatrix(row);
        int localRow = row - internalMatrix.positionOnDiagonal;
        int localColumn = column - internalMatrix.positionOnDiagonal;
        if (localColumn < 0)
            return 0;
        if (localColumn >= internalMatrix.matrix.getRowDimension())
            return 0;
        return internalMatrix.matrix.getArray()[localRow][localColumn];
    }

    private InternalMatrix findInternalMatrix(int row) {
        InternalMatrix intM = internalMatrices.get(row / maxSizeInternalMatrix);
        if (intM.positionOnDiagonal <= row &&
                row < intM.positionOnDiagonal + intM.matrix.getRowDimension()) {
            return intM;
        }
        // TODO: 11/4/16 add binary searcher
        for (InternalMatrix internalMatrix : internalMatrices) {
            if (internalMatrix.positionOnDiagonal <= row &&
                    row < internalMatrix.positionOnDiagonal + internalMatrix.matrix.getRowDimension()) {
                return internalMatrix;
            }
        }
        return null;
    }

    public Matrix convert() {
        double[][] result = new double[amount][amount];
        for (InternalMatrix internalMatrix : internalMatrices) {
            for (int i = 0; i < internalMatrix.matrix.getRowDimension(); i++) {
                for (int j = 0; j < internalMatrix.matrix.getColumnDimension(); j++) {
                    result[i + internalMatrix.positionOnDiagonal][j + internalMatrix.positionOnDiagonal] =
                            internalMatrix.matrix.getArray()[i][j];
                }
            }
        }
        return new Matrix(result);
    }

}
