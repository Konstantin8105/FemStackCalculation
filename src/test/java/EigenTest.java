import jama.EigenvalueDecomposition;
import jama.Matrix;
import org.junit.Assert;
import org.junit.Test;

public class EigenTest {
    @Test
    public void eigenMatrix(){
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
        Assert.assertEquals(ei.getRealEigenvalues()[0],2.,1e-5);
        Assert.assertEquals(ei.getRealEigenvalues()[1],3.,1e-5);
    }
}
