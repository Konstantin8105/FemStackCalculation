package home;

public enum Direction {
    DIRECTION_X(0),
    DIRECTION_Y(1),
    ROTATE(2);
    private int position;

    Direction(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
