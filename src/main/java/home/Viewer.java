package home;

import home.finiteElement.FemElement;
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
}
