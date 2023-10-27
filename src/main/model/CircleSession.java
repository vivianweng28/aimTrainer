package model;

import org.json.*;
import persistence.JsonReader;
import persistence.JsonWriter;
import persistence.Writable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// represents an aim training session where the targets are circles
public class CircleSession implements Session, Writable {
    private final List<Suggestion> allSuggestions;
    private int hit;
    private int shots;
    private static final String SESSION_TYPE = "circle";
    private static final int DEFAULT_DISTANCE = 100;
    private int distance;
    private int sessionNum;
    private JSONObject pastSessions;
    private Target currentTarget;

    // EFFECTS: creates new circle target session with no hits, shots, accuracy or summary suggestion, and has no
    // suggestions recorded.
    public CircleSession(int sessionNum) {
        allSuggestions = new ArrayList<Suggestion>();
        hit = 0;
        shots = 0;
        this.sessionNum = sessionNum;
        distance = DEFAULT_DISTANCE;
    }

    // EFFECTS: retrieves the distance from target
    public int getDistance() {
        return distance;
    }

    // MODIFIES: this
    // EFFECTS: changes the distance from target
    public void setDistance(int newDistance) {
        this.distance = newDistance;
    }

    // MODIFIES: this
    // EFFECTS: changes current target to a new target
    public void setTarget(Target t) {
        currentTarget = t;
    }

    // EFFECTS: retrieves the current target
    public Target getTarget() {
        return currentTarget;
    }

    // REQUIRES: x, y >= 0, centerX, centerY, and radius > 0, point (x,y) is not in the circular target with center at
    // (centerX, centerY) and a radius of length radius.
    // MODIFIES: this
    // EFFECTS: compares shot taken by player to the closest shot on the target and generates
    // suggestion, adds that suggestion to list of all suggestions
    public void analyze(double x, double y, double centerX, double centerY, double radius) {
        shots++;
        Vector vec = new Vector(x - centerX, y - centerY);

        Shot closestShot = getClosestShot(vec, radius, centerX, centerY);

        double adjustX = x - closestShot.getCompX();
        double adjustY = y - closestShot.getCompY();

        String dirX = "perfect";
        String dirY = "perfect";

        if (adjustX > 0) {
            dirX = "right";
        } else if (adjustX < 0) {
            dirX = "left";
        }

        if (adjustY > 0) {
            dirY = "up";
        } else if (adjustY < 0) {
            dirY = "down";
        }

        adjustX = Math.abs(adjustX);
        adjustY = Math.abs(adjustY);

        Suggestion suggest = new Suggestion(x, y, dirX, dirY, adjustX, adjustY);

        allSuggestions.add(suggest);
    }

    // REQUIRES: radius, centerX and centerY > 0
    // EFFECTS: generates closest shot on the target from the user shot
    public Shot getClosestShot(Vector vector, double radius, double centerX, double centerY) {
        double x = centerX + radius * vector.getUnitVector().getCompX();
        double y = centerY + radius * vector.getUnitVector().getCompY();
        Shot s = new Shot(x, y);
        return s;
    }

    // MODIFIES: this
    // EFFECTS: increases the number of times the user's shot hits the target by one, increases the total number of
    // shots by one
    public void hit() {
        hit++;
    }

    // EFFECTS: gets the list of all generated suggestions
    public List<Suggestion> getAllSuggestions() {
        return allSuggestions;
    }

    // EFFECTS: gets the total session accuracy in percentage
    public double getAccuracy() {
        return (double) hit / (double) shots * 100;
    }

    // EFFECTS: gets the total number of times the user's shot has hit the target
    public int getHit() {
        return hit;
    }

    // EFFECTS: gets the total number of times the user has taken a shot
    public int getShots() {
        return shots;
    }

    // EFFECTS: sets the total number of times the user's shot has hit the target
    public void setHit(int hit) {
        this.hit = hit;
    }

    // EFFECTS: sets the total number of times the user has taken a shot
    public void setShots(int shots) {
        this.shots = shots;
    }

    // EFFECTS: gets the last suggestion made by the system
    public Suggestion getLastSuggestion() {
        return allSuggestions.get(allSuggestions.size() - 1);
    }

    // EFFECTS: gets the average deviance from all the user's missed shots to the target in the x direction
    public double getSummarizedX() {
        double totalX = 0;
        for (Suggestion s : allSuggestions) {
            if (s.getDirX().equals("right")) {
                totalX += s.getAmtX();
            } else {
                totalX -= s.getAmtX();
            }
        }

        double avgX = totalX / (double) (allSuggestions.size());
        return avgX;
    }

    // EFFECTS: gets the average deviance from all the user's missed shots to the target in the y direction
    public double getSummarizedY() {
        double totalY = 0;
        for (Suggestion s : allSuggestions) {
            if (s.getDirY().equals("up")) {
                totalY = s.getAmtY();
            } else {
                totalY -= s.getAmtY();
            }
        }

        double avgY = totalY / (double) (allSuggestions.size());
        return avgY;
    }

    // EFFECTS: create summary suggestion from all suggestions given this session
    public Suggestion updateSummarySuggestion() {
        double avgX = getSummarizedX();
        double avgY = getSummarizedY();

        String dirX = "perfect";
        String dirY = "perfect";

        if (avgX > 0) {
            dirX = "right";
        } else if (avgX < 0) {
            dirX = "left";
        }

        if (avgY > 0) {
            dirY = "up";
        } else if (avgY < 0) {
            dirY = "down";
        }

        Suggestion summary = new Suggestion(avgX, avgY, dirX, dirY, Math.abs(avgX), Math.abs(avgY));

        return summary;
    }

    // EFFECTS: retrieves session number of current session
    public int getSessionNum() {
        return sessionNum;
    }

    // EFFECTS: retrieves session type of current session
    public String getSessionType() {
        return SESSION_TYPE;
    }

    // MODIFIES: this
    // EFFECTS: adds in a pre-existing Suggestion to list of all suggestions
    public void addSuggestion(Suggestion suggestion) {
        allSuggestions.add(suggestion);
    }

    // MODIFIES: this
    // EFFECTS: updates the JSONObject with the current session if it is new, or deleting the old version of an old
    // session if the current session is a continuation of an old session
    @Override
    public JSONObject toJson() {
        if (pastSessions.length() == 0) {
            pastSessions = new JSONObject();
        } else if (pastSessions.length() >= sessionNum) {
            pastSessions.remove("Session " + sessionNum);
        }

        pastSessions.put("Session " + getSessionNum(), sessionPropertiesToJson());
        return pastSessions;
    }

    // MODIFIES: this
    // EFFECTS: reads in past sessions from file (not including this current session) into a JSONObject for ease of
    // updating the object if current session is saved
    public void addOldSessionsToPastSessions(JsonReader reader) throws IOException {
        pastSessions = reader.retrieveOldSessions();
    }

    // EFFECTS: turns session properties in this session to a JSONArray and returns it
    public JSONObject sessionPropertiesToJson() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("Target Type", "circle");
        jsonObject.put("Target Distance", distance);
        jsonObject.put("Target X", getTarget().getCenterX());
        jsonObject.put("Target Y", getTarget().getCenterY());
        jsonObject.put("Hits", hit);
        jsonObject.put("Shots", shots);
        jsonObject.put("Suggestions", suggestionsToJson());

        return jsonObject;
    }

    // EFFECTS: returns suggestions in this session as a JSON array
    private JSONArray suggestionsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Suggestion s : allSuggestions) {
            jsonArray.put(s.toJson());
        }

        return jsonArray;
    }
}
