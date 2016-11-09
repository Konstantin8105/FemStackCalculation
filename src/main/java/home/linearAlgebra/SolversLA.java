package home.linearAlgebra;

public class SolversLA {

    public static SolverSystemOfLinearEquations bestSolverLA = new JamaSolver();

    public static SolverSystemOfLinearEquations solvers[] = new SolverSystemOfLinearEquations[]{
            new JamaSolver(),
    };
}
