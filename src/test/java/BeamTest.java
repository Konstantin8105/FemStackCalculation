import home.Direction;
import home.Fem;
import home.FemElements.FemBeam2d;
import home.FemElements.FemElement;
import home.Other.FemPoint;
import home.Other.Force;
import home.Other.Support;
import junit.framework.Assert;
import org.junit.Test;

public class BeamTest {
    @Test
    public void exampleBeam() {

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
        Fem.calculate(femPoints, lines, forces, supports);

        Assert.assertEquals(lines[0].getInternalForce().get(1, 0), -10000, 1e-5);
        Assert.assertEquals(lines[0].getInternalForce().get(2, 0), -50000, 1e-5);
    }
}
