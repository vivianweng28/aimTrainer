package ui;

import model.*;
import model.Session;
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
    private static final int DIM_X = 600;
    private static final int DIM_Y = 600;
    private static final Scanner SCN = new Scanner(System.in);
    private Target target;
    private Session currentSession;
    private boolean oldSession;
    private boolean over;
    private MainGUI mg;

    // private static final String DEFAULT_MODE = "circle";
    // private String mode;  ADD BACK IN IF ADD HUMAN SHAPED TARGET

    // EFFECT: create aim trainer that is programmed to continue running, has a default distance, and has a dummy
    // target
    public AimTrainer() {
        this.stop = false;
        //this.mode = DEFAULT_MODE;
        target = new Target(0,0);
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
        oldSession = false;
        over = false;
        loadSessions();
    }

    // MODIFIES: this
    // EFFECTS: adds a mainGUI to aimtrainer
    public void addGUI(MainGUI mainGUI) {
        mg = mainGUI;
    }

//    // MODIFIES: this
//    // EFFECTS: starts the aim trainer program, asks if distance is changed, prints out current distance from target,
//    // generates a random target, and adds new session to list of sessions
//    public void start() {
//        askSession(); // ask if load old session or run new session
//        while (!stop) {
//            askChangeDist();
//            if (oldSession) {
//                target = currentSession.getTarget();
//            } else {
//                target = generateTarget();
//            }
//            System.out.println("Current distance from target: " + currentSession.getDistance() + "m");
//            runGame();
//        }
//    }

//    // MODIFIES: this
//    // EFFECTS: gets shot input from user, records suggestion if did not hit target, asks if the user wants to
//    // continue the session after every shot. If the user ends the session, session feedback is given. For CONSOLE
//    public void runGame() {
//        boolean hit = false;
//        while (!hit) {
//            double x = getXFromUser();
//            double y = getYFromUser();
//            hit = target.hitTarget((int) x, (int) y);
//            currentSession.analyze((int) x, (int) y, target.getCenterX(), target.getCenterY(),
//            target.getRadius(), hit);
//            if (hit) {
//                System.out.println("Target hit!");
//                currentSession.hit();
//                generateTarget();
//            } else {
//                System.out.println("Target not hit! Keep trying!");
//                System.out.println("Immediate Feedback: " + currentSession.getLastSuggestion().giveSuggestion());
//            }
//            System.out.println("Continue the session? (N to stop, anything else to continue)");
//            SCN.nextLine();
//            String cont = SCN.nextLine();
//            if (cont.equals("N")) {
//                hit = true;
//                stop = true;
//                doNotContinue();
//            }
//        }
//    }

    // EFFECTS: returns a list of all the saved sessions
    public List<Session> getAllSessions() {
        return sessions;
    }

    // MODIFIES: this
    // EFFECTS: for GUI based program. Receives an input of a user's shot, records whether the shot hit the target, and
    // create feedback based on the shots. If the target was hit, a new target is randomly generated.
    public void update(int x, int y) {
        boolean hit = target.hitTarget(x, y);
        currentSession.analyze(x, y, target.getCenterX(), target.getCenterY(), target.getRadius(), hit);
        if (hit) {
            currentSession.hit();
            generateTarget();
        }
        mg.immediateFeedback(currentSession);
    }

//    // MODIFIES: this
//    // EFFECTS: when the session is over, asks if user wants to see statistics from current session,
//    see statistics from
//    // past sessions, or save the current session. Thanks the user for using the aim trainer.
//    public void doNotContinue() {
//        System.out.println("Session feedback: " + currentSession.updateSummarySuggestion().giveSuggestion());
//        askSeeCurrentStats();
//        askSeeAllStatistics();
//        askSave();
//        System.out.println("Thank you for training with us today!");
//        over = true;
//    }

    // EFFECTS: returns the DIM_X of the game.
    public int getDimX() {
        return DIM_X;
    }

    // EFFECTS: returns the DIM_Y of the game.
    public int getDimY() {
        return DIM_Y;
    }

//    // EFFECTS: asks the user if they want to see the statistics from this session. If so, displays the statistics
//    public void askSeeCurrentStats() {
//        System.out.println("Would you like to see your statistics for this session? (\"Y\" to view)");
//        String answer = SCN.nextLine();
//        if (answer.equals("Y")) {
//            Suggestion summary = currentSession.updateSummarySuggestion();
//            System.out.println("Current session feedback: " + summary.giveSuggestion());
//            System.out.println("Total accuracy: " + currentSession.getAccuracy() + "%");
//            List<Suggestion> allSuggestions = currentSession.getAllSuggestions();
//            int count = 1;
//            for (Suggestion s : allSuggestions) {
//                System.out.println("Shot " + count + ": x = " + s.getCompX() + ", y = " + s.getCompY());
//                System.out.println("Shot " + count + " suggestion: " + s.giveSuggestion());
//                count++;
//            }
//            System.out.println();
//        }
//    }

    // EFFECTS: returns session at the specified index
    public Session getSession(int i) {
        return sessions.get(i);
    }

    // EFFECTS: asks user if they want to see past records, and shows record for the requested session, including the
    // total accuracy rate, each shot the user took in that session, and all the suggestions given for each shot.
    // Thanks the user for training with the program.
    public void askSeeAllStatistics() {
        System.out.println("Would you like to see past sessions? (Y for yes, anything else for no)");
        System.out.println("Note that any changes made to past sessions will be reflected here, but will not be saved");
        System.out.println("unless manually saved.");
        String nextRecords = SCN.nextLine();
        if (nextRecords.equals("Y")) {
            if (sessions.size() == 0) {
                System.out.println("Oops, you do not have any saved past sessions");
            } else {
                System.out.println("Which session number would you like to see? You have " + sessions.size()
                        + " saved sessions.");
                int indexPlusOne = SCN.nextInt();
                SCN.nextLine();
                if (indexPlusOne > sessions.size() || indexPlusOne < 1) {
                    System.out.println("Invalid index number, session closing. Thank you for training with us today!");
                } else {
                    displayPastSession(indexPlusOne);
                }
            }
        }
    }

    // EFFECTS: displays the statistics of past session.
    public void displayPastSession(int indexPlusOne) {
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

    // EFFECTS: asks user if they want to save the session. If so, saves the session.
    public void askSave() {
        System.out.println("Would you like to save the current session? (Y to save, anything else to not save)");
        String save = SCN.nextLine();

        if (save.equals("Y")) {
            saveSessions();
        }
    }

    // EFFECTS: returns the target of the game.
    public Target getTarget() {
        return target;
    }

    public void setToOldSession(int i) {
        oldSession = true;
        currentSession = sessions.get(i);
        currentSession.getTarget().changeDist(currentSession.getDistance());
        try {
            currentSession.addOldSessionsToPastSessions(jsonReader);
        } catch (IOException e) {
            throw new RuntimeException();
        }
        target = currentSession.getTarget();
    }

//    // MODIFIES: this
//    // EFFECTS: asks if the user wants to start a new session or load old session, loads old session if directed
//    // to do so by the user
//    public void askSession() {
//        String newSession = askNewSession();
//        int sessionNum = sessions.size() + 1;
//        if (! newSession.equals("N")) {
//            oldSession = true;
//            System.out.println("Please enter the session number of the past session you would like to open");
//            sessionNum = getNextInt();
//            if (sessionNum <= sessions.size() && sessionNum > 0) {
//                currentSession = sessions.get(sessionNum - 1);
//                System.out.println("Loaded Session " + currentSession.getSessionNum() + " from " + JSON_STORE);
//            } else {
//                fileDoesNotExist();
//            }
//        } else {
//            currentSession = new Session(sessionNum);
//        }
//        System.out.println("This is session " + currentSession.getSessionNum());
//        try {
//            currentSession.addOldSessionsToPastSessions(jsonReader);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (NullPointerException n) {            //it's ok if there's nothing in file
//        }
//    }

    // MODIFIES: this
    // EFFECTS: creates a new session for the gui
    public void newSession() {
        currentSession = new Session(sessions.size() + 1);
        try {
            currentSession.addOldSessionsToPastSessions(jsonReader);
        } catch (IOException e) {
            throw new RuntimeException();
        }
        EventLog.getInstance().logEvent(new Event("A new session is created: session " + (sessions.size() + 1)));
        generateTarget();
    }

    // MODIFIES: this
    // EFFECTS: sets current session to a specified saved session
    public void setCurrentSession(int i) {
        EventLog.getInstance().logEvent(new Event("A saved session, session " + (i + 1)
                + " is retrieved and opened."));
        try {
            setToOldSession(i);
        } catch (IndexOutOfBoundsException e) {
            // nothing
        }
        currentSession = sessions.get(i);
    }

    // EFFECTS: returns the number of saved sessions
    public int getNumSessions() {
        return sessions.size();
    }

    // EFFECTS: gets the next integer user inputs
    public int getNextInt() {
        int answer = SCN.nextInt();
        SCN.nextLine();

        return answer;
    }

    // EFFECTS: asks user if they want to load an old training session or start a new session
    public String askNewSession() {
        System.out.println("Would you like to load an old training session or start a new session? "
                +  "(N for new session, anything else for old session)");
        return SCN.nextLine();
    }

    // MODIFIES: this
    // EFFECTS: informs user that file they requested does not exist, so the program is starting a new session for them,
    // updates oldSession to false
    public void fileDoesNotExist() {
        System.out.println("That file does not exist! Starting new session!");
        currentSession = new Session(sessions.size() + 1);
        oldSession = false;
    }

    // MODIFIES: this
    // EFFECTS: asks the user if they want to change the distance they are shooting from, if so, changes
    // the distance accordingly
    public void askChangeDist() {
        System.out.println("Would you like to change the distance you are shooting from? (\"Y\" to change)");
        String changeDist = SCN.nextLine();
        if (changeDist.equals("Y")) {
            System.out.println("Please enter an integer distance (in meters) from 1 to 500");
            int newDist = SCN.nextInt();
            SCN.nextLine();
            if (newDist >= 1 && newDist <= 500) {
                currentSession.setDistance(newDist);
            } else {
                System.out.println("Invalid distance");
                askChangeDist();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: changes the distance of the session and the target
    public void changeDist(int dist) {
        currentSession.setDistance(dist);
        target.changeDist(dist);
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

        target = new Target((int)x,(int)y);

        currentSession.setTarget(target);
        target.changeDist(currentSession.getDistance());

        return target;
    }

    public void delete(int i) {
        sessions.remove(i);
    }

    // EFFECTS: saves all sessions to file
    public void saveSessions() {
        sessions.add(currentSession);
        try {
            jsonWriter.open();
            jsonWriter.write(currentSession);
            jsonWriter.close();
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

    // EFFECTS: returns the current session
    public Session getCurrentSession() {
        return currentSession;
    }
}