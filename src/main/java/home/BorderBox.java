package home;

import home.other.FemPoint;

/**
 * Create rectangle for future check point outside rectangle.
 * Dimension: 2D
 *
 * @author Izyumov Konstantin
 * @since 24/08/2016
 */
public class BorderBox {
    private double x_min;
    private double x_max;
    private double y_min;
    private double y_max;

    private boolean calculateCenter;
    private FemPoint center;

    public BorderBox() {
        calculateCenter = true;
        x_min = Double.MAX_VALUE;
        x_max = -Double.MAX_VALUE;
        y_min = Double.MAX_VALUE;
        y_max = -Double.MAX_VALUE;
    }

    /**
     * Box change the size by X or Y and input point will be inside in rectangle or on border of rectangle
     *
     * @param point input data
     * @see FemPoint
     */
    public void addPoint(FemPoint point) {
        calculateCenter = true;
        x_min = Math.min(x_min, point.getX());
        x_max = Math.max(x_max, point.getX());
        y_min = Math.min(y_min, point.getY());
        y_max = Math.max(y_max, point.getY());
    }

    public double getX_min() {
        return x_min;
    }

    public double getX_max() {
        return x_max;
    }

    public double getY_min() {
        return y_min;
    }

    public double getY_max() {
        return y_max;
    }

    /**
     * Calculate center of rectangle
     *
     * @return point center of rectangle
     * @see FemPoint
     */
    public FemPoint getCenter() {
        if (calculateCenter) {
            center = new FemPoint(-1,
                    (x_min + x_max) / 2.0,
                    (y_min + y_max) / 2.0);
            calculateCenter = false;
        }
        return center;
    }

    /**
     * Scale rectangle at center point
     *
     * @param center      - center of scaling
     * @param scaleFactor - scale factor
     */
    public void scale(double scaleFactor, FemPoint center) {
        x_min = center.getX() - (center.getX() - x_min) * scaleFactor;
        x_max = center.getX() + (x_max - center.getX()) * scaleFactor;
        y_min = center.getY() - (center.getY() - y_min) * scaleFactor;
        y_max = center.getY() + (y_max - center.getY()) * scaleFactor;
    }
}