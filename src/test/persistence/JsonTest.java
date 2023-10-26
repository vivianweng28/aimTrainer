package persistence;


import model.Session;
import model.Suggestion;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    protected void checkSessionNumAndType(double sessionNum, String sessionType, Session session) {
        assertEquals(sessionNum, session.getSessionNum());
        assertEquals(sessionType, session.getSessionType());
    }
}