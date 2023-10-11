package model;

import model.Target;

// a target that the user hits

public class CircleTarget implements Target {
    private double radius;
    private double centerX;
    private double centerY;
    private static final int DEFAULT_SIZE = 5;

    // EFFECTS: creates a circular target with default radius of 5, and a given center
    public CircleTarget(double cenX, double cenY) {
        this.radius = DEFAULT_SIZE;
        this.centerX = cenX;
        this.centerY = cenY;
    }

    // REQUIRES: x and y are less than dimensions of the application and >= 0.
    // EFFECTS: determines whether the shot taken has hit the target (true if hit, false if not hit)
    @Override
    public boolean hitTarget(double x, double y) {
        if (dist(x, y) <= this.radius) {
            return true;
        } else {
            return false;
        }
    }

    // REQUIRES: x and y are less than dimensions of the application and >= 0.
    // EFFECTS: calculate the distance between the point given and the center of this target
    @Override
    public double dist(double pointOneX, double pointOneY) { // dist from point to the target
        double dist = Math.sqrt(Math.pow(pointOneX - centerX, 2) + Math.pow(pointOneY - centerY, 2));
        return dist;
    }

    // REQUIRES: dist is from 1 to 500
    // MODIFIES: this
    // EFFECTS: adjusts the radius of the target from the point of view of the user depending on the distance between
    // the target and user
    @Override
    public void changeDist(double dist) { // 100m is default size
        this.radius = (dist / 100) * DEFAULT_SIZE;
    }

    // EFFECTS: returns the x coordinate of the center of the target
    @Override
    public double getCenterX() {
        return centerX;
    }

    // EFFECTS: returns the y coordinate of the center of the target
    @Override
    public double getCenterY() {
        return centerY;
    }

    // EFFECTS: returns the radius of the target
    @Override
    public double getRadius() {
        return radius;
    }
}
