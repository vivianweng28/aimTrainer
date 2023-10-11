package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class CircleSessionTest {
    private CircleSession cs;

    @BeforeEach
    public void runBefore() {
            cs = new CircleSession();
    }

    @Test void testGetClosestShot() {
        Vector vector = new Vector(90, 90);
        Shot closestShot = cs.getClosestShot(vector, 5, 10, 10);

        double comp = 90 / Math.sqrt(Math.pow(90,2) + Math.pow(90,2)) * 5 + 10;

        assertEquals(comp, closestShot.getCompX());
        assertEquals(comp, closestShot.getCompY());
    }

    @Test
    public void testAnalyzeOnlyXLeft() {
        cs.analyze(1, 10, 10, 10, 5);
        Suggestion generatedSuggestion = cs.getLastSuggestion();

        assertEquals("left", generatedSuggestion.getDirX());
        assertEquals("perfect", generatedSuggestion.getDirY());
        assertEquals(4, generatedSuggestion.getAmtX());
        assertEquals(0, generatedSuggestion.getAmtY());
    }

    @Test
    public void testAnalyzeOnlyXRight() {
        cs.analyze(100, 10, 10, 10, 5);
        Suggestion generatedSuggestion = cs.getLastSuggestion();

        assertEquals( "right",generatedSuggestion.getDirX());
        assertEquals("perfect", generatedSuggestion.getDirY());
        assertEquals(85, generatedSuggestion.getAmtX());
        assertEquals(0, generatedSuggestion.getAmtY());
    }

    @Test
    public void testAnalyzeOnlyYUp() {
        cs.analyze(10, 100, 10, 10, 5);
        Suggestion generatedSuggestion = cs.getLastSuggestion();

        assertEquals("perfect", generatedSuggestion.getDirX());
        assertEquals("up", generatedSuggestion.getDirY());
        assertEquals(0, generatedSuggestion.getAmtX());
        assertEquals(85, generatedSuggestion.getAmtY());
    }

    @Test
    public void testAnalyzeOnlyYDown() {
        cs.analyze(10, 1, 10, 10, 5);
        Suggestion generatedSuggestion = cs.getLastSuggestion();

        assertEquals("perfect", generatedSuggestion.getDirX());
        assertEquals("down", generatedSuggestion.getDirY());
        assertEquals(0, generatedSuggestion.getAmtX());
        assertEquals(4, generatedSuggestion.getAmtY());
    }

    @Test
    public void testAnalyzeRightUp() {
        cs.analyze(100, 100, 10, 10, 5);
        Suggestion generatedSuggestion = cs.getLastSuggestion();
        double correctDist = 100 - (5 / Math.sqrt(2) + 10);

        assertEquals("right", generatedSuggestion.getDirX());
        assertEquals("up", generatedSuggestion.getDirY());
        assertEquals(correctDist, generatedSuggestion.getAmtX());
        assertEquals(correctDist, generatedSuggestion.getAmtY());
    }

    @Test
    public void testAnalyzeRightDown() {
        cs.analyze(100, 1, 10, 10, 5);
        Suggestion generatedSuggestion = cs.getLastSuggestion();

        double length = Math.sqrt(Math.pow(90, 2) + Math.pow(9, 2));
        double unitX = 90 / length;
        double unitY = -9 / length;

        assertEquals("right", generatedSuggestion.getDirX());
        assertEquals("down", generatedSuggestion.getDirY());
        assertEquals(Math.abs(100 - (10 + unitX * 5)), generatedSuggestion.getAmtX());
        assertEquals(Math.abs(1 - (10 + unitY * 5)), generatedSuggestion.getAmtY());
    }

    @Test
    public void testAnalyzeLeftUp() {
        cs.analyze(1, 100, 10, 10, 5);
        Suggestion generatedSuggestion = cs.getLastSuggestion();

        double length = Math.sqrt(Math.pow(90, 2) + Math.pow(9, 2));
        double unitX = -9 / length;
        double unitY = 90 / length;

        assertEquals("left", generatedSuggestion.getDirX());
        assertEquals("up", generatedSuggestion.getDirY());
        assertEquals(Math.abs(1 - (10 + unitX * 5)), generatedSuggestion.getAmtX());
        assertEquals(Math.abs(100 - (10 + unitY * 5)), generatedSuggestion.getAmtY());
    }

    @Test
    public void testAnalyzeLeftDown() {
        cs.analyze(1, 1, 10, 10, 5);
        Suggestion generatedSuggestion = cs.getLastSuggestion();
        double correctDist = 10 - (5 / Math.sqrt(2)) - 1;

        assertEquals("left", generatedSuggestion.getDirX());
        assertEquals("down", generatedSuggestion.getDirY());
        assertEquals(correctDist, generatedSuggestion.getAmtX());
        assertEquals(correctDist, generatedSuggestion.getAmtY());
    }

    @Test
    public void testUpdateSummarySuggestionWithOneSuggestion() {
        cs.analyze(1, 1, 10, 10, 5);
        Suggestion summary = cs.updateSummarySuggestion();
        String suggestion = summary.giveSuggestion();

        assertEquals("Shoot more to the right! Shoot more upwards!", suggestion);
    }

    @Test
    public void testUpdateSummarySuggestionWithTwoSuggestion() {
        cs.analyze(1, 1, 10, 10, 5);
        cs.analyze(0.1, 0.1, 10, 10, 5);
        Suggestion summary = cs.updateSummarySuggestion();
        String suggestion = summary.giveSuggestion();

        assertEquals("Shoot more to the right! Shoot more upwards!", suggestion);
    }

    @Test
    public void testUpdateSummarySuggestionWithLotsSuggestion() {
        cs.analyze(1, 1, 10, 10, 5);
        cs.analyze(0.1, 0.1, 10, 10, 5);
        cs.analyze(400, 400, 10, 10, 5);
        cs.analyze(400, 400, 10, 10, 5);
        cs.analyze(400, 400, 10, 10, 5);
        cs.analyze(400, 1, 10, 10, 5);
        cs.analyze(1, 400, 10, 10, 5);
        cs.analyze(400, 400, 10, 10, 5);
        Suggestion summary = cs.updateSummarySuggestion();
        String suggestion = summary.giveSuggestion();

        assertEquals("Shoot more to the left! Shoot more downwards!", suggestion);
    }

    @Test
    public void testHitOnce() {
        int before = cs.getHit();
        cs.hit();
        int after = cs.getHit();

        assertEquals(0, before);
        assertEquals(1, after);
    }

    @Test
    public void testHitMultiple() {
        int before = cs.getHit();
        cs.hit();
        cs.hit();
        int after = cs.getHit();

        assertEquals(0, before);
        assertEquals(2, after);
    }

    @Test
    public void testGetAllSuggestions() {
        cs.analyze(300, 300, 10, 10, 5);
        cs.analyze(1, 300, 10, 10, 5);
        cs.analyze(300, 300, 10, 10, 5);

        List<Suggestion> allSuggestions = cs.getAllSuggestions();

        assertEquals(3, allSuggestions.size());
        assertEquals("Shoot more to the left! Shoot more downwards!", allSuggestions.get(0).giveSuggestion());
        assertEquals("Shoot more to the right! Shoot more downwards!", allSuggestions.get(1).giveSuggestion());
        assertEquals("Shoot more to the left! Shoot more downwards!", allSuggestions.get(2).giveSuggestion());
    }

    @Test
    public void testAccuracy() {
        cs.analyze(300, 300, 10, 10, 5);
        cs.hit();
        double accuracy = cs.getAccuracy();

        assertEquals(50, accuracy);
    }
}