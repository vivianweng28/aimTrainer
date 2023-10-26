package persistence;

import model.Session;
import model.Suggestion;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            List<Session> sessions = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyRecord() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyRecord.json");
        try {
            List<Session> sessions = reader.read();
            assertEquals(1, sessions.size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralRecord() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralSessions.json");
        try {
            List<Session> sessions = reader.read();
            assertEquals(2, sessions.size());
            List<Suggestion> suggestions1 = sessions.get(0).getAllSuggestions();
            List<Suggestion> suggestions2 = sessions.get(1).getAllSuggestions();
            checkSessionNumTypeDistanceAndTarget(1, "circle", 100, 49, 49, sessions.get(0));
            assertEquals(2, suggestions1.size());
            checkSuggestion(50,50,"right", "up", 1, 1, suggestions1.get(0));
            checkSuggestion(49,49,"perfect", "perfect", 0, 0, suggestions1.get(1));
            checkSessionNumTypeDistanceAndTarget(2, "circle", 100, 39, 39, sessions.get(1));
            assertEquals(1, suggestions2.size());
            checkSuggestion(40,40,"right", "up", 1, 1, suggestions2.get(0));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}