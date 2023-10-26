package ui;

import model.*;
import model.CircleTarget;
import model.Session;
import model.CircleSession;
import model.Target;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// represents an aim trainer program that produces the targets
public class AimTrainer {

    private static final String JSON_STORE = "./data/record.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private boolean stop;
    private List<Session> sessions = new ArrayList<Session>();
    private static final int DIM_X = 500;
    private static final int DIM_Y = 500;
    private static final Scanner SCN = new Scanner(System.in);
    private Target target;
    private Session currentSession;
    private boolean oldSession;

    // private static final String DEFAULT_MODE = "circle";
    // private String mode;  ADD BACK IN IF ADD HUMAN SHAPED TARGET

    // EFFECT: create aim trainer that is programmed to continue running, has a default distance, and has a dummy
    // target
    public AimTrainer() {
        this.stop = false;
        //this.mode = DEFAULT_MODE;
        target = new CircleTarget(0,0);
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
        oldSession = false;
    }

    // MODIFIES: this
    // EFFECTS: starts the aim trainer program, asks if distance is changed, prints out current distance from target,
    // generates a random target, and adds new session to list of sessions
    public void start() {
        askSession(); // ask if load old session or run new session
        while (!stop) {
            askChangeDist();
            if (oldSession) {
                target = currentSession.getTarget();
            } else {
                target = generateTarget();
            }
            System.out.println("Current distance from target: " + currentSession.getDistance() + "m");
            runGame(target);
        }
    }

    // MODIFIES: this
    // EFFECTS: gets shot input from user, records suggestion if did not hit target, asks if the user wants to
    // continue the session after every shot. If the user ends the session, session feedback is given.
    public void runGame(Target target) {
        boolean hit = false;
        while (!hit) {
            double x = getXFromUser();
            double y = getYFromUser();
            hit = target.hitTarget(x, y);
            if (hit) {
                System.out.println("Target hit!");
                currentSession.hit();
                generateTarget();
            } else {
                System.out.println("Target not hit! Keep trying!");
                currentSession.analyze(x, y, target.getCenterX(), target.getCenterY(), target.getRadius());
                System.out.println("Immediate Feedback: " + currentSession.getLastSuggestion().giveSuggestion());
            }
            System.out.println("Continue the session? (N to stop, anything else to continue)");
            SCN.nextLine();
            String cont = SCN.nextLine();
            if (cont.equals("N")) {
                hit = true;
                stop = true;
                doNotContinue();
            }
        }
    }

    public void doNotContinue() {
        System.out.println("Session feedback: " + currentSession.updateSummarySuggestion().giveSuggestion());
        askSeeAllStatistics();
        askSave();
        System.out.println("Thank you for training with us today!");
    }

    // EFFECTS: asks user if they want to see past records, and shows record for the requested session, including the
    // total accuracy rate, each shot the user took in that session, and all the suggestions given for each shot.
    // Thanks the user for training with the program.
    public void askSeeAllStatistics() {
        System.out.println("Would you like to see past sessions? (Y for yes, anything else for no)");
        String nextRecords = SCN.nextLine();
        if (nextRecords.equals("Y")) {
            if (sessions.size() == 0) {
                System.out.println("Oops, you do not have any saved past sessions");
            } else {
                System.out.println("Which session number would you like to see? You have " + sessions.size()
                        + " saved sessions.");
                int indexPlusOne = SCN.nextInt();
                SCN.nextLine();
                if (indexPlusOne >= sessions.size() && indexPlusOne < 1) {
                    System.out.println("Invalid index number, session closing. Thank you for training with us today!");
                } else {
                    askPastSession(indexPlusOne);
                }
            }
        }
    }

    public void askPastSession(int indexPlusOne) {
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

    public void askSave() {
        System.out.println("Would you like to save the current session? (Y to save, anything else to not save)");
        String save = SCN.nextLine();

        if (save.equals("Y")) {
            saveSessions();
        }
    }

    public void askSession() {
        String newSession = askNewSession();
        loadSessions();
        int sessionNum = sessions.size() + 1;
        if (! newSession.equals("N")) {
            oldSession = true;
            System.out.println("Please enter the session number of the past session you would like to open");
            sessionNum = SCN.nextInt();
            SCN.nextLine();
            if (sessionNum <= sessions.size() && sessionNum > 0) {
                currentSession = sessions.get(sessionNum - 1);
                System.out.println("Loaded Session" + currentSession.getSessionNum() + " from " + JSON_STORE);
            } else {
                fileDoesNotExist();
            }
        } else {
            currentSession = new CircleSession(sessionNum);
        }
        System.out.println("This is session " + sessionNum);

        try {
            currentSession.addOldSessionsToPastSessions(jsonReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException n) {            //it's ok if there's nothing in file
        }
    }

    public String askNewSession() {
        System.out.println("Would you like to load an old training session or start a new session? "
                +  "(N for new session, anything else for old session");
        return SCN.nextLine();
    }

    public void fileDoesNotExist() {
        System.out.println("That file does not exist! Starting new session!");
        currentSession = new CircleSession(sessions.size() + 1);
        oldSession = false;
    }

    // MODIFIES: this
    // EFFECTS: asks the user if they want to change the distance they are shooting from, if so, changes
    // the distance accordingly

    public void askChangeDist() {
        System.out.println("Would you like to change the distance you are shooting from? (Y/N)");
        String changeDist = SCN.nextLine();
        if (changeDist.equals("Y")) {
            System.out.println("Please enter distance (in meters) from 1 to 500");
            currentSession.setDistance(SCN.nextInt());
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

        target.changeDist(currentSession.getDistance());

        while (!(x >= target.getRadius()) || !(x <= (DIM_X - target.getRadius()))) {
            x = Math.random() * DIM_X;
        }

        while (!(y >= target.getRadius()) || !(y <= (DIM_Y - target.getRadius()))) {
            y = Math.random() * DIM_Y;
        }

        target = new CircleTarget(x,y);

        currentSession.setTarget(target);

        return target;
    }

    // EFFECTS: saves all sessions to file
    private void saveSessions() {
        try {
            jsonWriter.open();
            jsonWriter.write(currentSession);
            jsonWriter.close();
            System.out.println("Saved Session" + currentSession.getSessionNum() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read old data: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads all past files from file
    private void loadSessions() {
        try {
            sessions = jsonReader.read();
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}