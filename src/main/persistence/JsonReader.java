package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import model.Session;
import model.Suggestion;
import model.Target;
import org.json.*;

// "Code influenced by the JsonSerizalizationDemo link_to_demo
// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads past sessions from file and returns it as a list of Sessions
    // throws IOException if an error occurs reading data from file
    public List<Session> read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseSession(jsonObject);
    }

    // EFFECTS: reads past sessions from file and returns it in the format of a JSONObject
    // throws IOException if an error occurs reading data from file
    public JSONObject retrieveOldSessions() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return jsonObject;
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses qualities of a session from JSON object and returns it
    private List<Session> parseSession(JSONObject jsonObject) {
        List<Session> allSessions = new ArrayList<Session>();
        Session s;

        for (int i = 1; i <= jsonObject.length(); i++) {
            JSONObject currentSession = jsonObject.getJSONObject("Session " + i); // get the JSONObject that holds
                                                                                      // each session
            String targetType = currentSession.getString("Target Type"); // get target type of selected session

            int targetDist = currentSession.getInt("Target Distance");

            int targetX = currentSession.getInt("Target X");

            int targetY = currentSession.getInt("Target Y");

            int shots = currentSession.getInt("Shots");

            int hit = currentSession.getInt("Hits");

 //           if (targetType.equals("circle")) { // create circle session
            s = new Session(i);
            s.setOldDistance(targetDist);
            s.setOldTarget(new Target(targetX, targetY));
            s.setHit(hit);
            s.setShots(shots);

            allSessions.add(s);
  //          } else {
  //              s = new CircleSession(i);
  //             allSessions.add(s); //CHANGE TO OTHER TARGET TYPE LATER
  //          }

            addAllSuggestions(s, currentSession); // add in suggestions for selected session
        }

        return allSessions;
    }

    // MODIFIES: s
    // EFFECTS: parses Suggestions from JSON object and adds them to Session
    private void addAllSuggestions(Session s, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("Suggestions");
        for (Object json : jsonArray) {
            JSONObject nextSuggestion = (JSONObject) json;
            addSuggestion(s, nextSuggestion);
        }
    }

    // MODIFIES: s
    // EFFECTS: parses all components of a Suggestion from JSON object and adds it to a Session
    private void addSuggestion(Session s, JSONObject jsonObject) {
        double shotX = jsonObject.getDouble("Shot X");
        double shotY = jsonObject.getDouble("Shot Y");
        String dirX = jsonObject.getString("Direction X");
        String dirY = jsonObject.getString("Direction Y");
        double amtX = jsonObject.getDouble("Error Amount X");
        double amtY = jsonObject.getDouble("Error Amount Y");
        Suggestion suggest = new Suggestion(shotX, shotY, dirX, dirY, amtX, amtY);
        s.addSuggestion(suggest);
    }
}
