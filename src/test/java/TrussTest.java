import home.Direction;
import home.Fem;
import home.FemElements.FemElement;
import home.FemElements.FemTruss2d;
import home.Other.FemPoint;
import home.Other.Force;
import home.Other.Support;
import junit.framework.Assert;
import org.junit.Test;

public class TrussTest {
    @Test
    public void exampleTruss() {
        FemPoint[] femPoints = new FemPoint[]{
                new FemPoint(0, 0, 0),
                new FemPoint(1, 0, 1.2),
                new FemPoint(2, 0.4, 0),
                new FemPoint(3, 0.4, 0.6),
                new FemPoint(4, 0.8, 0),
        };

        FemElement[] lines = new FemElement[]{
                new FemTruss2d(2e11, 40e-4, new FemPoint[]{femPoints[0], femPoints[1]}),
                new FemTruss2d(2e11, 64e-4, new FemPoint[]{femPoints[0], femPoints[2]}),
                new FemTruss2d(2e11, 60e-4, new FemPoint[]{femPoints[0], femPoints[3]}),
                new FemTruss2d(2e11, 60e-4, new FemPoint[]{femPoints[1], femPoints[3]}),
                new FemTruss2d(2e11, 40e-4, new FemPoint[]{femPoints[2], femPoints[3]}),
                new FemTruss2d(2e11, 64e-4, new FemPoint[]{femPoints[2], femPoints[4]}),
                new FemTruss2d(2e11, 60e-4, new FemPoint[]{femPoints[3], femPoints[4]})
        };

        Force[] forces = new Force[]{
                new Force(femPoints[1], Direction.DIRECTION_X, -70000),
                new Force(femPoints[3], Direction.DIRECTION_X, 42000),
        };

        Support[] supports = new Support[]{
                new Support(femPoints[0], Direction.DIRECTION_X),
                new Support(femPoints[0], Direction.DIRECTION_Y),
                new Support(femPoints[2], Direction.DIRECTION_Y),
                new Support(femPoints[4], Direction.DIRECTION_Y),
        };

        //=========================//
        try {
            Fem.calculate(femPoints, lines, forces, supports);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertEquals(lines.length, 7);
        Assert.assertEquals(supports.length, 4);
        Assert.assertEquals(forces.length, 2);

        Assert.assertEquals(lines[6].getInternalForce().get(0, 0), -61594.762917, 1e-5);
        Assert.assertEquals(lines[6].getInternalForce().get(1, 0), +61594.762917, 1e-5);
    }
}
