package home.linearAlgebra;

public class SolversLA {

    public static final SolverSystemOfLinearEquations bestSolverLA = new JamaSolver();

    public static SolverSystemOfLinearEquations solvers[] = new SolverSystemOfLinearEquations[]{
            new JamaSolver(),
            new PerpendSolver(),
            new SimpleIterationSolver()
    };
}
