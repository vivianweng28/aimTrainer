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
    void analyze(double x, double y, double centerX, double centerY, double radius);

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

    int getSessionNum();

    String getSessionType();

    JSONObject toJson();

    void addOldSessionsToPastSessions(JsonReader reader) throws IOException;


}
