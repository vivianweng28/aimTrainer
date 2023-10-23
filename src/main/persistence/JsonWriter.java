package persistence;

import model.Session;
import org.json.JSONArray;
import org.json.JSONObject;
import ui.AimTrainer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// "Code influenced by the JsonSerizalizationDemo link_to_demo
// Represents a writer that writes JSON representation of session to file
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of session to file
    public void write(AimTrainer a) {
        JSONArray json = a.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}

