package home.viewer;

import home.finiteElement.FemBeam2d;
import home.finiteElement.interfaces.FemElement;
import home.other.FemPoint;

import javax.swing.*;
import java.awt.*;

public class Viewer extends JFrame {

    public Viewer(final FemPoint[] femPoints, final FemElement[] femElements) {

        final BorderBox box = new BorderBox();
        for (int i = 0; i < femPoints.length; i++) {
            System.out.println(i);
            box.addPoint(femPoints[i]);
        }

        int WINDOWS_SIZE = 800;
        final double scale = Math.min(
                WINDOWS_SIZE / (box.getX_max() - box.getX_min()),
                WINDOWS_SIZE / (box.getY_max() - box.getY_min())
        );

        for (FemPoint femPoint : femPoints) {
            femPoint.setX(femPoint.getX() * scale + 20);
            femPoint.setY(femPoint.getY() * scale + 20);
        }

        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = ((Graphics2D) g);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setStroke(new BasicStroke(1));

                for (int i = 0; i < femElements.length; i++) {
                    g.setColor(Color.CYAN);
                    g.drawLine(
                            (int) femElements[i].getPoint()[0].getX(),
                            (int) femElements[i].getPoint()[0].getY(),
                            (int) femElements[i].getPoint()[1].getX(),
                            (int) femElements[i].getPoint()[1].getY()
                    );
                    g.setColor(Color.RED);
                    g.drawString(
                            i + "",
                            (int) ((femElements[i].getPoint()[0].getX() +femElements[i].getPoint()[1].getX())/2.),
                            (int) ((femElements[i].getPoint()[0].getY() +femElements[i].getPoint()[1].getY())/2.)
                    );
                }

                g.setColor(Color.BLUE);
                for (int i = 0; i < femPoints.length; i++) {
                    g.drawOval(
                            (int) femPoints[i].getX() - 1,
                            (int) femPoints[i].getY() - 1,
                            3, 3);
                    g.drawString(
                            i + "",
                            (int) femPoints[i].getX(),
                            (int) femPoints[i].getY()
                    );
                }
            }
            //special point
        };
        setContentPane(panel);
        this.setSize(new Dimension(WINDOWS_SIZE + 100, WINDOWS_SIZE + 100));
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

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
