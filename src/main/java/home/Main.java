package home;

import home.finiteElement.FemBeam2d;
import home.finiteElement.FemElement;
import home.other.FemPoint;

public class Main {
    public static void main(String[] args) {

        FemPoint[] femPoints = new FemPoint[9];
        femPoints[0] = new FemPoint(0, 0.0, 0.4);
        femPoints[1] = new FemPoint(1, 0.2, 0.4);
        femPoints[2] = new FemPoint(2, 0.4, 0.4);
        femPoints[3] = new FemPoint(3, 0.4, 0.2);
        femPoints[4] = new FemPoint(4, 0.4, 0.0);
        femPoints[5] = new FemPoint(5, 0.6, 0.4);
        femPoints[6] = new FemPoint(6, 0.8, 0.4);
        femPoints[7] = new FemPoint(7, 0.8, 0.2);
        femPoints[8] = new FemPoint(8, 0.8, 0.0);

        FemElement[] femElements = new FemElement[8];
        femElements[0] = new FemBeam2d(2e11, 24e-4, 32e-8, new FemPoint[]{femPoints[0], femPoints[1]});
        femElements[1] = new FemBeam2d(2e11, 24e-4, 32e-8, new FemPoint[]{femPoints[1], femPoints[2]});
        femElements[2] = new FemBeam2d(2e11, 24e-4, 32e-8, new FemPoint[]{femPoints[2], femPoints[3]});
        femElements[3] = new FemBeam2d(2e11, 24e-4, 32e-8, new FemPoint[]{femPoints[3], femPoints[4]});
        femElements[4] = new FemBeam2d(2e11, 48e-4, 64e-8, new FemPoint[]{femPoints[2], femPoints[5]});
        femElements[5] = new FemBeam2d(2e11, 48e-4, 64e-8, new FemPoint[]{femPoints[5], femPoints[6]});
        femElements[6] = new FemBeam2d(2e11, 24e-4, 32e-8, new FemPoint[]{femPoints[6], femPoints[7]});
        femElements[7] = new FemBeam2d(2e11, 24e-4, 32e-8, new FemPoint[]{femPoints[7], femPoints[8]});

        Viewer viewer = new Viewer(femPoints,femElements);
    }
}
