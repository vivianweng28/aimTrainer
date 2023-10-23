package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import model.CircleSession;
import model.Session;
import model.Suggestion;
import org.json.*;

// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Session read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseSession(jsonObject);
    }

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

    // EFFECTS: parses session from JSON object and returns it
    private Session parseSession(JSONObject jsonObject) {
        int number = jsonObject.getInt("Session Number");
        String targetType = jsonObject.getString("Target Type");
        Session s;
        if (targetType.equals("circle")) {
            s = new CircleSession(number);
        } else {
            s = new CircleSession(number); //CHANGE TO OTHER TARGET TYPE LATER
        }
        return s;
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

    // MODIFIES: wr
    // EFFECTS: parses thingy from JSON object and adds it to workroom
    private void addSuggestion(Session s, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
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
