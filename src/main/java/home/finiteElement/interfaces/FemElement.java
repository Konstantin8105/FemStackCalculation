package home.finiteElement.interfaces;

import home.other.FemPoint;

public abstract class FemElement {

    static private int global_number_id = -1;

    private static int global_number = 0;

    private static int getGlobalNumber() {
        return global_number++;
    }

    public static void dropNumeration() {
        global_number = 0;
    }

    private final int[] localAxes;
    final protected FemPoint[] point;
    private final double length;
    private int id;

    protected FemElement(FemPoint[] point) {
        this.point = point;
        localAxes = new int[getAmountLocalAxes()];
        for (int i = 0; i < localAxes.length; i++) {
            localAxes[i] = getGlobalNumber();
        }
        length = Math.sqrt(Math.pow(point[0].getX() - point[1].getX(), 2.) + Math.pow(point[0].getY() - point[1].getY(), 2.));
        id = global_number_id++;
    }

    protected double getLength() {
        return length;
    }

    abstract protected int getAmountLocalAxes();

    public int getId() {
        return id;
    }

    public int[] getLocalAxes() {
        return localAxes;
    }

    public FemPoint[] getPoint() {
        return point;
    }


}
