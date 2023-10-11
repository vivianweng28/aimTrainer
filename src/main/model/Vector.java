package model;

public class Vector {
    private double compX;
    private double compY;
    private double length;

    public Vector(double x, double y) {
        this.compX = x;
        this.compY = y;
        this.length = getVectorLength();
    }

    //EFFECT: get component x of this vector
    public double getCompX() {
        return compX;
    }

    //EFFECT: get component y of this vector
    public double getCompY() {
        return compY;
    }

    //EFFECT: get the length of this vector
    public double getVectorLength() {
        return Math.sqrt(Math.pow(this.compX, 2) + Math.pow(this.compY, 2));
    }

    public Vector getUnitVector() {
        Vector unit = new Vector(this.compX / length, this.compY / length);
        return unit;
    }
}
