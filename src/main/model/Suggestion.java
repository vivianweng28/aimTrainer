package model;

import org.json.JSONObject;
import persistence.Writable;

// represents a suggestion that the system generates based on the user's performance in the aim training
public class Suggestion extends Shot implements Writable {
    private final String dirX;
    private final String dirY;
    private final double amtX;
    private final double amtY;

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
        if (getDirX().equals("right")) {
            suggestion = "Shoot more to the left!";
        } else if (getDirX().equals("left")) {
            suggestion = "Shoot more to the right!";
        }

        if (getDirY().equals("up")) {
            suggestion += " Shoot more downwards!";
        } else if (getDirY().equals("down")) {
            suggestion += " Shoot more upwards!";
        }

        if (getDirX().equals("perfect") && getDirY().equals("perfect")) {
            suggestion = "Perfect!";
        }

        return suggestion;

    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Shot X", getCompX());
        json.put("Shot Y", getCompY());
        json.put("Direction X", getDirY());
        json.put("Direction Y", getDirY());
        json.put("Error Amount X", getAmtX());
        json.put("Error Amount X", getAmtY());
        return json;
    }
}
