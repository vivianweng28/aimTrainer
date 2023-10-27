package model;

// represents a mathematical vector, this is used for the mathematical calculations of the aim training program to
// generate feedback
public class Vector {
    private final double compX;
    private final double compY;
    private final double length;

    // EFFECTS: creates a mathematical vector with give x and y components, and calculates and assigns the vector's
    // length
    public Vector(double x, double y) {
        this.compX = x;
        this.compY = y;
        this.length = getVectorLength();
    }

    // EFFECT: get component x of this vector
    public double getCompX() {
        return compX;
    }

    // EFFECT: get component y of this vector
    public double getCompY() {
        return compY;
    }

    // EFFECT: get the length of this vector
    public double getVectorLength() {
        return Math.sqrt(Math.pow(this.compX, 2) + Math.pow(this.compY, 2));
    }

    // EFFECT: gets the vector in same direction but of length 1
    public Vector getUnitVector() {
        if (length == 0) {
            return new Vector(0,0);
        }
        Vector unit = new Vector(this.compX / length, this.compY / length);
        return unit;
    }
}
