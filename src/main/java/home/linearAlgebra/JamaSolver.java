package home.linearAlgebra;

import Jama.Matrix;

public class JamaSolver implements SolverSystemOfLinearEquations{
    @Override
    public Matrix solve(Matrix A, Matrix v) {
        return A.solve(v);
    }
}
