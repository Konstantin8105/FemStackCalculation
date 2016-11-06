package home.finiteElements;

import Jama.Matrix;
import home.finiteElements.interfaces.FemElement;
import home.other.FemPoint;

public class FemBeam2d extends FemElement {
    private final double elacity;
    private final double area;
    private final double momentInertia;

    //TODO: RELEASE 2: Add pin

    public FemBeam2d(double elacity, double area, double momentInertia, FemPoint[] point) {
        super(point);
        this.elacity = elacity;
        this.area = area;
        this.momentInertia = momentInertia;
    }

    @Override
    public Matrix getTr() {
        double lambda_xx = (point[1].getX() - point[0].getX()) / getLength();
        double lambda_xy = (point[1].getY() - point[0].getY()) / getLength();
        return new Matrix(new double[][]{
                {+lambda_xx, -lambda_xy, 0, 0, 0, 0},
                {+lambda_xy, +lambda_xx, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0},
                {0, 0, 0, +lambda_xx, -lambda_xy, 0},
                {0, 0, 0, +lambda_xy, +lambda_xx, 0},
                {0, 0, 0, 0, 0, 1}
        });
    }

    @Override
    public Matrix getStiffenerMatrix() {
        double[][] stiffener = new double[6][6];
        double l = getLength();
        double EFL = elacity * area / getLength();
        double EJ = elacity * momentInertia;
        stiffener[0][0] = stiffener[3][3] = +EFL;
        stiffener[0][3] = stiffener[3][0] = -EFL;
        stiffener[1][1] = stiffener[4][4] = 12.0 * EJ / Math.pow(l, 3.);
        stiffener[2][2] = stiffener[5][5] = 4.00 * EJ / l;
        stiffener[1][2] = stiffener[2][1] = 6.00 * EJ / Math.pow(l, 2.);
        stiffener[1][5] = stiffener[5][1] = 6.00 * EJ / Math.pow(l, 2.);
        stiffener[4][5] = stiffener[5][4] = -6.0 * EJ / Math.pow(l, 2.);
        stiffener[2][4] = stiffener[4][2] = -6.0 * EJ / Math.pow(l, 2.);
        stiffener[1][4] = stiffener[4][1] = -12. * EJ / Math.pow(l, 3.);
        stiffener[2][5] = stiffener[5][2] = 2.00 * EJ / l;
        return new Matrix(stiffener);
    }

    @Override
    public Matrix getMatrixMass() {
        double density = 78500;// N/m^3
        double[][] stiffener = new double[6][6];
        double l = getLength();
        double mu = density * area;
        double rz = density * momentInertia;
        stiffener[0][0] = stiffener[3][3] = l / 3. * mu;
        stiffener[1][1] = stiffener[4][4] = 13. * l / 35. * mu + 6. * rz / (5. * l);
        stiffener[2][2] = stiffener[5][5] = l * l * l / 105. * mu + 2. * l * rz / 15.;
        stiffener[1][2] = stiffener[2][1] = 11. * l * l / 210. * mu + rz / 10.;
        stiffener[4][5] = stiffener[5][4] = -11. * l * l / 210. * mu - rz / 10.;
        stiffener[0][3] = stiffener[3][0] = l / 6. * mu;
        stiffener[1][4] = stiffener[4][1] = 9. * l / 70. * mu - 6. * rz / (5. * l);
        stiffener[2][4] = stiffener[4][2] = 13. * l * l / 420. * mu - rz / 10.;
        stiffener[1][5] = stiffener[5][1] = -13. * l * l / 420. * mu + rz / 10.;
        stiffener[2][5] = stiffener[5][2] = -l * l * l / 140. * mu - rz * l / 30.;
        return new Matrix(stiffener);
    }
}
