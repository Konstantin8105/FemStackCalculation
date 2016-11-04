import home.finiteElements.FemBeam2d;
import home.finiteElements.interfaces.FemElement;
import home.other.Direction;
import home.other.FemPoint;
import home.other.Force;
import home.other.Support;
import home.solver.StrengthSolver;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class ParametricTestPerformance {

    /*
Amount =      3 : Time =         15 msec.
Amount =     10 : Time =         12 msec.
Amount =     20 : Time =         21 msec.
Amount =     50 : Time =        125 msec.
Amount =    100 : Time =        242 msec.
Amount =    200 : Time =       1891 msec.
Amount =    400 : Time =      13230 msec.
     */

    private final int amountIntermediatePoints;

    @Parameterized.Parameters
    public static Collection<Integer> primeNumbers() {
        return Arrays.asList(
                3,
                10,
                20,
                50,
                100,
                200,
                400);
    }

    public ParametricTestPerformance(int amountIntermediatePoints) {
        this.amountIntermediatePoints = amountIntermediatePoints;
    }

    @Test
    public void test() {
        Assert.assertTrue(amountIntermediatePoints > 0);

        long start = System.currentTimeMillis();

        FemPoint[] femPoints = new FemPoint[amountIntermediatePoints];
        for (int i = 0; i < femPoints.length; i++) {
            femPoints[i] = new FemPoint(i, (double) i / (double) (amountIntermediatePoints - 1) * 5.0, 0);
        }
        assertEquals(femPoints.length, amountIntermediatePoints);

        FemElement[] femElements = new FemElement[amountIntermediatePoints - 1];
        for (int i = 0; i < femElements.length; i++) {
            femElements[i] = new FemBeam2d(2e11, 53.8e-4, 8356e-8, new FemPoint[]{femPoints[i], femPoints[i + 1]});
        }

        Support[] supports = new Support[]{
                new Support(femPoints[0], Direction.DIRECTION_X),
                new Support(femPoints[0], Direction.DIRECTION_Y),
                new Support(femPoints[0], Direction.ROTATE),
        };


        Force[] forces = new Force[]{
                new Force(femPoints[femPoints.length - 1], Direction.DIRECTION_Y, 10000),
        };

        //=========================//
        boolean exception = false;
        StrengthSolver solver = null;
        try {
            //FemModel femModel = new FemModel(femPoints, femElements, supports);
            //solver = new StrengthSolver(femModel, forces);
            solver = new StrengthSolver(femPoints, femElements, forces, supports);
        } catch (Exception e) {
            e.printStackTrace();
            exception = true;
        }

        assertFalse(exception);

        assertEquals(solver.getGlobalDeformationPoint(femPoints[0]).getX(), 0.0000, 1e-4);
        assertEquals(solver.getGlobalDeformationPoint(femPoints[femPoints.length - 1]).getY(), 0.0249, 1e-4);

        long finish = System.currentTimeMillis();

        System.out.println("Amount = " + String.format("%6d", amountIntermediatePoints)
                + " : Time = " + String.format("%10d",(finish - start)) + " msec.");
    }
}
