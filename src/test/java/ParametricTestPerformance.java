import home.finiteElement.FemBeam2d;
import home.finiteElement.interfaces.FemElement;
import home.other.Direction;
import home.other.FemPoint;
import home.other.Force;
import home.other.Support;
import home.solver.FemModel;
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

    private final int amountIntermediatePoints;

    @Parameterized.Parameters
    public static Collection<Integer> primeNumbers() {
        return Arrays.asList(
                10,
                20,
                50,
                100,
                200);
    }

    public ParametricTestPerformance(int amountIntermediatePoints) {
        this.amountIntermediatePoints = amountIntermediatePoints;
    }

    @Test
    public void test() {
        Assert.assertTrue(amountIntermediatePoints > 0);

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
            FemModel femModel = new FemModel(femPoints, femElements, supports);
            solver = new StrengthSolver(femModel, forces);
        } catch (Exception e) {
            e.printStackTrace();
            exception = true;
        }

        assertFalse(exception);

        assertEquals(solver.getGlobalDeformationPoint(femPoints[0])[0], 0.0000, 1e-4);
        assertEquals(solver.getGlobalDeformationPoint(femPoints[femPoints.length - 1])[1], 0.0249, 1e-4);
    }
}
