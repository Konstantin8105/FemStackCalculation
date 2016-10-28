package home.finiteElement;

import home.other.Direction;
import home.other.FemPoint;
import home.other.Force;
import home.other.Support;
import home.solver.StrengthSolver;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class FemBending2dTest {

    @Test
    public void exampleBeam() {

        FemPoint[] femPoints = new FemPoint[]{
                new FemPoint(0, 0, 0),
                new FemPoint(1, 5, 0),
        };

        FemElement[] lines = new FemElement[]{
                new FemBending2d(2e11, 53.8e-4, 8356e-8, new FemPoint[]{femPoints[0], femPoints[1]}),
        };

        Force[] forces = new Force[]{
                new Force(femPoints[1], Direction.DIRECTION_Y, 10000),
        };

        Support[] supports = new Support[]{
//                new Support(femPoints[0], Direction.DIRECTION_X),
                new Support(femPoints[0], Direction.DIRECTION_Y),
                new Support(femPoints[0], Direction.ROTATE),
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
        assertEquals(femPoints[0].getGlobalDisplacement()[2], 0.0000, 1e-4);
        assertEquals(femPoints[1].getGlobalDisplacement()[1], 0.0249, 1e-4);
    }
}