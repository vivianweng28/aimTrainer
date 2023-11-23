package model;

import java.io.IOException;
import java.util.List;
import org.json.JSONObject;
import persistence.JsonReader;

// represents an aim training session
public interface Session {

    // REQUIRES: x, y >= 0, centerX, centerY, and radius > 0, point (x,y) is not in the target with center at
    // (centerX, centerY) and a radius of length radius.
    // MODIFIES: this
    // EFFECTS: generates suggestion based off of a user's shot that did not hit the target and adds suggestion
    // to list of all suggestions generated in this session
    void analyze(int x, int y, double centerX, double centerY, double radius);

    // MODIFIES: this
    // EFFECTS: adds in a pre-existing Suggestion to list of all suggestions
    void addSuggestion(Suggestion s);

    // EFFECTS: gets the last suggestion made by the system
    Suggestion getLastSuggestion();

    // EFFECTS: create summary suggestion from all suggestions given this session
    Suggestion updateSummarySuggestion();

    // EFFECTS: gets the list of all generated suggestions
    List<Suggestion> getAllSuggestions();

    // MODIFIES: this
    // EFFECTS: increases the number of times the user's shot hits the target by one, increases the total number of
    // shots by one, and calculates the new accuracy
    void hit();

    // EFFECTS: gets the total session accuracy in percentage
    double getAccuracy();

    // EFFECTS: gets the session number of this session
    int getSessionNum();

    // EFFECTS: gets the session type of the session
    String getSessionType();

    // MODIFIES: this
    // EFFECTS: updates the JSONObject with the current session if it is new, or deleting the old version of an old
    // session if the current session is a continuation of an old session
    JSONObject toJson();

    // MODIFIES: this
    // EFFECTS: reads in past sessions from file (not including this current session) into a JSONObject for ease of
    // updating the object if current session is saved
    void addOldSessionsToPastSessions(JsonReader reader) throws IOException;

    // EFFECTS: retrieves the distance from target
    int getDistance();

    // MODIFIES: this
    // EFFECTS: changes the distance from target
    void setDistance(int newDistance);

    // MODIFIES: this
    // EFFECTS: changes current target to a new target
    void setTarget(Target t);

    // EFFECTS: retrieves the current target
    Target getTarget();

    // EFFECTS: gets the total number of times the user's shot has hit the target
    int getHit();

    // EFFECTS: gets the total number of times the user has taken a shot
    int getShots();

    // EFFECTS: sets the total number of times the user's shot has hit the target
    void setHit(int hit);

    // EFFECTS: sets the total number of times the user has taken a shot
    void setShots(int shots);
}
