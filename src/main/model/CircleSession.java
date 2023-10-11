package model;

import java.util.ArrayList;
import java.util.List;

public class CircleSession implements Session {
    private List<Suggestion> allSuggestions;
    private Suggestion summarySuggestion;
    private int hit;
    private int shots;
    private double accuracy;

    public CircleSession() {
        allSuggestions = new ArrayList<Suggestion>();
        summarySuggestion = null;
        hit = 0;
        shots = 0;
        accuracy = 0;
    }

    // REQUIRES: x, y >= 0, centerX, centerY, and radius > 0, point (x,y) is not in the circular target with center at
    // (centerX, centerY) and a radius of radius.
    // MODIFIES: this
    // EFFECTS: compares shot that missed the target taken by player to the closest shot on the target and generates
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

    // EFFECTS: generates closest shot on the target from the user shot
    public Shot getClosestShot(Vector vector, double radius, double centerX, double centerY) {
        double x = centerX + radius * vector.getUnitVector().getCompX();
        double y = centerY + radius * vector.getUnitVector().getCompY();
        Shot s = new Shot(x, y);
        return s;
    }

    public void hit() {
        hit++;
        shots++;
        accuracy = (double) hit / (double) shots;
    }

    public List<Suggestion> getAllSuggestions() {
        return allSuggestions;
    }

    public double getAccuracy() {
        return accuracy * 100;
    }

    public int getHit() {
        return hit;
    }

    public int getShots() {
        return shots;
    }

    public Suggestion getLastSuggestion() {
        return allSuggestions.get(allSuggestions.size() - 1);
    }

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
}
