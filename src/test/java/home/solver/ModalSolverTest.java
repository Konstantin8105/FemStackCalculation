package home.solver;

import Jama.Matrix;
import home.finiteElement.FemBeam2d;
import home.finiteElement.FemBending2d;
import home.finiteElement.ModalFemElement;
import home.other.Direction;
import home.other.FemPoint;
import home.other.Force;
import home.other.Support;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ModalSolverTest {
    /*

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

            ModalFemElement[] femElements = new ModalFemElement[6];
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
            femPoints[1] = new FemPoint(1, l / 2., 0);
            femPoints[2] = new FemPoint(2, l, 0);

            double elacity = 2.05e11;
            double inertia = 4.908e-10;
            double area = 7.8539e-5;

            ModalFemElement[] femElements = new ModalFemElement[2];
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
    public void testCompareWithSTAAD() {

        double l = 1.;

        FemPoint[] femPoints = new FemPoint[5];
        femPoints[0] = new FemPoint(0, 0, 0);
        femPoints[1] = new FemPoint(1, l / 4., 0);
        femPoints[2] = new FemPoint(2, l / 2., 0);
        femPoints[3] = new FemPoint(3, l * 3. / 4., 0);
        femPoints[4] = new FemPoint(4, l, 0);

        double elacity = 2.05e11;
        double inertia = 4.9087e-6;
        double area = 0.00785398;

        ModalFemElement[] femElements = new ModalFemElement[4];
        femElements[0] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[0], femPoints[1]});
        femElements[1] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[1], femPoints[2]});
        femElements[2] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[2], femPoints[3]});
        femElements[3] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[3], femPoints[4]});

        Support[] supports = new Support[]{
                new Support(femPoints[0], Direction.DIRECTION_X),
                new Support(femPoints[0], Direction.DIRECTION_Y),
                new Support(femPoints[0], Direction.ROTATE),

                new Support(femPoints[4], Direction.DIRECTION_X),
                new Support(femPoints[4], Direction.DIRECTION_Y),
        };

        //=========================//
        Matrix[] values = null;
        try {
            values = ModalSolver.calculate(femPoints, femElements, null, supports);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Eigenvalue:");
        for (int i = 0; i < values[0].getArray().length; i++) {
            System.out.println(i
                    + " == "
                    + String.format("%.3e", values[0].getArray()[i][0])
                    + " rad.sec^-2.0"
                    + " : T = "
                    + String.format("%.3e", 2 * Math.PI / Math.sqrt(values[0].getArray()[i][0]))
                    + " sec."
                    + " : Hz = "
                    + String.format("%.3e", 1./(2 * Math.PI / Math.sqrt(values[0].getArray()[i][0])))
                    + " Hz."
            );
        }
        System.out.println("Eigenvector:");
        values[1].print(10, 6);

        // by STAAD: T1 = 0.032 sec/ T2 = 0.010 sec.
        assertEquals(1./(2 * Math.PI / Math.sqrt(values[0].getArray()[0][0])), 604, 1e-2);
        assertEquals(1./(2 * Math.PI / Math.sqrt(values[0].getArray()[1][0])), 1839, 1e-2);
    }

    @Test
    public void testCompareWithSTAAD2() {

        double l = 1.;

        FemPoint[] femPoints = new FemPoint[5];
        femPoints[0] = new FemPoint(0, 0, 0);
        femPoints[1] = new FemPoint(1, l / 4., 0);
        femPoints[2] = new FemPoint(2, l / 2., 0);
        femPoints[3] = new FemPoint(3, l * 3. / 4., 0);
        femPoints[4] = new FemPoint(4, l, 0);

        double elacity = 2.05e11;
        double inertia = 4.9087e-6;
        double area = 0.00785398;

        ModalFemElement[] femElements = new ModalFemElement[4];
        femElements[0] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[0], femPoints[1]});
        femElements[1] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[1], femPoints[2]});
        femElements[2] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[2], femPoints[3]});
        femElements[3] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[3], femPoints[4]});

        Support[] supports = new Support[]{
                new Support(femPoints[0], Direction.DIRECTION_X),
                new Support(femPoints[0], Direction.DIRECTION_Y),
                new Support(femPoints[0], Direction.ROTATE),

                new Support(femPoints[4], Direction.DIRECTION_X),
                new Support(femPoints[4], Direction.DIRECTION_Y),
        };

        Force[] forces = new Force[]{
                new Force(femPoints[2], Direction.DIRECTION_X, 1000),
                new Force(femPoints[2], Direction.DIRECTION_Y, 1000),
        };

        //=========================//
        Matrix[] values = null;
        try {
            values = ModalSolver.calculate(femPoints, femElements, forces, supports);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Eigenvalue:");
        for (int i = 0; i < values[0].getArray().length; i++) {
            System.out.println(i
                    + " == "
                    + String.format("%.3e", values[0].getArray()[i][0])
                    + " rad.sec^-2.0"
                    + " : T = "
                    + String.format("%.3e", 2 * Math.PI / Math.sqrt(values[0].getArray()[i][0]))
                    + " sec."
                    + " : Hz = "
                    + String.format("%.3e", 1./(2 * Math.PI / Math.sqrt(values[0].getArray()[i][0])))
                    + " Hz."
            );
        }
        System.out.println("Eigenvector:");
        values[1].print(10, 6);

        // by STAAD: T1 = 0.032 sec/ T2 = 0.010 sec.
        assertEquals(1./(2 * Math.PI / Math.sqrt(values[0].getArray()[0][0])), 142.9, 1e-2);
        assertEquals(1./(2 * Math.PI / Math.sqrt(values[0].getArray()[1][0])), 902, 1e-2);
    }

    @Test
    public void testBucklingPlaneFrameHz5() {

        double l = 1.;

        FemPoint[] femPoints = new FemPoint[5];
        femPoints[0] = new FemPoint(0, 0, 0);
        femPoints[1] = new FemPoint(1, l / 4., 0);
        femPoints[2] = new FemPoint(2, l / 2., 0);
        femPoints[3] = new FemPoint(3, l * 3. / 4, 0);
        femPoints[4] = new FemPoint(4, l, 0);

        double elacity = 2.05e11;
        double inertia = 4.9087e-10;
        double area = 7.8539e-5;

        ModalFemElement[] femElements = new ModalFemElement[4];
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

*/

    @Test
    public void alladinTestModal() {
        // https://www.isr.umd.edu/~austin/aladdin.d/matrix-appl-cantilever.html
        /* [a] : Define section/material properties */

        double E = 200000e6;
        double I = 15.5E+6 * 1e-12;
        double L = 4;

        //todo different at 10 times kg --> N
        double mbar = 31.6;// * 10;

        /* [b] : Define (4x4) stiffness matrix for beam element */

        double[][] stiff = new double[4][4];
        stiff[0][0] = 4 * E * I / L;
        stiff[0][1] = 6 * E * I / (L * L);
        stiff[0][2] = 2 * E * I / L;
        stiff[0][3] = -6 * E * I / (L * L);

        stiff[1][0] = 6 * E * I / (L * L);
        stiff[1][1] = 12 * E * I / (L * L * L);
        stiff[1][2] = 6 * E * I / (L * L);
        stiff[1][3] = -12 * E * I / (L * L * L);

        stiff[2][0] = 2 * E * I / L;
        stiff[2][1] = 6 * E * I / (L * L);
        stiff[2][2] = 4 * E * I / L;
        stiff[2][3] = -6 * E * I / (L * L);

        stiff[3][0] = -6 * E * I / (L * L);
        stiff[3][1] = -12 * E * I / (L * L * L);
        stiff[3][2] = -6 * E * I / (L * L);
        stiff[3][3] = 12 * E * I / (L * L * L);

        Matrix STIFF = new Matrix(stiff);
        System.out.println("STIFF");
        STIFF.print(10, 1);

        /* [c] : Define (4x4) consistent mass matrix for beam element */

        double[][] mass = new double[4][4];

        mass[0][0] = (mbar * L / 420) * 4 * L * L;
        mass[1][0] = (mbar * L / 420) * 22 * L;
        mass[2][0] = (mbar * L / 420) * -3 * L * L;
        mass[3][0] = (mbar * L / 420) * 13 * L;

        mass[0][1] = (mbar * L / 420) * 22 * L;
        mass[1][1] = (mbar * L / 420) * 156;
        mass[2][1] = (mbar * L / 420) * -13 * L;
        mass[3][1] = (mbar * L / 420) * 54;

        mass[0][2] = (mbar * L / 420) * -3 * L * L;
        mass[1][2] = (mbar * L / 420) * -13 * L;
        mass[2][2] = (mbar * L / 420) * 4 * L * L;
        mass[3][2] = (mbar * L / 420) * -22 * L;

        mass[0][3] = (mbar * L / 420) * 13 * L;
        mass[1][3] = (mbar * L / 420) * 54;
        mass[2][3] = (mbar * L / 420) * -22 * L;
        mass[3][3] = (mbar * L / 420) * 156;

        Matrix MASS = new Matrix(mass);
        System.out.println("MASS");
        MASS.print(10, 1);

        /* [d] : Destination Array beam element connectivity */

        int[][] LD = new int[][]{
                {-1, -1, 1, 2},
                {1, 2, 3, 4},
                {3, 4, 5, 6},
                {5, 6, 7, 8},
                {7, 8, 9, -1},
        };

        /* [e] : Allocate memory for global mass and stiffness matrices */

        Matrix GMASS = new Matrix(9, 9);

        Matrix GSTIFF = new Matrix(9, 9);

        /* [f] : Assemble Global Stiffness/Mass Matrices for Two Element Cantilever */

        int no_elements = 5;
        for (int i = 0; i < no_elements; i++) {
            for (int j = 0; j < 4; j = j + 1) {
                int row = LD[i][j];
                if (row > -1) {
                    row -= 1;
                    for (int k = 0; k < 4; k = k + 1) {
                        int col = LD[i][k];
                        if (col > -1) {
                            col -= 1;
                            GMASS.getArray()[row][col] = GMASS.getArray()[row][col] + mass[j][k];
                            GSTIFF.getArray()[row][col] = GSTIFF.getArray()[row][col] + stiff[j][k];
                        }
                    }
                }
            }
        }
        System.out.println("Global STIFF");
        GSTIFF.print(10, 1);

        System.out.println("Global MASS");
        GMASS.print(10, 1);


        /* [g] : Compute and Print Eigenvalues and Eigenvectors */

        Matrix[] values = Solver.calculateEigen(GSTIFF, GMASS);

        System.out.println("Eigenvalue:");
        for (int i = 0; i < values[0].getArray().length; i++) {
            System.out.println(i
                    + " == "
                    + String.format("%.3e", values[0].getArray()[i][0])
                    + " rad.sec^-2.0"
                    + " : T = "
                    + String.format("%.3e", 2 * Math.PI / Math.sqrt(values[0].getArray()[i][0]))
                    + " sec."
            );
        }
        System.out.println("Eigenvector:");
        values[1].print(10, 6);

        assertEquals(values[0].getArray()[0][0], 145.8, 1e-1);
        assertEquals(values[0].getArray()[1][0], 1539., 1);

        // by STAAD: T1 = 0.519 sec/ T2 = 0.161 sec.
        assertEquals(2 * Math.PI / Math.sqrt(values[0].getArray()[0][0]), 0.519, 1e-2);
        assertEquals(2 * Math.PI / Math.sqrt(values[0].getArray()[1][0]), 0.161, 1e-2);

//        assertEquals(values[1].getArray()[5][0], 1, 1e-2);
//        assertEquals(values[1].getArray()[7][1], 1, 1e-2);
    }


    @Test
    public void testAlladin() {
        FemPoint[] femPoints = new FemPoint[6];
        femPoints[0] = new FemPoint(0, 0.0, 0);
        femPoints[1] = new FemPoint(1, 4.0, 0);
        femPoints[2] = new FemPoint(2, 8.0, 0);
        femPoints[3] = new FemPoint(3, 12., 0);
        femPoints[4] = new FemPoint(4, 16., 0);
        femPoints[5] = new FemPoint(4, 20., 0);


        double E = 200000e6;
        double I = 15.5E+6 * 1e-12;
//        double L = 4;
        double mbar = 31.6 * 9.81;


        double elacity = E;
        double inertia = I;
        double area = mbar / 78500;


        ModalFemElement[] femElements = new ModalFemElement[5];
        femElements[0] = new FemBending2d(elacity, area, inertia, new FemPoint[]{femPoints[0], femPoints[1]});
        femElements[1] = new FemBending2d(elacity, area, inertia, new FemPoint[]{femPoints[1], femPoints[2]});
        femElements[2] = new FemBending2d(elacity, area, inertia, new FemPoint[]{femPoints[2], femPoints[3]});
        femElements[3] = new FemBending2d(elacity, area, inertia, new FemPoint[]{femPoints[3], femPoints[4]});
        femElements[4] = new FemBending2d(elacity, area, inertia, new FemPoint[]{femPoints[4], femPoints[5]});

        Support[] supports = new Support[]{
                //new Support(femPoints[0], Direction.DIRECTION_X),
                new Support(femPoints[0], Direction.DIRECTION_Y),
                new Support(femPoints[0], Direction.ROTATE),

                new Support(femPoints[5], Direction.DIRECTION_Y),
        };

        //=========================//
        Matrix[] values = null;
        boolean exception = false;
        try {
            values = ModalSolver.calculate(femPoints, femElements, null, supports);
        } catch (Exception e) {
            e.printStackTrace();
            exception = true;
        }

        assertFalse(exception);

        System.out.println("Eigenvalue:");
        for (int i = 0; i < values[0].getArray().length; i++) {
            System.out.println(i
                    + " == "
                    + String.format("%.3e", values[0].getArray()[i][0])
                    + " rad.sec^-2.0"
                    + " : T = "
                    + String.format("%.3e", 2 * Math.PI / Math.sqrt(values[0].getArray()[i][0]))
                    + " sec."
            );
        }
        System.out.println("Eigenvector:");
        values[1].print(10, 6);

        assertEquals(values[0].getArray()[0][0], 145.8, 1e-1);
        assertEquals(values[0].getArray()[1][0], 1539., 1);

        // by STAAD: T1 = 0.519 sec/ T2 = 0.161 sec.
        assertEquals(2 * Math.PI / Math.sqrt(values[0].getArray()[0][0]), 0.519, 1e-2);
        assertEquals(2 * Math.PI / Math.sqrt(values[0].getArray()[1][0]), 0.161, 1e-2);

//        assertEquals(values[1].getArray()[5][0], 1, 1e-2);
//        assertEquals(values[1].getArray()[7][1], 1, 1e-2);
    }


    @Test
    public void testAlladin2() {
        FemPoint[] femPoints = new FemPoint[6];
        femPoints[0] = new FemPoint(0, 0.0, 0);
        femPoints[1] = new FemPoint(1, 4.0, 0);
        femPoints[2] = new FemPoint(2, 8.0, 0);
        femPoints[3] = new FemPoint(3, 12., 0);
        femPoints[4] = new FemPoint(4, 16., 0);
        femPoints[5] = new FemPoint(4, 20., 0);


        double E = 200000e6;
        double I = 15.5E+6 * 1e-12;
//        double L = 4;
        double mbar = 31.6 * 9.81;


        double elacity = E;
        double inertia = I;
        double area = mbar / 78500;

        ModalFemElement[] femElements = new ModalFemElement[5];
        femElements[0] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[0], femPoints[1]});
        femElements[1] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[1], femPoints[2]});
        femElements[2] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[2], femPoints[3]});
        femElements[3] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[3], femPoints[4]});
        femElements[4] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[4], femPoints[5]});

        Support[] supports = new Support[]{
                new Support(femPoints[0], Direction.DIRECTION_X),
                new Support(femPoints[0], Direction.DIRECTION_Y),
                new Support(femPoints[0], Direction.ROTATE),

                new Support(femPoints[5], Direction.DIRECTION_X),
                new Support(femPoints[5], Direction.DIRECTION_Y),
        };

        //=========================//
        boolean exception = false;
        Matrix[] values = null;
        try {
            values = ModalSolver.calculate(femPoints, femElements, null, supports);
        } catch (Exception e) {
            e.printStackTrace();
            exception = true;
        }

        assertFalse(exception);

        System.out.println("Eigenvalue:");
        for (int i = 0; i < values[0].getArray().length; i++) {
            System.out.println(i
                    + " == "
                    + String.format("%.3e", values[0].getArray()[i][0])
                    + " rad.sec^-2.0"
                    + " : T = "
                    + String.format("%.3e", 2 * Math.PI / Math.sqrt(values[0].getArray()[i][0]))
                    + " sec."
            );
        }
        System.out.println("Eigenvector:");
        values[1].print(10, 6);

        assertEquals(values[0].getArray()[0][0], 145.8, 1);
        assertEquals(values[0].getArray()[1][0], 1539., 10);

        // by STAAD: T1 = 0.519 sec/ T2 = 0.161 sec.
        assertEquals(2 * Math.PI / Math.sqrt(values[0].getArray()[0][0]), 0.519, 1e-2);
        assertEquals(2 * Math.PI / Math.sqrt(values[0].getArray()[1][0]), 0.161, 1e-2);

//        assertEquals(values[1].getArray()[5][0], 1, 1e-2);
//        assertEquals(values[1].getArray()[7][1], 1, 1e-2);
    }


    @Test
    public void testBookPage535_Npoints() {
        // http://www.soprotmat.ru/dinamika3.htm
        // TY = 0.1028431 sec
        // TX = 0.005 sec
        double L = 1;
        double E = 2e11;
        double I = 78e-8;

        double elacity = E;
        double inertia = I;
        double area = 1e-5;
        double Q = 1230;

        List<Double> w2 = new ArrayList<>();

        for (int i = 2; i <= 4; i += 1) {
            FemPoint[] femPoints = new FemPoint[i];
            for (int j = 0; j < i; j++) {
                femPoints[j] = new FemPoint(j, L * (j) / (double) (i - 1), 0);
            }

            ModalFemElement[] femElements = new ModalFemElement[i - 1];
            for (int j = 0; j < femElements.length; j++) {
                femElements[j] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[j], femPoints[j + 1]});
            }
            Support[] supports = new Support[]{
                    new Support(femPoints[0], Direction.DIRECTION_X),
                    new Support(femPoints[0], Direction.DIRECTION_Y),
                    new Support(femPoints[0], Direction.ROTATE),
            };

            Force[] forces = new Force[]{
                    new Force(femPoints[i - 1], Direction.DIRECTION_Y, Q)// TODO: 01.11.2016 strange 9.8
            };
            //=========================//
            Matrix[] values;
            boolean exception = false;
            try {
                values = ModalSolver.calculate(femPoints, femElements, forces, supports);
                w2.add(values[0].getArray()[0][0]);

            } catch (Exception e) {
                e.printStackTrace();
                exception = true;
            }

            assertFalse(exception);

        }

        // in according to site
        // TY = 0,1 sec
        // TX = 0.005 sec
        // in according to STAAD:
        // T = 0.102 sec.
        // f = 9.832 Hz.
        double T = 0.1028431;
        assertEquals(w2.get(w2.size() - 1), Math.pow(2. * Math.PI / T, 2.), 20);
        double T2 = 2. * Math.PI / Math.sqrt(w2.get(w2.size() - 1));
        assertEquals(T2, T, 1e-3);
    }


    @Test
    public void testSelfWeight() {
        double L = 1.;
        double elacity = 2.05e11;
        double inertia = 1.08e-6;
        double area = 0.0036;

        double w_lineForce = -1;

        for (int amountPoints = 2; amountPoints < 5; amountPoints++) {
            FemPoint[] femPoints = new FemPoint[amountPoints];
            for (int j = 0; j < amountPoints; j++) {
                femPoints[j] = new FemPoint(j, L * (double) (j) / (double) (amountPoints - 1), 0);
            }

            ModalFemElement[] femElements = new ModalFemElement[amountPoints - 1];
            for (int j = 0; j < femElements.length; j++) {
                femElements[j] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[j], femPoints[j + 1]});
            }
            Support[] supports = new Support[]{
                    new Support(femPoints[0], Direction.DIRECTION_X),
                    new Support(femPoints[0], Direction.DIRECTION_Y),
                    new Support(femPoints[0], Direction.ROTATE),
            };

            //=========================//
            boolean exception = false;
            Matrix[] values;
            try {
                values = ModalSolver.calculate(femPoints, femElements, null, supports);
                w_lineForce = values[0].getArray()[0][0];
                System.out.println(amountPoints + " : W^2 with point force = " + w_lineForce);
            } catch (Exception e) {
                e.printStackTrace();
                exception = true;
            }
            assertFalse(exception);
        }

        double w_pointsForces = -1;
        for (int amountPoints = 10; amountPoints < 150; amountPoints += 20) {
            FemPoint[] femPoints = new FemPoint[amountPoints];
            for (int j = 0; j < amountPoints; j++) {
                femPoints[j] = new FemPoint(j, L * (double) (j) / (double) (amountPoints - 1), 0);
            }

            ModalFemElement[] femElements = new ModalFemElement[amountPoints - 1];
            for (int j = 0; j < femElements.length; j++) {
                femElements[j] = new FemBeam2d(elacity, area * 1e-6, inertia, new FemPoint[]{femPoints[j], femPoints[j + 1]});
            }

            Support[] supports = new Support[]{
                    new Support(femPoints[0], Direction.DIRECTION_X),
                    new Support(femPoints[0], Direction.DIRECTION_Y),
                    new Support(femPoints[0], Direction.ROTATE),
            };

            Force[] forces = new Force[amountPoints - 1];
            for (int i = 0; i < forces.length; i++) {
                forces[i] = new Force(femPoints[i + 1], Direction.DIRECTION_Y, area * L * 78500 / (amountPoints - 1));
            }
            forces[forces.length - 1] =
                    new Force(femPoints[amountPoints - 1], Direction.DIRECTION_Y, area * L * 78500 / (amountPoints - 1) / 2.);

            //=========================//
            boolean exception = false;
            Matrix[] values;
            try {
                values = ModalSolver.calculate(femPoints, femElements, forces, supports);
                w_pointsForces = values[0].getArray()[0][0];
                System.out.println(amountPoints + " : W^2 with point force = " + w_pointsForces);
            } catch (Exception e) {
                e.printStackTrace();
                exception = true;
            }
            assertFalse(exception);
        }

        assertEquals(w_lineForce, w_pointsForces, 100);

        // in according to STAAD:
        // T = 0.0204545 sec
        // f = 48.889 Hz
        double T = 0.0204545;
        double W = Math.pow(2. * Math.PI / T, 2.);
        System.out.println("Ideal W = " + W);
        assertTrue(1. - w_lineForce / W < 1e-3);
        double T2 = 2. * Math.PI / Math.sqrt(w_lineForce);
        assertEquals(T2, T, 1e-3);
    }
}