package home.FemElements;

public class FemBeam2d {

    public int number;
    FemPoint pi, pj;
    public double E;
    public double A;
    public double J;

    FemBeam2d(int number_, FemPoint pi_, FemPoint pj_, double E_, double A_, double J_) {
        number = number_;
        pi = pi_;
        pj = pj_;
        E = E_;
        A = A_;
        J = J_;
    }

    public double Lij() {//Length
        return Math.sqrt((pi.getX() - pj.getX()) * (pi.getX() - pj.getX()) + (pi.getY() - pj.getY()) * (pi.getY() - pj.getY()));
    }

    public double[] Vij() {//Vector cosinus
        double Vij[] = new double[4];
        Vij[0] = (pj.getX() - pi.getX()) / Lij();        //LambdaXX0
        Vij[1] = (pj.getY() - pi.getY()) / Lij();        //LambdaXY0
        Vij[2] = -(pj.getY() - pi.getY()) / Lij();    //LambdaYX0
        Vij[3] = (pj.getX() - pi.getX()) / Lij();        //LambdaYY0
        return Vij;
    }

    public double[][] Tr() {//matrix
        double[][] Tr = new double[6][6];
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 6; j++)
                Tr[i][j] = 0f;
        double[] Vij = Vij();
        Tr[0][0] = Tr[3][3] = Vij[0];
        Tr[0][1] = Tr[3][4] = Vij[1];
        Tr[1][0] = Tr[4][3] = Vij[2];
        Tr[1][1] = Tr[4][4] = Vij[3];
        Tr[2][2] = Tr[5][5] = 1.f;
        return Tr;
    }

    public double[][] Tr_1() {//matrix
        double[][] Tr_1 = new double[6][6];
        double[][] Tr = Tr();
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 6; j++)
                Tr_1[i][j] = Tr[j][i];
        return Tr_1;
    }

    // FE compress-tention-bending
    double[][] Kr() {
        double[][] Stiff = new double[6][6];
        double l = Lij();
        double EFl = E * A / l;
        double EJ = E * J;
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 6; j++)
                Stiff[i][j] = 0;
        Stiff[0][0] = Stiff[3][3] = EFl;
        Stiff[1][1] = Stiff[4][4] = 12 * EJ / (float) Math.pow((double) l, 3);
        Stiff[2][2] = Stiff[5][5] = 4 * EJ / l;
        Stiff[1][2] = Stiff[2][1] = 6 * EJ / (float) Math.pow((double) l, 2);
        Stiff[4][5] = Stiff[5][4] = -6 * EJ / (float) Math.pow((double) l, 2);
        Stiff[0][3] = Stiff[3][0] = -EFl;
        Stiff[1][4] = Stiff[4][1] = -12 * EJ / (float) Math.pow((double) l, 3);
        Stiff[1][5] = Stiff[5][1] = 6 * EJ / (float) Math.pow((double) l, 2);
        Stiff[2][4] = Stiff[4][2] = -6 * EJ / (float) Math.pow((double) l, 2);
        Stiff[2][5] = Stiff[5][2] = 2 * EJ / l;
        return Stiff;
    }
}
