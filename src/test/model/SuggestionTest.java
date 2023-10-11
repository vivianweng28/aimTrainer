package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SuggestionTest {

    @Test
    public void testGiveSuggestionRightDown() {
        Suggestion rightDown = new Suggestion(10, 10, "right", "down", 10, 10);

    }

    @Test
    public void testGiveSuggestionRightUp() {
        Suggestion rightUp = new Suggestion (10, 10, "right", "up", 10, 10);

    }

    @Test
    public void testGiveSuggestionLeftDown() {
        Suggestion leftDown = new Suggestion (10, 10, "left", "down", 10, 10);
    }

    @Test
    public void testGiveSuggestionLeftUp() {
        Suggestion leftUp = new Suggestion (10, 10, "left", "up", 10, 10);
    }

    @Test
    public void testGiveSuggestionJustLeft() {
        Suggestion left = new Suggestion (10, 10, "left", "perfect", 10, 0);
    }

    @Test
    public void testGiveSuggestionJustRight() {
        Suggestion right = new Suggestion (10, 10, "right", "perfect", 10, 0);
    }

    @Test
    public void testGiveSuggestionJustUp() {
        Suggestion up = new Suggestion (10, 10, "perfect", "up", 0, 10);
    }

    @Test
    public void testGiveSuggestionJustDown() {
        Suggestion down = new Suggestion (10, 10, "perfect", "down", 0, 10);
    }

    @Test
    public void testGiveSuggestionPerfect() {
        Suggestion down = new Suggestion (10, 10, "perfect", "down", 0, 10);
    }
}