package model;

public interface Target {

    boolean hitTarget(double x, double y);

    double dist(double pointOneX, double pointOneY);

    void changeDist(double dist);

    double getCenterX();

    double getCenterY();

    double getRadius();

}
