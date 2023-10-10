package model;

import model.Target;

// a target that the user hits

public class CircleTarget implements Target {
    private double radius;
    private double centerX;
    private double centerY;
    private static final int DEFAULT_SIZE = 5;

    public CircleTarget(double cenX, double cenY) {
        this.radius = DEFAULT_SIZE;
        this.centerX = cenX;
        this.centerY = cenY;
    }

    @Override
    public boolean hitTarget(double x, double y) {
        if (dist(x, y) <= this.radius) {
            return true;
        } else {
            return false;
        }
    }

    public double dist(double pointOneX, double pointOneY) { // dist from point to the target
        double dist = Math.sqrt(Math.pow(pointOneX - centerX, 2) + Math.pow(pointOneY - centerY, 2));
        return dist;
    }

    public void changeSize(double radius) {
        this.radius = radius;
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public double getRadius() {
        return radius;
    }
}
