import home.other.Direction;
import home.solver.Fem;
import home.finiteElement.FemBeam2d;
import home.finiteElement.FemElement;
import home.other.FemPoint;
import home.other.Force;
import home.other.Support;
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
        Assert.assertEquals(lines[0].getInternalForce().get(2, 0), -25000*5, 1e-5);
        Assert.assertEquals(lines[0].getDisplacementInGlobalSystem().get(4, 0), 0.064754, 1e-4);
        Assert.assertEquals(lines[0].getDisplacementInLocalSystem().get(4, 0), 0.064754, 1e-4);

        Assert.assertEquals(femPoints[0].getGlobalDisplacement()[0], 0.0000, 1e-4);
        Assert.assertEquals(femPoints[1].getGlobalDisplacement()[0], -5e-6, 1e-6);
        Assert.assertEquals(femPoints[1].getGlobalDisplacement()[1], 0.064754, 1e-4);

        // Buckling factor = 228.569
    }

    @Test
    public void testBuckling5() {

        FemPoint[] femPoints = new FemPoint[]{
                new FemPoint(0, 0, 0),
                new FemPoint(1, 1, 0),
                new FemPoint(2, 2, 0),
                new FemPoint(3, 3, 0),
                new FemPoint(4, 4, 0),
                new FemPoint(5, 5, 0),
        };

        FemElement[] lines = new FemElement[]{
                new FemBeam2d(2.05e11, 0.0314159, 7.85398e-5, new FemPoint[]{femPoints[0], femPoints[1]}),
                new FemBeam2d(2.05e11, 0.0314159, 7.85398e-5, new FemPoint[]{femPoints[1], femPoints[2]}),
                new FemBeam2d(2.05e11, 0.0314159, 7.85398e-5, new FemPoint[]{femPoints[2], femPoints[3]}),
                new FemBeam2d(2.05e11, 0.0314159, 7.85398e-5, new FemPoint[]{femPoints[3], femPoints[4]}),
                new FemBeam2d(2.05e11, 0.0314159, 7.85398e-5, new FemPoint[]{femPoints[4], femPoints[5]}),
        };

        Force[] forces = new Force[]{
                new Force(femPoints[5], Direction.DIRECTION_X, -7000),
                new Force(femPoints[5], Direction.DIRECTION_Y, 25000),
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
        Assert.assertEquals(lines[0].getInternalForce().get(2, 0), -25000*5, 1e-5);
        Assert.assertEquals(lines[4].getDisplacementInGlobalSystem().get(4, 0), 0.064754, 1e-4);
        Assert.assertEquals(lines[4].getDisplacementInLocalSystem().get(4, 0), 0.064754, 1e-4);

        Assert.assertEquals(femPoints[0].getGlobalDisplacement()[0], 0.0000, 1e-4);
        Assert.assertEquals(femPoints[5].getGlobalDisplacement()[0], -5e-6, 1e-6);
        Assert.assertEquals(femPoints[5].getGlobalDisplacement()[1], 0.064754, 1e-4);

        // Buckling factor = 228.569
    }

}
