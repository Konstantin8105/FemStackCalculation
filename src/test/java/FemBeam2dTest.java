import home.finiteElement.FemBeam2d;
import home.finiteElement.FemElement;
import home.other.Direction;
import home.other.FemPoint;
import home.other.Force;
import home.other.Support;
import home.solver.Fem;
import junit.framework.Assert;
import org.junit.Test;

public class FemBeam2dTest {
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
            Fem.calculate(femPoints, lines, forces, supports);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertEquals(lines[0].getInternalForce().get(1, 0), -10000, 1e-5);
        Assert.assertEquals(lines[0].getInternalForce().get(2, 0), -50000, 1e-5);
        Assert.assertEquals(lines[0].getDisplacementInGlobalSystem().get(4, 0), 0.0249, 1e-4);
        Assert.assertEquals(lines[0].getDisplacementInLocalSystem().get(4, 0), 0.0249, 1e-4);

        Assert.assertEquals(femPoints[0].getGlobalDisplacement()[0], 0.0000, 1e-4);
        Assert.assertEquals(femPoints[1].getGlobalDisplacement()[1], 0.0249, 1e-4);
    }

    @Test
    public void testBuckling() {

        FemPoint[] femPoints = new FemPoint[]{
                new FemPoint(0, 0, 0),
                new FemPoint(1, 5, 0),
        };

        FemElement[] lines = new FemElement[]{
                new FemBeam2d(2.05e11, 0.0314159, 7.85398e-5, new FemPoint[]{femPoints[0], femPoints[1]}),
        };

        Force[] forces = new Force[]{
                new Force(femPoints[1], Direction.DIRECTION_X, -7000),
                new Force(femPoints[1], Direction.DIRECTION_Y, 25000),
        };

        Support[] supports = new Support[]{
                new Support(femPoints[0], Direction.DIRECTION_X),
                new Support(femPoints[0], Direction.DIRECTION_Y),
                new Support(femPoints[0], Direction.ROTATE),
        };

        //=========================//
        try {
            Fem.calculate(femPoints, lines, forces, supports, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertEquals(lines[0].getInternalForce().get(0, 0), 7000, 1e-5);
        Assert.assertEquals(lines[0].getInternalForce().get(1, 0), -25000, 1e-5);
        Assert.assertEquals(lines[0].getInternalForce().get(2, 0), -25000 * 5, 1e-5);
        Assert.assertEquals(lines[0].getDisplacementInGlobalSystem().get(4, 0), 0.064754, 1e-4);
        Assert.assertEquals(lines[0].getDisplacementInLocalSystem().get(4, 0), 0.064754, 1e-4);

        Assert.assertEquals(femPoints[0].getGlobalDisplacement()[0], 0.0000, 1e-4);
        Assert.assertEquals(femPoints[1].getGlobalDisplacement()[0], -5e-6, 1e-6);
        Assert.assertEquals(femPoints[1].getGlobalDisplacement()[1], 0.064754, 1e-4);

        // Buckling factor = 228.569
    }

    @Test
    public void testBend() {
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
            Fem.calculate(femPoints, lines, forces, supports);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertEquals(lines[0].getInternalForce().get(0, 0), 0, 1e-5);
        Assert.assertEquals(lines[0].getInternalForce().get(1, 0), -1e4, 1e-5);
        Assert.assertEquals(lines[0].getInternalForce().get(2, 0), -5e4, 1e-5);

        Assert.assertEquals(femPoints[0].getGlobalDisplacement()[0], 0.0000, 1e-4);
        Assert.assertEquals(femPoints[amount - 1].getGlobalDisplacement()[1], 0.2083333, 1e-4);
    }

    @Test
    public void testBuckling5() {
        // E = 2e11
        // J = 1e-5
        // A = 1
        // P = 1e4
        // l = 5
        // Pcr = -->3.14^2/(5^2)*2e11*1e-5 = 788768 N.
        //

        int amount = 6;

        FemPoint[] femPoints = new FemPoint[amount];
        for (int i = 0; i < amount; i++) {
            femPoints[i] = new FemPoint(i, i * 5. / (amount - 1), 0);
        }

        FemElement[] lines = new FemElement[amount - 1];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = new FemBeam2d(2e11, 1, 1e-5, new FemPoint[]{femPoints[i], femPoints[i + 1]});
        }

        Force[] forces = new Force[]{
                new Force(femPoints[amount - 2], Direction.DIRECTION_X, -7000),
                //new Force(femPoints[amount-1], Direction.DIRECTION_Y, -10),//just for fun
        };

        Support[] supports = new Support[]{
                new Support(femPoints[0], Direction.DIRECTION_X),
                new Support(femPoints[0], Direction.DIRECTION_Y),
                new Support(femPoints[amount - 1], Direction.DIRECTION_Y),
        };

        //=========================//
        try {
            Fem.calculate(femPoints, lines, forces, supports, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertEquals(lines[0].getInternalForce().get(0, 0), 7000, 1e-5);
        Assert.assertEquals(lines[0].getBucklingAxialLoad(), 788768, 1e-5);//critical
    }

    @Test
    public void testBucklingPlaneFrame() {

        FemPoint[] femPoints = new FemPoint[9];
        femPoints[0] = new FemPoint(0, 0.0, 0.4);
        femPoints[1] = new FemPoint(1, 0.2, 0.4);
        femPoints[2] = new FemPoint(2, 0.4, 0.4);
        femPoints[3] = new FemPoint(3, 0.4, 0.2);
        femPoints[4] = new FemPoint(4, 0.4, 0.0);
        femPoints[5] = new FemPoint(5, 0.6, 0.4);
        femPoints[6] = new FemPoint(6, 0.8, 0.4);
        femPoints[7] = new FemPoint(7, 0.8, 0.2);
        femPoints[8] = new FemPoint(8, 0.8, 0.0);

        FemElement[] femElements = new FemElement[8];
        femElements[0] = new FemBeam2d(2e11, 24e-4, 32e-8, new FemPoint[]{femPoints[0], femPoints[1]});
        femElements[1] = new FemBeam2d(2e11, 24e-4, 32e-8, new FemPoint[]{femPoints[1], femPoints[2]});
        femElements[2] = new FemBeam2d(2e11, 24e-4, 32e-8, new FemPoint[]{femPoints[2], femPoints[3]});
        femElements[3] = new FemBeam2d(2e11, 24e-4, 32e-8, new FemPoint[]{femPoints[3], femPoints[4]});
        femElements[4] = new FemBeam2d(2e11, 48e-4, 64e-8, new FemPoint[]{femPoints[2], femPoints[5]});
        femElements[5] = new FemBeam2d(2e11, 48e-4, 64e-8, new FemPoint[]{femPoints[5], femPoints[6]});
        femElements[6] = new FemBeam2d(2e11, 24e-4, 32e-8, new FemPoint[]{femPoints[6], femPoints[7]});
        femElements[7] = new FemBeam2d(2e11, 24e-4, 32e-8, new FemPoint[]{femPoints[7], femPoints[8]});

        Force[] forces = new Force[]{
                new Force(femPoints[2], Direction.DIRECTION_Y, -8000),
                new Force(femPoints[7], Direction.DIRECTION_Y, -10000),
        };

        Support[] supports = new Support[]{
                new Support(femPoints[0], Direction.DIRECTION_X),
                new Support(femPoints[0], Direction.DIRECTION_Y),
                new Support(femPoints[4], Direction.DIRECTION_X),
                new Support(femPoints[4], Direction.DIRECTION_Y),
                new Support(femPoints[4], Direction.ROTATE),
                new Support(femPoints[8], Direction.DIRECTION_X),
                new Support(femPoints[8], Direction.DIRECTION_Y),
                new Support(femPoints[8], Direction.ROTATE),
        };

        //=========================//
        try {
            Fem.calculate(femPoints, femElements, forces, supports, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertEquals(femElements[0].getBucklingAxialLoad(), 8860, 1e-5);
    }
}
