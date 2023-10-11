package model;

public class Shot {
    private double compX;
    private double compY;

    public Shot(double compX, double compY) {
        this.compX = compX;
        this.compY = compY;
    }

    // EFFECTS: get x coordinate of the shot
    public double getCompX() {
        return compX;
    }

    // EFFECTS: get y coordinate of the shot
    public double getCompY() {
        return compY;
    }
}
