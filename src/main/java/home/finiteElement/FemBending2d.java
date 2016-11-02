package home.finiteElement;

import Jama.Matrix;
import home.finiteElement.interfaces.FemElement;
import home.finiteElement.interfaces.iModal;
import home.other.FemPoint;

public class FemBending2d extends FemElement implements iModal {
    private final double elacity;
    private final double area;
    private final double momentInertia;

    public FemBending2d(double elacity, double area, double momentInertia, FemPoint[] point) {
        super(point);
        this.elacity = elacity;
        this.area = area;
        this.momentInertia = momentInertia;
    }

    @Override
    protected int getAmountLocalAxes() {
        return 4;
    }

    @Override
    public Matrix getTr() {
        double lambda_xx = (point[1].getX() - point[0].getX()) / getLength();
        return new Matrix(new double[][]{
                {lambda_xx, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, lambda_xx, 0},
                {0, 0, 0, 1}
        });
    }

    @Override
    public Matrix getStiffenerMatrix() {
        double[][] stiffener = new double[4][4];
        double l = getLength();
        double EJ = elacity * momentInertia;
        stiffener[0][0] = stiffener[2][2] = 12.0 * EJ / Math.pow(l, 3);
        stiffener[1][1] = stiffener[3][3] = 4.00 * EJ / l;
        stiffener[0][1] = stiffener[1][0] = 6.00 * EJ / Math.pow(l, 2);
        stiffener[0][3] = stiffener[3][0] = 6.00 * EJ / Math.pow(l, 2);
        stiffener[0][2] = stiffener[2][0] = -12. * EJ / Math.pow(l, 3);
        stiffener[1][2] = stiffener[2][1] = -6.0 * EJ / Math.pow(l, 2);
        stiffener[2][3] = stiffener[3][2] = -6.0 * EJ / Math.pow(l, 2);
        stiffener[1][3] = stiffener[3][1] = 2.00 * EJ / l;
        return new Matrix(stiffener);
    }

    @Override
    public Matrix getMatrixMass() {
        double density = 78500;//N/m^3//7833.41*9.81;//76819.5;//78500;
        double[][] stiffener = new double[4][4];
        double l = getLength();
        double mu = density * area;// *l ;
        double rz = 0;//density*momentInertia;

        stiffener[0][0] = stiffener[2][2] = 13. * l / 35. * mu + 6. * rz / (5. * l);
        stiffener[1][1] = stiffener[3][3] = l * l * l / 105. * mu + 2. * l * rz / 15.;
        stiffener[0][1] = stiffener[1][0] = 11. * l * l / 210. * mu + rz / 10.;
        stiffener[3][2] = stiffener[2][3] = -11. * l * l / 210. * mu - rz / 10.;

        stiffener[2][0] = stiffener[0][2] = 9. * l / 70. * mu - 6. * rz / (5. * l);
        stiffener[2][1] = stiffener[1][2] = 13. * l * l / 420. * mu - rz / 10.;
        stiffener[3][0] = stiffener[0][3] = -13. * l * l / 420. * mu + rz / 10.;
        stiffener[3][1] = stiffener[1][3] = -l * l * l / 140. * mu - rz * l / 30.;

        return new Matrix(stiffener);
    }

    @Override
    public void setGlobalDisplacementInPoint(double[] localDisplacement) {
        point[0].setGlobalDisplacement(new double[]{0, localDisplacement[0], localDisplacement[1]});
        point[1].setGlobalDisplacement(new double[]{0, localDisplacement[2], localDisplacement[3]});
    }
}
