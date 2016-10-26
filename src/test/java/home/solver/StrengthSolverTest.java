package home.solver;

import home.finiteElement.FemBeam2d;
import home.finiteElement.FemElement;
import home.other.Direction;
import home.other.FemPoint;
import home.other.Force;
import home.other.Support;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StrengthSolverTest {

    @Test
    public void testBending() {

        FemPoint[] femPoints = new FemPoint[]{
                new FemPoint(0, 0, 0),
                new FemPoint(1, 5, 0),
        };

        FemElement[] lines = new FemElement[]{
                new FemBeam2d(2e11, 53.8e-4, 8356e-8, new FemPoint[]{femPoints[0], femPoints[1]}),
        };

        Force[] forces = new Force[]{
                new Force(femPoints[1], Direction.DIRECTION_Y, 10000),
        };

        Support[] supports = new Support[]{
                new Support(femPoints[0], Direction.DIRECTION_X),
                new Support(femPoints[0], Direction.DIRECTION_Y),
                new Support(femPoints[0], Direction.ROTATE),
        };

        //=========================//
        try {
            StrengthSolver.calculate(femPoints, lines, forces, supports);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(lines[0].getInternalForce().get(1, 0), -10000, 1e-5);
        assertEquals(lines[0].getInternalForce().get(2, 0), -50000, 1e-5);
        assertEquals(lines[0].getDisplacementInGlobalSystem().get(4, 0), 0.0249, 1e-4);
        assertEquals(lines[0].getDisplacementInLocalSystem().get(4, 0), 0.0249, 1e-4);

        assertEquals(femPoints[0].getGlobalDisplacement()[0], 0.0000, 1e-4);
        assertEquals(femPoints[1].getGlobalDisplacement()[1], 0.0249, 1e-4);
    }


    @Test
    public void testBendingWithIntermediantPoints() {
        // E = 2e11
        // J = 1e-5
        // A = 1
        // P = 1e4
        // l = 5
        // Vmax = P*l^3/(3*E*J) = 0.2083333

        int amount = 3;

        FemPoint[] femPoints = new FemPoint[amount];
        for (int i = 0; i < amount; i++) {
            femPoints[i] = new FemPoint(i, i * 5. / (amount - 1), 0);
        }

        FemElement[] lines = new FemElement[amount - 1];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = new FemBeam2d(2e11, 1, 1e-5, new FemPoint[]{femPoints[i], femPoints[i + 1]});
        }

        Force[] forces = new Force[]{
                new Force(femPoints[amount - 1], Direction.DIRECTION_Y, 1e4),
        };

        Support[] supports = new Support[]{
                new Support(femPoints[0], Direction.DIRECTION_X),
                new Support(femPoints[0], Direction.DIRECTION_Y),
                new Support(femPoints[0], Direction.ROTATE),
        };

        //=========================//
        try {
            StrengthSolver.calculate(femPoints, lines, forces, supports);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(lines[0].getInternalForce().get(0, 0), 0, 1e-5);
        assertEquals(lines[0].getInternalForce().get(1, 0), -1e4, 1e-5);
        assertEquals(lines[0].getInternalForce().get(2, 0), -5e4, 1e-5);

        assertEquals(femPoints[0].getGlobalDisplacement()[0], 0.0000, 1e-4);
        assertEquals(femPoints[amount - 1].getGlobalDisplacement()[1], 0.2083333, 1e-4);
    }

}