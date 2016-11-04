package home.solver;

import Jama.Matrix;
import home.finiteElements.interfaces.FemElement;
import home.other.FemPoint;
import home.other.Force;
import home.other.Support;
import home.solver.matrixes.SparseZeroOneMatrix;

import java.util.ArrayList;
import java.util.List;

public class StrengthSolver extends Solver {


    public class DeformationPoint {
        int idPoint;
        double[] deformation;

        public DeformationPoint(int idPoint, double[] deformation) {
            this.idPoint = idPoint;
            this.deformation = deformation;
        }

        public double getX() {
            return deformation[0];
        }

        public double getY() {
            return deformation[1];
        }

        public double getRotate() {
            return deformation[2];
        }
    }

    List<DeformationPoint> localDeformationPoint = new ArrayList<>();
    List<DeformationPoint> globalDeformationPoint = new ArrayList<>();

    class LocalForce {
        int idElement;
        Matrix forces;

        public LocalForce(int idElement, Matrix forces) {
            this.idElement = idElement;
            this.forces = forces;
        }
    }

    List<LocalForce> localForces = new ArrayList<>();

    public StrengthSolver(
            FemPoint[] femPoints,
            FemElement[] femElements,
            Force[] forces,
            Support[] supports) throws Exception {

        FemElement.dropNumeration();
        FemPoint.dropNumeration();

        if (forces == null)
            return;
        if (forces.length == 0)
            return;

        convertPointGlobalAxeToNumber = convertPointAxeToSequenceAxe(femElements);
        convertLineGlobalAxeToNumber = convertLineAxeToSequenceAxe(femElements);

        //TODO optimize
        SparseZeroOneMatrix A = generateMatrixCompliance(femPoints, femElements);
        Matrix Kok = generateMatrixQuasiStiffener(femElements);
        Matrix Ko = A.convert().transpose().times(Kok).times(A.convert());
        Matrix K = putZeroInSupportRowColumns(Ko, supports);
        Matrix forceVector = generateForceVector(femPoints, forces, FemPoint.AMOUNT_POINT_AXES);
        //TODO optimize
        Matrix Z0 = K.solve(forceVector);
        //TODO optimize
        Matrix Z0k = A.convert().times(Z0);

        if (DEBUG) System.out.println("Start calc localDisplacement");
        for (int i = 0; i < femElements.length; i++) {
            int sizeAxes = femElements[i].getPoint().length * FemPoint.AMOUNT_POINT_AXES;
            double[] localDisplacement = new double[sizeAxes];
            for (int j = 0; j < localDisplacement.length; j++) {
                localDisplacement[j] = Z0k.getArray()[i * sizeAxes + j][0];
            }
            addInGlobalDisplacementCoordinate(femElements[i], localDisplacement);
        }
//        if (DEBUG) {
//            for (FemElement femElement : femElements) {
//                System.out.println(femElement);
//                femElement.getInternalForce().print(10, 1);
//            }
//        }
//
//        if (DEBUG) {
//            Matrix Z01 = K.solve(forceVector);
//            System.out.println("Z01");
//            Z01.print(15, 10);
//            Matrix G0 = K.solve(Z01);
//            System.out.println("G0");
//            G0.print(15, 10);
//        }
//
//        if (DEBUG) {
//            for (int i = 0; i < femPoints.length; i++) {
//                System.out.println("[" + i + "]");
//                for (int j = 0; j < femPoints[i].getGlobalDisplacement().length; j++) {
//                    System.out.println("\t " + femPoints[i].getGlobalDisplacement()[j]);
//                }
//            }
//        }

        FemElement.dropNumeration();
        FemPoint.dropNumeration();
    }


//    private Matrix displacementInGlobalSystem;
//
//    public Matrix getDisplacementInGlobalSystem() {
//        return displacementInGlobalSystem;
//    }
//
//    private Matrix displacementInLocalSystem;
//
//    public Matrix getDisplacementInLocalSystem() {
//        return displacementInLocalSystem;
//    }
//
//    private Matrix internalForce;
//
//    public Matrix getInternalForce() {
//        return internalForce;
//    }


    public void addInGlobalDisplacementCoordinate(FemElement femElement, double[] localDisplacement) {
        double[][] temp = new double[localDisplacement.length][1];
        for (int i = 0; i < localDisplacement.length; i++) {
            temp[i][0] = localDisplacement[i];
        }
        int position = 0;
        for (int i = 0; i < femElement.getPoint().length; i++) {
            double[] deformation = new double[FemPoint.AMOUNT_POINT_AXES];
            for (int j = 0; j < FemPoint.AMOUNT_POINT_AXES; j++) {
                deformation[j] = localDisplacement[position++];
            }
            globalDeformationPoint.add(new DeformationPoint(
                    femElement.getPoint()[i].getId(), deformation));
        }
//        femElement.setGlobalDisplacementInPoint(localDisplacement);
        Matrix displacementInGlobalSystem = new Matrix(temp);
        Matrix displacementInLocalSystem = femElement.getTr().times(displacementInGlobalSystem);
        position = 0;
        for (int i = 0; i < femElement.getPoint().length; i++) {
            double[] deformation = new double[3];
            for (int j = 0; j < FemPoint.AMOUNT_POINT_AXES; j++) {
                deformation[j] = displacementInLocalSystem.getArray()[position++][0];
            }
            localDeformationPoint.add(new DeformationPoint(
                    femElement.getPoint()[i].getId(), deformation));
        }
        Matrix internalForce = femElement.getStiffenerMatrix().times(displacementInLocalSystem);
        localForces.add(new LocalForce(femElement.getId(), internalForce));
    }

    public List<DeformationPoint> getLocalDeformationPoint() {
        return localDeformationPoint;
    }

    public List<DeformationPoint> getGlobalDeformationPoint() {
        return globalDeformationPoint;
    }

    public DeformationPoint getGlobalDeformationPoint(int id) {
        for (int i = 0; i < globalDeformationPoint.size(); i++) {
            if (globalDeformationPoint.get(i).idPoint == id)
                return globalDeformationPoint.get(i);
        }
        return null;
    }

    public DeformationPoint getGlobalDeformationPoint(FemPoint point) {
        return getGlobalDeformationPoint(point.getId());
    }

    public List<LocalForce> getLocalForces() {
        return localForces;
    }
}
