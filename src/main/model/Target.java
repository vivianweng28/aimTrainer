package model;

// represents a target that the user is aiming for
public interface Target {

    // REQUIRES: x and y are less than dimensions of the application and >= 0.
    // EFFECTS: determines whether the shot taken has hit the target (true if hit, false if not hit)
    boolean hitTarget(double x, double y);

    // REQUIRES: x and y are less than dimensions of the application and >= 0.
    // EFFECTS: calculate the distance between the point given and the center of this target
    double dist(double pointOneX, double pointOneY);

    // REQUIRES: dist is from 1 to 500, inclusive
    // MODIFIES: this
    // EFFECTS: adjusts the radius of the target from the point of view of the user depending on the distance between
    // the target and user
    void changeDist(double dist);

    // EFFECTS: returns the x coordinate of the center of the target
    double getCenterX();

    // EFFECTS: returns the y coordinate of the center of the target
    double getCenterY();

    // EFFECTS: returns the radius of the target
    double getRadius();

}
