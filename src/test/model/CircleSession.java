package model;

import model.Suggestion;

import java.util.ArrayList;
import java.util.List;

public class CircleSession implements Session{
    private List<Suggestion> totalAccuracy;

    public CircleSession(){
        totalAccuracy = new ArrayList<Suggestion>();
    }

    public void analyze(double x, double y, double centerX, double centerY, double radius){
        double xAdjust = x - radius;
        double yAdjust = x - super.getRadius();

        String dirX = "perfect";
        String dirY = "perfect";

        if(xAdjust > 0){
            dirX = "right";
        } else if(xAdjust < 0) {
            dirX = "left";
        }

        if(yAdjust > 0){
            dirX = "up";
        } else if(yAdjust < 0) {
            dirX = "down";
        }

        xAdjust = Math.abs(xAdjust);
        yAdjust = Math.abs(yAdjust);

        Suggestion suggest = new Suggestion(x, y, dirX, dirY, xAdjust, yAdjust);

        totalAccuracy.add(suggest);
    }

    public Shot getClosestShot()
    {

    }

    public List<Suggestion> getTotalAccuracy(){
        return totalAccuracy;
    }
}
