package ui;

import model.*;
import model.CircleTarget;
import model.Session;
import model.CircleSession;
import model.Record;
import model.Target;

import java.util.Scanner;

// aim trainer object that produces the targets
public class AimTrainer {

    private boolean stop;
    private static final Record sessions = new Record();
    private static final int DIM_X = 500;
    private static final int DIM_Y = 500;
    private static final int DEFAULT_DISTANCE = 100; //meters
    private int distance;
    private static final Scanner SCN = new Scanner(System.in);
    private Target target;
    // private static final String DEFAULT_MODE = "circle";
    // private String mode;  ADD BACK IN IF ADD HUMAN SHAPED TARGET

    public AimTrainer() {
        this.stop = false;
        //this.mode = DEFAULT_MODE;
        this.distance = DEFAULT_DISTANCE;
        target = new CircleTarget();
    }

    public void start() {
        Session s = new CircleSession();
        sessions.addSession(s);
        while (!stop) {
            askChangeDist();
            target = generateTarget();
            runGame(s, target);
        }
    }

    public void runGame(Session s, Target target) {
        boolean hit = false;
        while (!hit) {
            double x = getXFromUser();
            double y = getYFromUser();
            hit = target.hitTarget(x, y);
            if (hit) {
                System.out.println("Target hit! Next target appearing");
            } else {
                System.out.println("Target not hit! Keep trying!");
                s.analyze(x, y, target.getCenterX(), target.getCenterY(), target.getRadius());
                System.out.println("Immediate Feedback: " + s.getLastSuggestion().giveSuggestion());
            }
            System.out.println("Continue the session? (N to stop, anything else to continue)");

            SCN.nextLine();
            String cont = SCN.nextLine();

            if (cont.equals("N")) {
                stop = true;
                System.out.println("Session feedback: " + s.updateSummarySuggestion().giveSuggestion());
                askSeeAllStatistics();
            }
        }
    }

    public void askSeeAllStatistics() {
        System.out.println("Would you like to see past records? (Y for yes, anything else for no)");
        String nextRecords = SCN.nextLine();
        if (nextRecords.equals("Y")) {
            System.out.println("Which session number would you like to see? Please enter a positive integer.");
            int indexPlusOne = SCN.nextInt();
            if (indexPlusOne >= sessions.getNumSessions() && indexPlusOne >= 1) {
                System.out.println("Invalid index number, session closing. Thank you for training with us today!");
            } else {
                Suggestion summary = sessions.getSession(indexPlusOne - 1).getSummarySuggestion();
                System.out.println("Session " + indexPlusOne + " feedback: " + summary.giveSuggestion());
            }
        } else {
            System.out.println("Thank you for training with us today!");
        }
    }

    public void askChangeDist() {
        System.out.println("Would you like to change the distance you are shooting from? (Y/N)");
        String changeDist = SCN.nextLine();
        if (changeDist.equals("Y")) {
            System.out.println("Please enter distance (in meters) from 1 to 500");
            distance = SCN.nextInt();
        }
    }

    public double getXFromUser() {
        System.out.println("Please enter the x coordinate");
        return SCN.nextDouble();
    }

    public double getYFromUser() {
        System.out.println("Please enter the y coordinate");
        return SCN.nextDouble();
    }

    public Target generateTarget() {
        double x = 0;
        double y = 0;

        target.changeDist(distance);

        while (!(x >= target.getRadius()) || !(x <= (DIM_X - target.getRadius()))) {
            x = Math.random() * DIM_X;
        }

        while (!(y >= target.getRadius()) || !(y <= (DIM_Y - target.getRadius()))) {
            y = Math.random() * DIM_Y;
        }

        target = new CircleTarget(x,y);

        return target;
    }


}