package home.solver;

import home.finiteElements.FemBeam2d;
import home.finiteElements.interfaces.FemElement;
import home.loads.Direction;
import home.other.FemPoint;
import home.loads.Force;
import home.other.Support;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class StrengthSolverTest {

    @Test
    public void testBending() {

        FemPoint[] femPoints = new FemPoint[]{
                new FemPoint(0, 0, 0),
                new FemPoint(1, 5, 0),
        };

        FemBeam2d[] femElements = new FemBeam2d[]{
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
        boolean exception = false;
        StrengthSolver solver = null;
        try {
            solver = new StrengthSolver(femPoints, femElements, forces, supports);
        } catch (Exception e) {
            e.printStackTrace();
            exception = true;
        }

        assertFalse(exception);

        assertEquals(solver.getLocalForces(femElements[0]).get(1, 0), -10000, 1e-5);
        assertEquals(solver.getLocalForces(femElements[0]).get(2, 0), -50000, 1e-5);

        assertEquals(solver.getGlobalDeformationPoint(femPoints[0]).getX(), 0.0000, 1e-4);
        assertEquals(solver.getGlobalDeformationPoint(femPoints[1]).getY(), 0.0249, 1e-4);
    }


    @Test
    public void testBendingWithIntermediantPoints() {
        // E = 2e11
        // J = 1e-5
        // A = 1
        // P = 1e4
        // l = 5
        // Vmax = P*l^3/(3*E*J) = 0.2083333

        int amount = 4;

        FemPoint[] femPoints = new FemPoint[amount];
        for (int i = 0; i < amount; i++) {
            femPoints[i] = new FemPoint(i, i * 5. / (amount - 1), 0);
        }

        FemElement[] femElements = new FemElement[amount - 1];
        for (int i = 0; i < femElements.length; i++) {
            femElements[i] = new FemBeam2d(2e11, 1, 1e-5, new FemPoint[]{femPoints[i], femPoints[i + 1]});
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
        boolean exception = false;
        StrengthSolver solver = null;
        try {
            solver = new StrengthSolver(femPoints, femElements, forces, supports);
        } catch (Exception e) {
            e.printStackTrace();
            exception = true;
        }

        assertFalse(exception);

        assertEquals(solver.getLocalForces(femElements[0]).get(0, 0), 0, 1e-5);
        assertEquals(solver.getLocalForces(femElements[0]).get(1, 0), -1e4, 1e-5);
        assertEquals(solver.getLocalForces(femElements[0]).get(2, 0), -5e4, 1e-5);

        assertEquals(solver.getGlobalDeformationPoint(femPoints[0]).getX(), 0.0000, 1e-4);
        assertEquals(solver.getGlobalDeformationPoint(femPoints[amount - 1]).getY(), 0.2083333, 1e-4);
    }

    @Test
    public void testBookPage535() {
        double L = 1;
        FemPoint[] femPoints = new FemPoint[2];
        femPoints[0] = new FemPoint(0, 0.0, 0);
        femPoints[1] = new FemPoint(1, L, 0);

        double E = 200000e6;
        double I = 78e-8;

        double elacity = E;
        double inertia = I;
        double area = 1e-13;
        double Q = 1230;

        FemElement[] femElements = new FemElement[1];
        femElements[0] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[0], femPoints[1]});

        Support[] supports = new Support[]{
                new Support(femPoints[0], Direction.DIRECTION_X),
                new Support(femPoints[0], Direction.DIRECTION_Y),
                new Support(femPoints[0], Direction.ROTATE),
        };

        Force[] forces = new Force[]{
                new Force(femPoints[1], Direction.DIRECTION_Y, Q)
        };
        //=========================//
        boolean exception = false;
        StrengthSolver solver = null;
        try {
            solver = new StrengthSolver(femPoints, femElements, forces, supports);
        } catch (Exception e) {
            e.printStackTrace();
            exception = true;
        }

        assertFalse(exception);

        double deflection = Q * Math.pow(L, 3.) / (3 * E * I);

        assertEquals(solver.getGlobalDeformationPoint(femPoints[1]).getX(), 0.0000, 1e-4);
        assertEquals(solver.getGlobalDeformationPoint(femPoints[1]).getY(), deflection, 1e-4);
    }
}