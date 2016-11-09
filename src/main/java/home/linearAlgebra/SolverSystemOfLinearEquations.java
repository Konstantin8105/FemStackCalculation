package home.linearAlgebra;

import Jama.Matrix;

public interface SolverSystemOfLinearEquations {
    Matrix solve(Matrix A, Matrix v);
}
