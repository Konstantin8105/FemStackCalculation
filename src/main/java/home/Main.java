package home;

public class Main {

    public static void main(String[] args) {
/*
        double Height = 2.5f;//meter
        int AmountPoints = 3;
        double Zpoint[] = new double[AmountPoints];
        for(int i=0;i<Zpoint.length;i++)
            Zpoint[i] = ((double)i/((double)Zpoint.length-1))*Height;
        double ID = 0.1f;
        double OD = ID+2f*0.010f;

        MKEPoint points[] = new MKEPoint[Zpoint.length];
        for(int i=0;i<points.length;i++){
            MKEPoint tmp = new MKEPoint(i,0.f,Zpoint[i]);//Zpoint[i]
            points[i] = tmp;
        }
// 1. ќбразование расчЄтной схемы.
// 2. ¬ычисление матриц[Kr ] жЄсткости  Ё в местной системе
// 3. ¬ычисление матриц[Tr ] ортогонального преобразовани¤  Ё
// 4. ¬ычисление матриц[K0r] жЄсткости  Ё в общей системе координат
// 5. ‘ормирование матрицы жЄсткости конструкции
// 6. ‘ормирование разрешающей системы уравнений равновеси¤
// 7. –ешение системы уравнений
// 8. ¬ычисление вектора внутренних узловых сил конструкции в общей системе координат
// 9. ¬ычисление векторов внутренних узловых сил  Ё в местной сис-теме координат
// 10. ¬ычисление компонентов напр¤жений в узлах  Ё в местной систе-ме координат

        MKEPoint support = points[0];

        FemBeam2d lines[] = new FemBeam2d[points.length-1];
        for(int i=0;i<lines.length;i++){
            FemBeam2d tmp = new FemBeam2d(i,points[i], points[i+1],
                    Standard.E,
                    Standard.Area_tube(ID,OD),
                    Standard.Inertia_tube(ID,OD));
            lines[i] = tmp;
        }

        //////////////////////
        double GlobalK[][] = new double[points[0].GetGlobalNumber()][points[0].GetGlobalNumber()];
        double GlobalForce[] = new double[points[0].GetGlobalNumber()];
        for(int i=0;i<GlobalForce.length;i++)
            GlobalForce[i] = 0f;
        GlobalForce[GlobalForce.length-3] = 10000f;
        //GlobalForce[GlobalForce.length-2] = 10000f;
        for(int i=0;i<GlobalK.length;i++)
            for(int j=0;j<GlobalK[0].length;j++)
                GlobalK[i][j] = 0;
        for(int i=0;i<lines.length;i++){
            double Kr_global[][] = new double[6][6];
            Kr_global = Standard.multiply(lines[i].Tr(), lines[i].Kr());
            Kr_global = Standard.multiply(Kr_global, lines[i].Tr_1());
            int number_of_points[] = new int [6];
            for(int j=0;j<3;j++) number_of_points[j] = lines[i].pi.number_axe_first+j;
            for(int j=3;j<6;j++) number_of_points[j] = lines[i].pj.number_axe_first+(j-3);
            for(int j=0;j<number_of_points.length;j++)
                for(int k=0;k<number_of_points.length;k++){
                    GlobalK[number_of_points[j]][number_of_points[k]] += Kr_global[j][k];
                }
        }
        for(int i=support.number_axe_first;i<support.number_axe_first+3;i++){
            for(int j=0;j<GlobalK.length;j++){
                GlobalK[i][j] = 0;
                GlobalK[j][i] = 0;
            }
            GlobalK[i][i] = 1f;
            GlobalForce[i] = 0f;
        }
        double GlobalDisplacement[] = new double[points[0].GetGlobalNumber()];
        GlobalDisplacement = Standard.gauss(GlobalK, GlobalForce);

        System.out.println(
                "DX"+
                        "\t\t"+	"DY"+
                        "\t\t"+	"RXY");

        for(int i=0;i<GlobalDisplacement.length;i+=3){
            System.out.println(
                    String.format("%+.2e", GlobalDisplacement[i])+
                            "\t"+	String.format("%+.2e", GlobalDisplacement[i+1])+
                            "\t"+	String.format("%+.2e", GlobalDisplacement[i+2]));
        }

        for(int i=0;i<lines.length;i++){
            int number_of_points[] = new int [6];
            for(int j=0;j<3;j++) number_of_points[j] = lines[i].pi.number_axe_first+j;
            for(int j=3;j<6;j++) number_of_points[j] = lines[i].pj.number_axe_first+(j-3);
            double GlobalLocalDisplacement[] = new double[6];
            for(int j=0;j<6;j++)GlobalLocalDisplacement[j] = GlobalDisplacement[number_of_points[j]];
            double LocalDisplacement[] = Standard.multiply(lines[i].Tr_1(), GlobalLocalDisplacement);
            lines[i].Print();
            System.out.println(
                    "LocalDisplacement\n"+
                            "\t"+	String.format("%+.2e", LocalDisplacement[0])+
                            "\t"+	String.format("%+.2e", LocalDisplacement[1])+
                            "\t"+	String.format("%+.2e", LocalDisplacement[2])+ "\n"+
                            "\t"+	String.format("%+.2e", LocalDisplacement[3])+
                            "\t"+	String.format("%+.2e", LocalDisplacement[4])+
                            "\t"+	String.format("%+.2e", LocalDisplacement[5]));

            double LocalReaction[] = Standard.multiply(lines[i].Kr(), LocalDisplacement);
            System.out.println(
                    "LocalReaction\n"+
                            "\t"+	String.format("%+.2e", LocalReaction[0])+
                            "\t"+	String.format("%+.2e", LocalReaction[1])+
                            "\t"+	String.format("%+.2e", LocalReaction[2])+ "\n"+
                            "\t"+	String.format("%+.2e", LocalReaction[3])+
                            "\t"+	String.format("%+.2e", LocalReaction[4])+
                            "\t"+	String.format("%+.2e", LocalReaction[5]));
        }*/
//////////////////////
// Buckling
// 1. ќбразование расчЄтной схемы.
// 2. ¬ычисление матриц[Kr] жЄсткости  Ё в местной системе коорди-нат XYZ.
// 3. ¬ычисление матриц[Gr] потенциала нагрузки  Ё в местной систе-ме координат.
// 4. ¬ычисление матриц[Tr] ортогонального преобразовани¤  Ё.
// 5. ¬ычисление матриц[K0r] жЄсткости  Ё в общей системе координат
// 6. ¬ычисление матриц[G0r] потенциала нагрузки  Ё в общей системе
// 7. ‘ормирование матрицы жЄсткости конструкции
// 8. ‘ормирование матрицы потенциала нагрузки конструкции
// 9. ‘ормирование разрешающей системы уравнений равновеси¤
// 10. –ешение разрешающей системы уравнений
// 11. –ешение уравнений собственных значений
//////////////////////
    }
}
