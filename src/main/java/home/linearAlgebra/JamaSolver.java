package home.linearAlgebra;

import Jama.Matrix;

public class JamaSolver implements SolverSystemOfLinearEquations {
    @Override
    public Matrix solve(Matrix A, Matrix v) {

//        System.out.println("\t\t// Matrix A //");
//        System.out.println("double[][] a = new double[][]{");
//        for (int i = 0; i < A.getRowDimension(); i++) {
//            System.out.print("{");
//            for (int j = 0; j < A.getColumnDimension(); j++) {
//                System.out.print(r(String.format("%.8e", A.get(i, j)))+",");
//            }
//            System.out.println("},");
//        }
//        System.out.println("};");
//
//        System.out.println("\t\t// Vector V //");
//        System.out.println("double[][] v = new double[][]{");
//        for (int i = 0; i < v.getRowDimension(); i++) {
//            System.out.print(r(String.format("{%.8e}", v.get(i, 0)))+",");
//        }
//        System.out.println("};");

        return A.solve(v);
    }

//    String r(String str){
//        return str.replace(',','.');
//    }
}
