package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class SuggestionTest {

    @Test
    public void testGiveSuggestionRightDown() {
        Suggestion rightDown = new Suggestion(10, 10, "right", "down", 10, 10);
        String suggestion = rightDown.giveSuggestion();

        assertEquals("Shoot more to the left! Shoot more upwards!", suggestion);
    }

    @Test
    public void testGiveSuggestionRightUp() {
        Suggestion rightUp = new Suggestion (10, 10, "right", "up", 10, 10);
        String suggestion = rightUp.giveSuggestion();

        assertEquals("Shoot more to the left! Shoot more downwards!", suggestion);
    }

    @Test
    public void testGiveSuggestionLeftDown() {
        Suggestion leftDown = new Suggestion (10, 10, "left", "down", 10, 10);
        String suggestion = leftDown.giveSuggestion();

        assertEquals("Shoot more to the right! Shoot more upwards!", suggestion);
    }

    @Test
    public void testGiveSuggestionLeftUp() {
        Suggestion leftUp = new Suggestion (10, 10, "left", "up", 10, 10);
        String suggestion = leftUp.giveSuggestion();

        assertEquals("Shoot more to the right! Shoot more downwards!", suggestion);
    }

    @Test
    public void testGiveSuggestionJustLeft() {
        Suggestion left = new Suggestion (10, 10, "left", "perfect", 10, 0);
        String suggestion = left.giveSuggestion();

        assertEquals("Shoot more to the right!", suggestion);
    }

    @Test
    public void testGiveSuggestionJustRight() {
        Suggestion right = new Suggestion (10, 10, "right", "perfect", 10, 0);
        String suggestion = right.giveSuggestion();

        assertEquals("Shoot more to the left!", suggestion);
    }

    @Test
    public void testGiveSuggestionJustUp() {
        Suggestion up = new Suggestion (10, 10, "perfect", "up", 0, 10);
        String suggestion = up.giveSuggestion();

        assertEquals(" Shoot more downwards!", suggestion);
    }

    @Test
    public void testGiveSuggestionJustDown() {
        Suggestion down = new Suggestion (10, 10, "perfect", "down", 0, 10);
        String suggestion = down.giveSuggestion();

        assertEquals(" Shoot more upwards!", suggestion);
    }

    @Test
    public void testGiveSuggestionPerfect() {
        Suggestion down = new Suggestion (10, 10, "perfect", "perfect", 0, 10);
        String suggestion = down.giveSuggestion();

        assertEquals("Perfect shot!", suggestion);
    }
}