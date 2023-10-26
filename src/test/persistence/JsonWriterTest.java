package persistence;

import model.CircleSession;
import model.Session;
import model.Suggestion;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest extends JsonTest {
    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.

    List<Session> sessions;

    @BeforeEach
    void runBefore() {
        sessions = new ArrayList<Session>();
    }

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptySession() {
        try {
            Session s = new CircleSession(2);
            JsonReader reader = new JsonReader("./data/testWriterEmptySession.json");
            s.addOldSessionsToPastSessions(reader);
            JsonWriter writer = new JsonWriter("./data/testWriterEmptySession.json");
            writer.open();
            writer.write(s);
            writer.close();

            sessions = reader.read();
            List <Suggestion> s2Suggestions = sessions.get(1).getAllSuggestions();
            assertEquals(2, sessions.size());
            checkSessionNumAndType(2, "circle", s);
            assertEquals(0, s2Suggestions.size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroomAddNewSession() {
        try {
            Session s = new CircleSession(2);
            Suggestion first = new Suggestion(10, 10, "right", "up", 2, 2);
            Suggestion second = new Suggestion(11, 11, "right", "up", 1, 1);
            s.addSuggestion(first);
            s.addSuggestion(second);

            JsonReader reader = new JsonReader("./data/testWriterGeneralSession.json");
            s.addOldSessionsToPastSessions(reader);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralSession.json");
            writer.open();
            writer.write(s);
            writer.close();

            sessions = reader.read();
            assertEquals(2, sessions.size());
            checkSessionNumAndType(1, "circle", sessions.get(0));
            List <Suggestion> s1Suggestions = sessions.get(0).getAllSuggestions();
            checkSuggestion(50,50,"right", "up", 1, 1, s1Suggestions.get(0));
            checkSuggestion(51,51,"perfect", "perfect", 0, 0, s1Suggestions.get(1));

            checkSessionNumAndType(2, "circle", sessions.get(1));
            List <Suggestion> s2Suggestions = sessions.get(1).getAllSuggestions();
            checkSuggestion(10, 10, "right", "up", 2, 2, s2Suggestions.get(0));
            checkSuggestion(11, 11, "right", "up", 1, 1, s2Suggestions.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroomAddNewSessionReplace() {
        try {
            JsonReader reader = new JsonReader("./data/testWriterGeneralSessionReplace.json");
            sessions = reader.read();
            Session s = sessions.get(0);
            s.addOldSessionsToPastSessions(reader);
            Suggestion first = new Suggestion(10, 10, "right", "up", 2, 2);
            Suggestion second = new Suggestion(11, 11, "right", "up", 1, 1);
            s.addSuggestion(first);
            s.addSuggestion(second);
            Suggestion origFirst = new Suggestion(50,50,"right", "up", 1, 1);
            Suggestion origSecond = new Suggestion(51,51,"perfect", "perfect", 0, 0);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralSessionReplace.json");
            writer.open();
            writer.write(s);
            writer.close();

            sessions = reader.read();
            assertEquals(1, sessions.size());
            checkSessionNumAndType(1, "circle", sessions.get(0));
            List <Suggestion> s1Suggestions = sessions.get(0).getAllSuggestions();
            checkSuggestion(50,50,"right", "up", 1, 1, s1Suggestions.get(0));
            checkSuggestion(51,51,"perfect", "perfect", 0, 0, s1Suggestions.get(1));
            checkSuggestion(10, 10, "right", "up", 2, 2, s1Suggestions.get(2));
            checkSuggestion(11, 11, "right", "up", 1, 1, s1Suggestions.get(3));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}