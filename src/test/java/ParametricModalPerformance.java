import home.finiteElements.FemBeam2d;
import home.finiteElements.interfaces.FemElement;
import home.other.Direction;
import home.other.FemPoint;
import home.other.MassPoint;
import home.other.Support;
import home.solver.ModalSolver;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class ParametricModalPerformance {
/*
Amount =      2 : Time =         18 msec.
Amount =      3 : Time =         10 msec.
Amount =     10 : Time =         35 msec.
Amount =     20 : Time =         80 msec.
Amount =     50 : Time =        253 msec.
Amount =    100 : Time =       1296 msec.
Amount =    200 : Time =       5944 msec.

After optimize At*Mok*A
Amount =      2 : Time =         17 msec.
Amount =      3 : Time =          1 msec.
Amount =     10 : Time =         34 msec.
Amount =     20 : Time =         58 msec.
Amount =     50 : Time =        237 msec.
Amount =    100 : Time =       1054 msec.
Amount =    200 : Time =       4030 msec.


 */
    private final int amountIntermediatePoints;

    @Parameterized.Parameters
    public static Collection<Integer> primeNumbers() {
        return Arrays.asList(
                2,
                3,
                10,
                20,
                50,
                100,
                200
        );
    }

    public ParametricModalPerformance(int amountIntermediatePoints) {
        this.amountIntermediatePoints = amountIntermediatePoints;
    }

    @Test
    public void test() {
        Assert.assertTrue(amountIntermediatePoints > 0);

        long start = System.currentTimeMillis();

        double L = 1;
        double elacity = 2e11;
        double inertia = 78e-8;
        double area = 1e-5;
        double Q = 1230;

        FemPoint[] femPoints = new FemPoint[amountIntermediatePoints];
        for (int j = 0; j < amountIntermediatePoints; j++) {
            femPoints[j] = new FemPoint(j, L * (j) / (double) (amountIntermediatePoints - 1), 0);
        }

        FemElement[] femElements = new FemElement[amountIntermediatePoints - 1];
        for (int j = 0; j < femElements.length; j++) {
            femElements[j] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[j], femPoints[j + 1]});
        }
        Support[] supports = new Support[]{
                new Support(femPoints[0], Direction.DIRECTION_X),
                new Support(femPoints[0], Direction.DIRECTION_Y),
                new Support(femPoints[0], Direction.ROTATE),
        };

        MassPoint[] mass = new MassPoint[]{
                new MassPoint(femPoints[amountIntermediatePoints - 1], Q)
        };
        //=========================//
        ModalSolver solver = null;
        boolean exception = false;
        try {
            solver = new ModalSolver(femPoints, femElements, mass, supports);
        } catch (Exception e) {
            e.printStackTrace();
            exception = true;
        }

        assertFalse(exception);


        // in according to site
        // TY = 0,1 sec
        // TX = 0.005 sec
        // in according to STAAD:
        // T = 0.102 sec.
        // f = 9.832 Hz.
        double T = 0.1028431;
       // assertEquals(w2.get(w2.size() - 1), Math.pow(2. * Math.PI / T, 2.), 20);
        double T2 = solver.getModes().get(0).getPeriod();
        assertEquals(T2, T, 1e-3);


        long finish = System.currentTimeMillis();

        System.out.println("Amount = " + String.format("%6d", amountIntermediatePoints)
                + " : Time = " + String.format("%10d", (finish - start)) + " msec.");
    }
}
