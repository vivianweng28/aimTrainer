package model;

public class Shot {
    private double dirX;
    private double dirY;

    public Shot(double dirX, double dirY) {
        this.dirX = dirX;
        this.dirY = dirY;
    }

    public double getDirX() {
        return dirX;
    }

    public double getDirY() {
        return dirY;
    }
}
