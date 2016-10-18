import jama.EigenvalueDecomposition;
import jama.Matrix;
import org.junit.Assert;
import org.junit.Test;

public class EigenTest {
    @Test
    public void eigenMatrix2x2(){
        double [][] m = new double[][]{
                {-1.,-6.},
                {2.,6.}
        };
        Matrix A = new Matrix(m);
        EigenvalueDecomposition ei = new EigenvalueDecomposition(A);
        ei.getD().print(10,5);
        ei.getV().print(10,5);
        for (int i = 0; i < ei.getRealEigenvalues().length; i++) {
            System.out.println("Real  "+ei.getRealEigenvalues()[i]);
        }
        for (int i = 0; i < ei.getImagEigenvalues().length; i++) {
            System.out.println("Image "+ei.getImagEigenvalues()[i]);
        }
        Assert.assertEquals(ei.getD().getArray()[0][0],2.,1e-5);
        Assert.assertEquals(ei.getD().getArray()[1][1],3.,1e-5);
    }
    @Test
    public void eigenMatrix3x3() {
        double[][] m = new double[][]{
                {3,6,3},
                {-1,-2,1},
                {1,2,-3}
        };
        Matrix A = new Matrix(m);
        EigenvalueDecomposition ei = new EigenvalueDecomposition(A);
        ei.getD().print(10,5);
        ei.getV().print(10,5);
        for (int i = 0; i < ei.getRealEigenvalues().length; i++) {
            System.out.println("Real  "+ei.getRealEigenvalues()[i]);
        }
        for (int i = 0; i < ei.getImagEigenvalues().length; i++) {
            System.out.println("Image "+ei.getImagEigenvalues()[i]);
        }
        Assert.assertEquals(ei.getD().getArray()[0][0],2.,1e-5);
        Assert.assertEquals(ei.getD().getArray()[1][1],0.,1e-5);
        Assert.assertEquals(ei.getD().getArray()[2][2],-4.,1e-5);
    }
}
