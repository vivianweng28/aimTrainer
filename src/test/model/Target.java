package model;

public interface Target {

    public boolean hitTarget(double x, double y);

    public double dist(double pointOneX, double pointOneY, double pointTwoX, double pointTwoY);

    public void changeSize(double radius);

}
