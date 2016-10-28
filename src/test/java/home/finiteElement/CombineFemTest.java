package home.finiteElement;

import home.other.Direction;
import home.other.FemPoint;
import home.other.Force;
import home.other.Support;
import home.solver.StrengthSolver;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class CombineFemTest {

    @Test
    public void testStructure() {

        FemPoint[] femPoints = new FemPoint[]{
                new FemPoint(0, 0, 2),
                new FemPoint(1, 2, 2),
                new FemPoint(2, 2, 0),
                new FemPoint(3, 0, 0),
        };

        FemElement[] lines = new FemElement[]{
                new FemBeam2d(2.05e11, 0.00785, 4.908e-6, new FemPoint[]{femPoints[0], femPoints[1]}),
                new FemBeam2d(2.05e11, 0.00785, 4.908e-6, new FemPoint[]{femPoints[1], femPoints[2]}),
                new FemBeam2d(2.05e11, 0.00785, 4.908e-6, new FemPoint[]{femPoints[2], femPoints[3]}),
                new FemTruss2d(2.05e11, 0.003141, new FemPoint[]{femPoints[3], femPoints[1]}),
                new FemTruss2d(2.05e11, 0.003141, new FemPoint[]{femPoints[0], femPoints[2]}),
        };

        Force[] forces = new Force[]{
                new Force(femPoints[1], Direction.DIRECTION_Y, -2500),
                new Force(femPoints[1], Direction.ROTATE, 7500),
        };

        Support[] supports = new Support[]{
                new Support(femPoints[0], Direction.DIRECTION_X),
                new Support(femPoints[0], Direction.DIRECTION_Y),

                new Support(femPoints[3], Direction.DIRECTION_X),
        };

        //=========================//
        boolean exception = false;
        try {
            StrengthSolver.calculate(femPoints, lines, forces, supports);
        } catch (Exception e) {
            e.printStackTrace();
            exception = true;
        }

        assertFalse(exception);

        assertEquals(femPoints[0].getGlobalDisplacement()[0], 0.0000, 1e-4);
        assertEquals(femPoints[0].getGlobalDisplacement()[1], 0.0000, 1e-4);

        assertEquals(femPoints[1].getGlobalDisplacement()[0], -0.002e-3, 1e-4);
        assertEquals(femPoints[1].getGlobalDisplacement()[1], -0.063e-3, 1e-4);

        assertEquals(femPoints[2].getGlobalDisplacement()[0], 0.002e-3, 1e-4);
        assertEquals(femPoints[2].getGlobalDisplacement()[1], -0.062e-3, 1e-4);

        assertEquals(femPoints[3].getGlobalDisplacement()[1], -0.022e-3, 1e-4);
    }
}
