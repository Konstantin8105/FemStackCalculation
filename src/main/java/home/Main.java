package home;

import home.finiteElement.FemBeam2d;
import home.finiteElement.interfaces.FemElement;
import home.other.FemPoint;

public class Main {
    public static void main(String[] args) {

        FemPoint[] femPoints = new FemPoint[7];
        femPoints[0] = new FemPoint(0, 0, 0);
        femPoints[1] = new FemPoint(1, 1, 0);
        femPoints[2] = new FemPoint(2, 2, 0);
        femPoints[3] = new FemPoint(3, 3, 0);
        femPoints[4] = new FemPoint(4, 4, 0);
        femPoints[5] = new FemPoint(5, 2, 1);
        femPoints[6] = new FemPoint(6, 2, 2);

        double elacity = 2.05e11;
        double inertia = 7.8539e-5;
        double area = 0.314159;

        FemElement[] femElements = new FemElement[6];
        femElements[0] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[0], femPoints[1]});
        femElements[1] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[1], femPoints[2]});
        femElements[2] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[2], femPoints[3]});
        femElements[3] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[3], femPoints[4]});
        femElements[4] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[5], femPoints[6]});
        femElements[5] = new FemBeam2d(elacity, area, inertia, new FemPoint[]{femPoints[5], femPoints[2]});

        Viewer viewer = new Viewer(femPoints,femElements);
    }
}
