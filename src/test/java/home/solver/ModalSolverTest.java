package home.solver;

import home.finiteElement.FemBeam2d;
import home.finiteElement.FemElement;
import home.other.Direction;
import home.other.FemPoint;
import home.other.Force;
import home.other.Support;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ModalSolverTest {

    @Test
    public void testBucklingPlaneFrameHz() {

        FemPoint[] femPoints = new FemPoint[7];
        femPoints[0] = new FemPoint(0, 0, 0);
        femPoints[1] = new FemPoint(1, 1, 0);
        femPoints[2] = new FemPoint(2, 2, 0);
        femPoints[3] = new FemPoint(3, 3, 0);
        femPoints[4] = new FemPoint(4, 4, 0);
        femPoints[5] = new FemPoint(5, 2, 1);
        femPoints[6] = new FemPoint(6, 2, 2);

        double elacity = 2.05e11;
        double inertia = 7.8539e-5;
        double area = 0.0279602;//0.0314159;

        FemElement[] femElements = new FemElement[6];
        femElements[0] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[0], femPoints[1]});
        femElements[1] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[1], femPoints[2]});
        femElements[2] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[2], femPoints[3]});
        femElements[3] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[3], femPoints[4]});
        femElements[4] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[6], femPoints[5]});
        femElements[5] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[5], femPoints[2]});

        Force[] forces = new Force[]{
                new Force(femPoints[1], Direction.DIRECTION_Y, 1000),
                new Force(femPoints[3], Direction.DIRECTION_Y, 1000),
        };

        Support[] supports = new Support[]{
                new Support(femPoints[0], Direction.DIRECTION_X),
                new Support(femPoints[0], Direction.DIRECTION_Y),
                //new Support(femPoints[0], Direction.ROTATE),

                new Support(femPoints[4], Direction.DIRECTION_X),
                new Support(femPoints[4], Direction.DIRECTION_Y),
                //new Support(femPoints[4], Direction.ROTATE),

                new Support(femPoints[6], Direction.DIRECTION_X),
                new Support(femPoints[6], Direction.DIRECTION_Y),
                //new Support(femPoints[6], Direction.ROTATE),
        };

        //=========================//
        try {
            ModalSolver.calculate(femPoints, femElements, forces, supports);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(femElements[2].getBucklingAxialLoad(), 93, 1e-5);
    }

    @Test
    public void testBucklingPlaneFrameHz2() {

        double l = 1.;

        FemPoint[] femPoints = new FemPoint[3];
        femPoints[0] = new FemPoint(0, 0, 0);
        femPoints[1] = new FemPoint(1, l/2., 0);
        femPoints[2] = new FemPoint(2, l, 0);

        double elacity = 2.05e11;
        double inertia = 4.908e-10;
        double area = 7.8539e-5;

        FemElement[] femElements = new FemElement[2];
        femElements[0] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[0], femPoints[1]});
        femElements[1] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[1], femPoints[2]});

        Force[] forces = new Force[]{
                new Force(femPoints[1], Direction.DIRECTION_Y, 1000),
        };

        Support[] supports = new Support[]{
                new Support(femPoints[0], Direction.DIRECTION_X),
                new Support(femPoints[0], Direction.DIRECTION_Y),

                new Support(femPoints[2], Direction.DIRECTION_Y),
        };

        //=========================//
        try {
            ModalSolver.calculate(femPoints, femElements, forces, supports);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(femElements[0].getBucklingAxialLoad(), 1.094, 1e-5);
        assertEquals(femElements[1].getBucklingAxialLoad(), 89.23, 1e-5);
        assertEquals(femElements[2].getBucklingAxialLoad(), 2304., 1e-5);
    }

    @Test
    public void testBucklingPlaneFrameHz4() {

        double l = 1.;

        FemPoint[] femPoints = new FemPoint[5];
        femPoints[0] = new FemPoint(0, 0, 0);
        femPoints[1] = new FemPoint(1, l/4., 0);
        femPoints[2] = new FemPoint(2, l/2., 0);
        femPoints[3] = new FemPoint(3, l*3./4, 0);
        femPoints[4] = new FemPoint(4, l, 0);

        double elacity = 2.05e11;
        double inertia = 4.908e-10;
        double area = 7.8539e-5;

        FemElement[] femElements = new FemElement[4];
        femElements[0] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[0], femPoints[1]});
        femElements[1] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[1], femPoints[2]});
        femElements[2] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[2], femPoints[3]});
        femElements[3] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[3], femPoints[4]});

        Force[] forces = new Force[]{
                new Force(femPoints[2], Direction.DIRECTION_Y, 1000),
        };

        Support[] supports = new Support[]{
                new Support(femPoints[0], Direction.DIRECTION_X),
                new Support(femPoints[0], Direction.DIRECTION_Y),

                new Support(femPoints[4], Direction.DIRECTION_Y),
        };

        //=========================//
        try {
            ModalSolver.calculate(femPoints, femElements, forces, supports);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(femElements[0].getBucklingAxialLoad(), 1.094, 1e-5);
        assertEquals(femElements[1].getBucklingAxialLoad(), 89.23, 1e-5);
        assertEquals(femElements[2].getBucklingAxialLoad(), 2304., 1e-5);
    }

    @Test
    public void testBucklingPlaneFrameHz5() {

        double l = 1.;

        FemPoint[] femPoints = new FemPoint[5];
        femPoints[0] = new FemPoint(0, 0, 0);
        femPoints[1] = new FemPoint(1, l/4., 0);
        femPoints[2] = new FemPoint(2, l/2., 0);
        femPoints[3] = new FemPoint(3, l*3./4, 0);
        femPoints[4] = new FemPoint(4, l, 0);

        double elacity = 2.05e11;
        double inertia = 4.9087e-10;
        double area = 7.8539e-5;

        FemElement[] femElements = new FemElement[4];
        femElements[0] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[0], femPoints[1]});
        femElements[1] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[1], femPoints[2]});
        femElements[2] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[2], femPoints[3]});
        femElements[3] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[3], femPoints[4]});

        Force[] forces = new Force[]{
                new Force(femPoints[2], Direction.DIRECTION_Y, 1000),
        };

        Support[] supports = new Support[]{
                new Support(femPoints[0], Direction.DIRECTION_X),
                new Support(femPoints[0], Direction.DIRECTION_Y),
                new Support(femPoints[0], Direction.ROTATE),

                new Support(femPoints[4], Direction.DIRECTION_X),
                new Support(femPoints[4], Direction.DIRECTION_Y),
        };

        //=========================//
        try {
            ModalSolver.calculate(femPoints, femElements, forces, supports);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(femElements[0].getBucklingAxialLoad(), 1.653, 1e-5);
        assertEquals(femElements[1].getBucklingAxialLoad(), 92.371, 1e-5);
        assertEquals(femElements[2].getBucklingAxialLoad(), 147.813, 1e-5);
    }
}