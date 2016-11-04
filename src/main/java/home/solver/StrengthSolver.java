package home.solver;

import Jama.Matrix;
import home.finiteElements.interfaces.FemElement;
import home.other.FemPoint;
import home.other.Force;
import home.other.Support;
import home.solver.matrixes.SparseSquareSymmetricMatrix;
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

        SparseZeroOneMatrix A = generateMatrixCompliance(femPoints, femElements);
        SparseSquareSymmetricMatrix Kok = generateMatrixQuasiStiffener(femElements);
        Matrix Ko = SparseZeroOneMatrix.multiplyWithSquareSymmetric(A.transpose().times(Kok), A);
        Matrix K = putZeroInSupportRowColumns(Ko, supports);
        Matrix forceVector = generateForceVector(femPoints, forces, FemPoint.AMOUNT_POINT_AXES);

        long start = System.currentTimeMillis();

        //TODO big problem - optimize
        Matrix Z0 = K.solve(forceVector);

        System.out.println("Solve time = " + (System.currentTimeMillis() - start) + " msec.");

        Matrix Z0k = A.times(Z0);
        // Start calc localDisplacement
        for (int i = 0; i < femElements.length; i++) {
            int sizeAxes = femElements[i].getPoint().length * FemPoint.AMOUNT_POINT_AXES;
            double[] localDisplacement = new double[sizeAxes];
            for (int j = 0; j < localDisplacement.length; j++) {
                localDisplacement[j] = Z0k.getArray()[i * sizeAxes + j][0];
            }
            addInGlobalDisplacementCoordinate(femElements[i], localDisplacement);
        }

        FemElement.dropNumeration();
        FemPoint.dropNumeration();
    }


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

    public DeformationPoint getGlobalDeformationPoint(FemPoint point) {
        for (int i = 0; i < globalDeformationPoint.size(); i++) {
            if (globalDeformationPoint.get(i).idPoint == point.getId())
                return globalDeformationPoint.get(i);
        }
        return null;
    }

    public Matrix getLocalForces(FemElement femElement) {
        for (int i = 0; i < localForces.size(); i++) {
            if (localForces.get(i).idElement == femElement.getId())
                return localForces.get(i).forces;
        }
        return null;
    }
}
