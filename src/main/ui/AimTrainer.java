package ui;

import model.*;
import model.CircleTarget;
import model.Session;
import model.CircleSession;
import model.Target;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// represents an aim trainer program that produces the targets
public class AimTrainer {

    private static final String JSON_STORE = "./data/session.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private boolean stop;
    private static final List<Session> sessions = new ArrayList<Session>();
    private static final int DIM_X = 500;
    private static final int DIM_Y = 500;
    private static final int DEFAULT_DISTANCE = 100; //meters
    private int distance;
    private static final Scanner SCN = new Scanner(System.in);
    private Target target;
    private Session currentSession;
    private JSONArray allSessions;

    // private static final String DEFAULT_MODE = "circle";
    // private String mode;  ADD BACK IN IF ADD HUMAN SHAPED TARGET

    // EFFECT: create aim trainer that is programmed to continue running, has a default distance, and has a dummy
    // target
    public AimTrainer() {
        this.stop = false;
        //this.mode = DEFAULT_MODE;
        this.distance = DEFAULT_DISTANCE;
        target = new CircleTarget(0,0);
        currentSession = new CircleSession(sessions.size() + 1);
        allSessions = new JSONArray();
    }

    // MODIFIES: this
    // EFFECTS: starts the aim trainer program, asks if distance is changed, prints out current distance from target,
    // generates a random target, and adds new session to list of sessions
    public void start() {
        sessions.add(s);
        while (!stop) {
            askChangeDist();
            target = generateTarget();
            System.out.println("Current distance from target: " + distance + "m");
            runGame(s, target);
        }
    }

    // MODIFIES: this
    // EFFECTS: gets shot input from user, records suggestion if did not hit target, asks if the user wants to
    // continue the session after every shot. If the user ends the session, session feedback is given.
    public void runGame(Session s, Target target) {
        boolean hit = false;
        while (!hit) {
            double x = getXFromUser();
            double y = getYFromUser();
            hit = target.hitTarget(x, y);
            if (hit) {
                System.out.println("Target hit! Next target appearing");
                s.hit();
            } else {
                System.out.println("Target not hit! Keep trying!");
                s.analyze(x, y, target.getCenterX(), target.getCenterY(), target.getRadius());
                System.out.println("Immediate Feedback: " + s.getLastSuggestion().giveSuggestion());
            }
            System.out.println("Continue the session? (N to stop, anything else to continue)");

            SCN.nextLine();
            String cont = SCN.nextLine();

            if (cont.equals("N")) {
                hit = true;
                stop = true;
                System.out.println("Session feedback: " + s.updateSummarySuggestion().giveSuggestion());
                askSeeAllStatistics();
            }
        }
    }

    // EFFECTS: asks user if they want to see past records, and shows record for the requested session, including the
    // total accuracy rate, each shot the user took in that session, and all the suggestions given for each shot.
    // Thanks the user for training with the program.
    public void askSeeAllStatistics() {
        System.out.println("Would you like to see past records? (Y for yes, anything else for no)");
        String nextRecords = SCN.nextLine();
        if (nextRecords.equals("Y")) {
            System.out.println("Which session number would you like to see? Please enter a positive integer.");
            int indexPlusOne = SCN.nextInt();
            if (indexPlusOne >= sessions.size() && indexPlusOne < 1) {
                System.out.println("Invalid index number, session closing. Thank you for training with us today!");
            } else {
                Session sessionSelected = sessions.get(indexPlusOne - 1);
                Suggestion summary = sessionSelected.updateSummarySuggestion();
                System.out.println("Session " + indexPlusOne + " feedback: " + summary.giveSuggestion());
                System.out.println("Total accuracy: " + sessionSelected.getAccuracy() + "%");
                List<Suggestion> allSuggestions = sessionSelected.getAllSuggestions();
                int count = 1;
                for (Suggestion s : allSuggestions) {
                    System.out.println("Shot " + count + ": x = " + s.getCompX() + ", y = " + s.getCompY());
                    System.out.println("Shot " + count + " suggestion: " + s.giveSuggestion());
                    count++;
                }
            }
        }
        System.out.println("Thank you for training with us today!");
    }

    // MODIFIES: this
    // EFFECTS: asks the user if they want to change the distance they are shooting from, if so, changes
    // the distance accordingly

    public void askChangeDist() {
        System.out.println("Would you like to change the distance you are shooting from? (Y/N)");
        String changeDist = SCN.nextLine();
        if (changeDist.equals("Y")) {
            System.out.println("Please enter distance (in meters) from 1 to 500");
            distance = SCN.nextInt();
        }
    }

    // EFFECTS: gets the x coordinate of the user shot from user
    public double getXFromUser() {
        System.out.println("Please enter the x coordinate");
        return SCN.nextDouble();
    }

    // EFFECTS: gets the y coordinate of the user shot from user
    public double getYFromUser() {
        System.out.println("Please enter the y coordinate");
        return SCN.nextDouble();
    }

    // MODIFIES: this
    // EFFECTS: generates a new target for the aim training system
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

    public JSONArray toJson() {
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(currentSession.toJson());
        json.put("Sessions", jsonArray);
        return json;
    }

    // EFFECTS: saves the workroom to file
    private void saveWorkRoom() {
        Session current = sessions.get(sessions.size() - 1);
        try {
            jsonWriter.open();
            jsonWriter.write(current);
            jsonWriter.close();
            System.out.println("Saved Session" + current.getSessionNum() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads workroom from file
    private void loadWorkRoom() {
        try {
            currentSession = jsonReader.read();
            System.out.println("Loaded Session" + currentSession.getSessionNum() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}