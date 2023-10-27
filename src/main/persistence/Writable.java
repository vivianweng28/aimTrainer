package persistence;

import org.json.JSONObject;

// "Code influenced by the JsonSerizalizationDemo link_to_demo
// represents anything that can be written into a Json file
public interface Writable {

    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
