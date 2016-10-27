package home.finiteElement;

import Jama.Matrix;
import home.other.FemPoint;

public class FemBeam2d extends ModalFemElement {
    private final double elacity;
    private final double area;
    private final double momentInertia;

    @SuppressWarnings("SameParameterValue")
    public FemBeam2d(double elacity, double area, double momentInertia, FemPoint[] point) {
        super(point);
        this.elacity = elacity;
        this.area = area;
        this.momentInertia = momentInertia;
    }

    @Override
    protected int getAmountAxes() {
        return 6;
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
        stiffener[1][1] = stiffener[4][4] = 12.0 * EJ / Math.pow(l, 3);
        stiffener[2][2] = stiffener[5][5] = 4.00 * EJ / l;
        stiffener[1][2] = stiffener[2][1] = 6.00 * EJ / Math.pow(l, 2);
        stiffener[1][5] = stiffener[5][1] = 6.00 * EJ / Math.pow(l, 2);
        stiffener[4][5] = stiffener[5][4] = -6.0 * EJ / Math.pow(l, 2);
        stiffener[2][4] = stiffener[4][2] = -6.0 * EJ / Math.pow(l, 2);
        stiffener[1][4] = stiffener[4][1] = -12. * EJ / Math.pow(l, 3);
        stiffener[2][5] = stiffener[5][2] = 2.00 * EJ / l;
        return new Matrix(stiffener);
    }

//    @Override
//    protected Matrix getPotentialMatrix() {
//        double[][] stiffener = new double[6][6];
//        double l = getLength();
//        //todo only if compress
////        double axialForce = -1;
////        stiffener[1][1] = stiffener[4][4] = axialForce * (6. / 5.0 / l);
////        stiffener[2][2] = stiffener[5][5] = axialForce * (2. * l / 15.);
////        stiffener[1][2] = stiffener[2][1] = axialForce * (0.1);
////        stiffener[2][4] = stiffener[4][2] = axialForce * (-0.1);
////        stiffener[4][5] = stiffener[5][4] = axialForce * (-0.1);
////        stiffener[4][1] = stiffener[1][4] = axialForce * (-6. / 5. / l);
////        stiffener[5][2] = stiffener[2][5] = axialForce * (-l / 30.);
////        stiffener[1][5] = stiffener[5][1] = axialForce * (0.1);
//        return new Matrix(stiffener);
//    }

//
//    abstract protected Matrix getPotentialMatrix();
//
//
//    public Matrix getPotentialMatrixTr() {
//        Matrix tr = getTr();
//        Matrix gr = getPotentialMatrix();
//        return ((tr.transpose().times(gr)).times(tr));
//    }

//    @Override
//    public Matrix getStiffenerMatrix2() {
//        double[][] stiffener = new double[6][6];
//        double l = getLength();
//        double EFL = elacity * area / getLength();
//        double factor = Math.PI * Math.PI * elacity * momentInertia / (8. * l);
//        stiffener[0][0] = stiffener[3][3] = factor * (EFL);
//        stiffener[1][1] = stiffener[4][4] = factor * (1. / l / l);
//        stiffener[2][2] = stiffener[5][5] = factor * (1. / 4. + 1. / (Math.PI * Math.PI));
//        stiffener[1][2] = stiffener[2][1] = factor * (-1. / 2. / l);
//        stiffener[4][5] = stiffener[5][4] = factor * (-1. / 2. / l);
//        stiffener[2][4] = stiffener[4][2] = factor * (-1. / 2. / l);
//        stiffener[1][4] = stiffener[4][1] = factor * (1. / l / l);
//        stiffener[0][3] = stiffener[3][0] = factor * (-EFL);
//        stiffener[1][5] = stiffener[5][1] = factor * (-1. / 2. / l);
//        stiffener[2][5] = stiffener[5][2] = factor * (1. / 4. - 1. / (Math.PI * Math.PI));
//        return new Matrix(stiffener);
//    }

//    @Override
//    protected Matrix getPotentialMatrix2() {
//        double[][] stiffener = new double[6][6];
//        double l = getLength();
//        double pi2 = Math.PI * Math.PI;
//        //todo only if compress
//        double axialForce = Math.abs(getInternalForce().getArray()[0][0]) > 0 ? 1 : 0;
//        stiffener[1][1] = stiffener[4][4] = axialForce * (pi2 / 8. / l);
//        stiffener[1][4] = stiffener[4][1] = axialForce * (pi2 / 8. / l);
//        stiffener[2][2] = stiffener[5][5] = axialForce * ((pi2 / 4. - 1.) * l / 8.);
//        stiffener[2][1] = stiffener[1][2] = axialForce * (1. / 2. - pi2 / 16.);
//        stiffener[2][4] = stiffener[4][2] = axialForce * (1. / 2. - pi2 / 16.);
//        stiffener[4][5] = stiffener[5][4] = axialForce * (1. / 2. - pi2 / 16.);
//        stiffener[1][5] = stiffener[5][1] = axialForce * (1. / 2. - pi2 / 16.);
//        stiffener[2][5] = stiffener[5][2] = axialForce * ((pi2 / 4. - 3.) * l / 8.);
//        return new Matrix(stiffener);
//    }

    @Override
    protected Matrix getMatrixMass() {
//        double mu = 78500*area;//N/m^3
//        double[][] stiffener = new double[6][6];
//        double l = getLength();
//        stiffener[0][0] = stiffener[3][3] = l/3.*mu;
//        stiffener[1][1] = stiffener[4][4] = 13.*l/35.*mu;
//        stiffener[2][2] = stiffener[5][5] = l*l*l/105.*mu;
//        stiffener[1][2] = stiffener[2][1] = 11.*l*l/210.*mu;
//        stiffener[4][5] = stiffener[5][4] = -11.*l*l/210.*mu;
//        stiffener[0][3] = stiffener[3][0] = l/6.*mu;
//        stiffener[1][4] = stiffener[4][1] = 9.*l/70.*mu;
//        stiffener[2][4] = stiffener[4][2] = 13.*l*l/420.*mu;
//        stiffener[1][5] = stiffener[5][1] = -13.*l*l/420.*mu;
//        stiffener[2][5] = stiffener[5][2] = -l*l*l/140.*mu;
//        return new Matrix(stiffener);

        double density = 78500;//N/m^3//7844.68*9.81;//76819.5;//78500;
        double[][] stiffener = new double[6][6];
        double l = getLength();
        double mu = density*area*l;
        double rz = 0;// density*momentInertia;

        stiffener[0][0] = stiffener[3][3] = l/3.*mu;
        stiffener[1][1] = stiffener[4][4] = 13.*l/35.*mu+6.*rz/(5.*l);
        stiffener[2][2] = stiffener[5][5] = l*l*l/105.*mu + 2.*l*rz/15.;
        stiffener[1][2] = stiffener[2][1] = 11.*l*l/210.*mu + rz/10.;
        stiffener[4][5] = stiffener[5][4] = -11.*l*l/210.*mu - rz/10.;
        stiffener[0][3] = stiffener[3][0] = l/6.*mu;
        stiffener[1][4] = stiffener[4][1] = 9.*l/70.*mu - 6.*rz/(5.*l);
        stiffener[2][4] = stiffener[4][2] = 13.*l*l/420.*mu - rz/10.;
        stiffener[1][5] = stiffener[5][1] = -13.*l*l/420.*mu + rz/10.;
        stiffener[2][5] = stiffener[5][2] = -l*l*l/140.*mu - rz*l/30.;

        return new Matrix(stiffener);
    }


    @Override
    protected void setGlobalDisplacementInPoint(double[] localDisplacement) {
        point[0].setGlobalDisplacement(new double[]{localDisplacement[0], localDisplacement[1], localDisplacement[2]});
        point[1].setGlobalDisplacement(new double[]{localDisplacement[3], localDisplacement[4], localDisplacement[5]});
    }

}
