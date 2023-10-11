package model;

import java.util.ArrayList;
import java.util.List;

public class CircleSession implements Session {
    private List<Suggestion> allSuggestions;
    private Suggestion summarySuggestion;

    public CircleSession() {
        allSuggestions = new ArrayList<Suggestion>();
        summarySuggestion = null;
    }

    public void analyze(double x, double y, double centerX, double centerY, double radius) {
        Vector vec = new Vector(x - radius, y - radius);

        Shot closestShot = getClosestShot(vec, radius, centerX, centerY);

        double adjustX = x - closestShot.getDirX();
        double adjustY = y - closestShot.getDirY();

        String dirX = "perfect";
        String dirY = "perfect";

        if(adjustX > 0) {
            dirX = "right";
        } else if(adjustX < 0) {
            dirX = "left";
        }

        if(adjustY > 0) {
            dirY = "up";
        } else if(adjustY < 0) {
            dirY = "down";
        }

        adjustX = Math.abs(adjustX);
        adjustY = Math.abs(adjustY);

        Suggestion suggest = new Suggestion(x, y, dirX, dirY, adjustX, adjustY);

        allSuggestions.add(suggest);
    }

    public Shot getClosestShot(Vector vector, double radius, double centerX, double centerY) {
        double x = centerX + radius * vector.getUnitVector().getCompX();
        double y = centerY + radius * vector.getUnitVector().getCompY();
        Shot s = new Shot(x, y);
        return s;
    }

    public List<Suggestion> getAllSuggestions() {
        return allSuggestions;
    }

    public Suggestion getLastSuggestion() {
        return allSuggestions.get(allSuggestions.size() - 1);
    }

    public Suggestion getSummarySuggestion() {
        return summarySuggestion;
    }

    public Suggestion updateSummarySuggestion() {
        double totalX = 0;
        double totalY = 0;

        for (Suggestion s : allSuggestions) {
            totalX += s.getAmtX();
            totalY += s.getAmtY();
        }

        double avgX = totalX/(double) (allSuggestions.size());
        double avgY = totalY/(double) (allSuggestions.size());

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
            dirY = "right";
        }

        Suggestion summary = new Suggestion(avgX, avgY, dirX, dirY, avgX, avgY);

        return summary;
    }
}
