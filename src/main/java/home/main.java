package home;

public class main {
    public static void main(String[] args) {

        // test of iteration solver
        double x = 1.;
        double y = 0.5 * x;
        for (int i = 0; i < 20; i++) {
            System.out.println("[" + x + "," + y + "]");
            double x2 = 0.5 * y - 1.5;
            double y2 = 0.5 * x;
            x = x2;
            y = y2;
        }
//        double x1 = 0;
//        double x2 = 0;
//        double x3 = 0;
//        for (int i = 0; i < 100; i++) {
//            System.out.println(x1 + "," + x2 + "," + x3);
//            double x1_ = 1./8.*(10.-4*x2-2*x3);//-0.5 * x2 - 0.25 * x3 + 1.25;
//            double x2_ = 1./5.*(5.-3*x1-x3);//-0.6 * x1 - 0.20 * x3 + 1.00;
//            double x3_ = 1./10.*(4-3*x1+2*x2);//-0.3 * x1 + 0.20 * x2 + 0.40;
//            x1 = x1_;
//            x2 = x2_;
//            x3 = x3_;
//        }
    }
}
