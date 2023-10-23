package model;

// represents a theoretical shot that the user could take on the target
public class Shot {
    protected final double compX;
    protected final double compY;

    // EFFECTS: creates a shot with x coordinate at compX and y coordinate at compY
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
