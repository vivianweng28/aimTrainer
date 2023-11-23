package persistence;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// "Code influenced by the JsonSerizalizationDemo link_to_demo
// test for JsonWriter class
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
            s.setTarget(new Target(49,49));
            JsonReader reader = new JsonReader("./data/testWriterEmptySession.json");
            s.addOldSessionsToPastSessions(reader);
            JsonWriter writer = new JsonWriter("./data/testWriterEmptySession.json");
            writer.open();
            writer.write(s);
            writer.close();

            sessions = reader.read();
            List <Suggestion> s2Suggestions = sessions.get(1).getAllSuggestions();
            assertEquals(2, sessions.size());
            checkSessionProperties(2, "circle", 100, 49, 49, 0,0, s);
            assertEquals(0, s2Suggestions.size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralSessionsAddNewSession() {
        try {
            Session s = new CircleSession(2);
            s.setTarget(new Target(8,8));
            Suggestion first = new Suggestion(10, 10, "right", "up", 2, 2);
            Suggestion second = new Suggestion(9, 9, "right", "up", 1, 1);
            s.addSuggestion(first);
            s.setShots(s.getShots() + 1);
            s.addSuggestion(second);
            s.setShots(s.getShots() + 1);

            JsonReader reader = new JsonReader("./data/testWriterGeneralSession.json");
            s.addOldSessionsToPastSessions(reader);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralSession.json");
            writer.open();
            writer.write(s);
            writer.close();

            sessions = reader.read();
            assertEquals(2, sessions.size());
            checkSessionProperties(1, "circle", 100, 49, 49, 1,2, sessions.get(0));
            List <Suggestion> s1Suggestions = sessions.get(0).getAllSuggestions();
            checkSuggestion(50,50,"right", "up", 1, 1, s1Suggestions.get(0));
            checkSuggestion(49,49,"perfect", "perfect", 0, 0, s1Suggestions.get(1));

            checkSessionProperties(2, "circle",100, 8, 8, 0, 2, sessions.get(1));
            List <Suggestion> s2Suggestions = sessions.get(1).getAllSuggestions();
            checkSuggestion(10, 10, "right", "up", 2, 2, s2Suggestions.get(0));
            checkSuggestion(9, 9, "right", "up", 1, 1, s2Suggestions.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralSessionsAddNewSessionContinue() {
        try {
            JsonReader reader = new JsonReader("./data/testWriterGeneralSessionsContinue.json");
            sessions = reader.read();
            Session s = sessions.get(0);

            s.addOldSessionsToPastSessions(reader);

            Suggestion first = new Suggestion(60, 60, "right", "up", 11, 11);
            Suggestion second = new Suggestion(59, 59, "right", "up", 10, 10);
            s.addSuggestion(first);
            s.setShots(s.getShots() + 1);
            s.addSuggestion(second);
            s.setShots(s.getShots() + 1);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralSessionsContinue.json");
            writer.open();
            writer.write(s);
            writer.close();

            sessions = reader.read();
            assertEquals(1, sessions.size());
            checkSessionProperties(1, "circle", 100, 49,49, 1, 4, sessions.get(0));
            List <Suggestion> s1Suggestions = sessions.get(0).getAllSuggestions();
            checkSuggestion(50,50,"right", "up", 1, 1, s1Suggestions.get(0));
            checkSuggestion(49,49,"perfect", "perfect", 0, 0, s1Suggestions.get(1));
            checkSuggestion(60, 60, "right", "up", 11, 11, s1Suggestions.get(2));
            checkSuggestion(59, 59, "right", "up", 10, 10, s1Suggestions.get(3));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterEmptyPastSession() {
        try {
            JsonReader reader = new JsonReader("./data/testWriterEmptyPastSessions.json");
            sessions = reader.read();
            Session s = new CircleSession(1);
            s.setTarget(new Target(8, 8));

            s.addOldSessionsToPastSessions(reader);
            Suggestion first = new Suggestion(10, 10, "right", "up", 2, 2);
            Suggestion second = new Suggestion(9, 9, "right", "up", 1, 1);
            s.addSuggestion(first);
            s.addSuggestion(second);
            s.setHit(0);
            s.setShots(2);

            JsonWriter writer = new JsonWriter("./data/testWriterEmptyPastSessions.json");
            writer.open();
            writer.write(s);
            writer.close();

            sessions = reader.read();
            assertEquals(1, sessions.size());
            checkSessionProperties(1, "circle", 100, 8,8, 0,2, sessions.get(0));
            List <Suggestion> s1Suggestions = sessions.get(0).getAllSuggestions();
            checkSuggestion(10, 10, "right", "up", 2, 2, s1Suggestions.get(0));
            checkSuggestion(9, 9, "right", "up", 1, 1, s1Suggestions.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}