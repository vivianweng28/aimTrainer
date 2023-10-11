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

    public double getAmtX() {
        return amtX;
    }

    public double getAmtY() {
        return amtY;
    }

    public String getDirX() {
        return dirX;
    }

    public String getDirY() {
        return dirY;
    }

    public String giveSuggestion() {
        String suggestion = "";
        if (dirX.equals("right")) {
            suggestion = "Shoot more to the left!";
        } else if (dirX.equals("left")) {
            suggestion = "Shoot more to the right!";
        }

        if (dirY.equals("up")) {
            suggestion += " Shoot more downwards!";
        } else if (dirY.equals("down")) {
            suggestion += " Shoot more upwards!";
        }

        if (dirX.equals("perfect") && dirY.equals("perfect")) {
            suggestion = "Perfect shot!";
        }

        return suggestion;

    }
}
