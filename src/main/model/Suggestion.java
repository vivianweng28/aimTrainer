package model;

// represents a suggestion that the system generates based on the user's performance in the aim training
public class Suggestion extends Shot {
    private String dirX;
    private String dirY;
    private double amtX;
    private double amtY;

    // EFFECTS: creates a suggestion that records the shot taken, and the deviance from the target
    public Suggestion(double shotX, double shotY, String dirX, String dirY, double amtX, double amtY) {
        super(shotX, shotY);
        this.dirX = dirX;
        this.dirY = dirY;
        this.amtX = amtX;
        this.amtY = amtY;
    }

    // EFFECTS: gets the distance in the x direction between the shot taken and the target
    public double getAmtX() {
        return amtX;
    }

    // EFFECTS: gets the distance in the y direction between the shot taken and the target
    public double getAmtY() {
        return amtY;
    }

    // EFFECTS: gets the direction that the shot was relative to the target in the x direction (left or right)
    public String getDirX() {
        return dirX;
    }

    // EFFECTS: gets the direction that the shot was relative to the target in the y direction (up or down)
    public String getDirY() {
        return dirY;
    }

    // EFFECTS: gives written suggestion for the shot taken regarding how to hit the target
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
            suggestion = "Perfect!";
        }

        return suggestion;

    }
}
