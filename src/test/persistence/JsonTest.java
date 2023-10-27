package persistence;


import model.Session;
import model.Suggestion;

import static org.junit.jupiter.api.Assertions.assertEquals;

// "Code influenced by the JsonSerizalizationDemo link_to_demo
// test that JsonReaderTest and JsonWriterTest will both use
public class JsonTest {
    protected void checkSuggestion(double shotX, double shotY, String dirX, String dirY, double amtX,
                                   double amtY, Suggestion suggestion) {
        assertEquals(shotX, suggestion.getCompX());
        assertEquals(shotY, suggestion.getCompY());
        assertEquals(dirX, suggestion.getDirX());
        assertEquals(dirY, suggestion.getDirY());
        assertEquals(amtX, suggestion.getAmtX());
        assertEquals(amtY, suggestion.getAmtY());
    }
    protected void checkSessionProperties(double sessionNum, String sessionType, int dist, double targetX,
                                                        double targetY, int hit, int shots, Session session) {
        assertEquals(sessionNum, session.getSessionNum());
        assertEquals(sessionType, session.getSessionType());
        assertEquals(dist, session.getDistance());
        assertEquals(targetX, session.getTarget().getCenterX());
        assertEquals(targetY, session.getTarget().getCenterY());
        assertEquals(hit, session.getHit());
        assertEquals(shots, session.getShots());
    }
}