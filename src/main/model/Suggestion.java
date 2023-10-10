package model;

public class Suggestion extends Shot {
    private String dirX;
    private String dirY;
    private double amtX;
    private double amtY;

    public Suggestion(double shotX, double shotY, String dirX, String dirY, double amtX, double amtY) {
        super(shotX, shotY);
        this.dirX = dirX;
        this.dirY = dirY;
        this.amtX = amtX;
        this.amtY = amtY;
    }
}
