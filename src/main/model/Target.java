package model;

// represents a target in the shape of a circle that the user hits
public class Target {
    private int radius;
    private int centerX;
    private int centerY;
    private static final int DEFAULT_SIZE = 50;

    // EFFECTS: creates a circular target with default radius of 5, and a given center
    public Target(int cenX, int cenY) {
        this.radius = DEFAULT_SIZE;
        this.centerX = cenX;
        this.centerY = cenY;
    }

    // REQUIRES: x and y are less than dimensions of the application and >= 0.
    // EFFECTS: determines whether the shot taken has hit the target (true if hit, false if not hit)
    public boolean hitTarget(int x, int y) {
        if (dist(x, y) <= this.radius) {
            return true;
        } else {
            return false;
        }
    }

    // REQUIRES: x and y are less than dimensions of the application and >= 0.
    // EFFECTS: calculate the distance between the point given and the center of this target
    public double dist(double pointOneX, double pointOneY) { // dist from point to the target
        double dist = Math.sqrt(Math.pow(pointOneX - centerX, 2) + Math.pow(pointOneY - centerY, 2));
        return dist;
    }

    // REQUIRES: dist is from 1 to 500, inclusive
    // MODIFIES: this
    // EFFECTS: adjusts the radius of the target from the point of view of the user depending on the distance between
    // the target and user
    public void changeDist(double dist) { // 100m is default size
        this.radius = (int) ((DEFAULT_SIZE / dist) * DEFAULT_SIZE);
    }

    // EFFECTS: returns the x coordinate of the center of the target
    public int getCenterX() {
        return centerX;
    }

    // EFFECTS: returns the y coordinate of the center of the target
    public int getCenterY() {
        return centerY;
    }

    // EFFECTS: returns the radius of the target
    public int getRadius() {
        return radius;
    }
}