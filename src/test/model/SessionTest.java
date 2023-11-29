package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

// test for CircleSession class
class SessionTest {
    private Session cs;

    @BeforeEach
    public void runBefore() {
            cs = new Session(1);
            cs.setTarget(new Target(60,60));
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
        cs.analyze(1, 10, 60, 60, 50, false);
        Suggestion generatedSuggestion = cs.getLastSuggestion();

        assertEquals("left", generatedSuggestion.getDirX());
        assertEquals("perfect", generatedSuggestion.getDirY());
        assertEquals(20.8552199713047, generatedSuggestion.getAmtX());
        assertEquals(17.67391522991923, generatedSuggestion.getAmtY());
    }

    @Test
    public void testAnalyzeOnlyXRight() {
        cs.analyze(1000, 60, 60, 60, 50, false);
        Suggestion generatedSuggestion = cs.getLastSuggestion();

        assertEquals( "right",generatedSuggestion.getDirX());
        assertEquals("perfect", generatedSuggestion.getDirY());
        assertEquals(890, generatedSuggestion.getAmtX());
        assertEquals(0, generatedSuggestion.getAmtY());
    }

    @Test
    public void testAnalyzeOnlyYUp() {
        cs.analyze(60, 200, 60, 60, 50, false);
        Suggestion generatedSuggestion = cs.getLastSuggestion();

        assertEquals("perfect", generatedSuggestion.getDirX());
        assertEquals("down", generatedSuggestion.getDirY());
        assertEquals(0, generatedSuggestion.getAmtX());
        assertEquals(90, generatedSuggestion.getAmtY());
    }

    @Test
    public void testAnalyzeOnlyYDown() {
        cs.analyze(60, 1, 60, 60, 50, false);
        Suggestion generatedSuggestion = cs.getLastSuggestion();

        assertEquals("perfect", generatedSuggestion.getDirX());
        assertEquals("up", generatedSuggestion.getDirY());
        assertEquals(0, generatedSuggestion.getAmtX());
        assertEquals(9, generatedSuggestion.getAmtY());
    }

    @Test
    public void testAnalyzeRightUp() {
        cs.analyze(200, 200, 60, 60, 50, false);
        Suggestion generatedSuggestion = cs.getLastSuggestion();
        double correctDist = 100 - (5 / Math.sqrt(2) + 10);

        assertEquals("right", generatedSuggestion.getDirX());
        assertEquals("down", generatedSuggestion.getDirY());
        assertEquals(104.64466094067262, generatedSuggestion.getAmtX());
        assertEquals(104.64466094067262, generatedSuggestion.getAmtY());
    }

    @Test
    public void testAnalyzeRightDown() {
        cs.analyze(200, 1, 60, 60, 50, false);
        Suggestion generatedSuggestion = cs.getLastSuggestion();

        double length = Math.sqrt(Math.pow(90, 2) + Math.pow(9, 2));
        double unitX = 90 / length;
        double unitY = -9 / length;

        assertEquals("right", generatedSuggestion.getDirX());
        assertEquals("up", generatedSuggestion.getDirY());
        assertEquals(93.92442862675496, generatedSuggestion.getAmtX());
        assertEquals(39.58243777841816, generatedSuggestion.getAmtY());
    }

    @Test
    public void testAnalyzeLeftUp() {
        cs.analyze(1, 200, 60, 60, 50, false);
        Suggestion generatedSuggestion = cs.getLastSuggestion();

        double length = Math.sqrt(Math.pow(90, 2) + Math.pow(9, 2));
        double unitX = -9 / length;
        double unitY = 90 / length;

        assertEquals("left", generatedSuggestion.getDirX());
        assertEquals("down", generatedSuggestion.getDirY());
        assertEquals(39.58243777841816, generatedSuggestion.getAmtX());
        assertEquals(93.92442862675496, generatedSuggestion.getAmtY());
    }

    @Test
    public void testAnalyzeLeftDown() {
        cs.analyze(1, 1, 60, 60, 50, false);
        Suggestion generatedSuggestion = cs.getLastSuggestion();
        double correctDist = 10 - (5 / Math.sqrt(2)) - 1;

        assertEquals("left", generatedSuggestion.getDirX());
        assertEquals("up", generatedSuggestion.getDirY());
        assertEquals(23.64466094067263, generatedSuggestion.getAmtX());
        assertEquals(23.64466094067263, generatedSuggestion.getAmtY());
    }

    @Test
    public void testUpdateSummarySuggestionWithOneSuggestionLeft() {
        cs.analyze(1, 60, 60, 60, 50, false);
        Suggestion summary = cs.updateSummarySuggestion();
        String suggestion = summary.giveSuggestion();

        assertEquals("Shoot more to the right!", suggestion);
    }

    @Test
    public void testUpdateSummarySuggestionWithOneSuggestionDown() {
        cs.analyze(60, 1, 60, 60, 50, false);
        Suggestion summary = cs.updateSummarySuggestion();
        String suggestion = summary.giveSuggestion();

        assertEquals(" Shoot more downwards!", suggestion);
    }

    @Test
    public void testUpdateSummarySuggestionWithOneSuggestionRight() {
        cs.analyze(200, 60, 60, 60, 50, false);
        Suggestion summary = cs.updateSummarySuggestion();
        String suggestion = summary.giveSuggestion();

        assertEquals("Shoot more to the left!", suggestion);
    }

    @Test
    public void testUpdateSummarySuggestionWithOneSuggestionUp() {
        cs.analyze(60, 200, 60, 60, 50, false);
        Suggestion summary = cs.updateSummarySuggestion();
        String suggestion = summary.giveSuggestion();

        assertEquals(" Shoot more upwards!", suggestion);
    }

    @Test
    public void testUpdateSummarySuggestionWithLotsSuggestionRightUp() {
        cs.analyze(1, 1, 60, 60, 50, false);
        cs.analyze(400, 400, 60, 60, 50, false);
        cs.analyze(400, 400, 60, 60, 50, false);
        cs.analyze(400, 400, 60, 60, 50, false);
        cs.analyze(400, 400, 60, 60, 50, false);

        Suggestion summary = cs.updateSummarySuggestion();
        String suggestion = summary.giveSuggestion();

        assertEquals("Shoot more to the left! Shoot more upwards!", suggestion);
    }

    @Test
    public void testUpdateSummarySuggestionWithLotsSuggestionRightDown() {
        cs.analyze(400, 1, 60, 60, 50, false);
        cs.analyze(400, 1, 60, 60, 50, false);
        cs.analyze(400, 4, 60, 60, 50, false);
        cs.analyze(400, 1, 60, 60, 50, false);
        cs.analyze(400, 1, 60, 60, 50, false);

        Suggestion summary = cs.updateSummarySuggestion();
        String suggestion = summary.giveSuggestion();

        assertEquals("Shoot more to the left! Shoot more downwards!", suggestion);
    }

    @Test
    public void testUpdateSummarySuggestionWithLotsSuggestionLeftDown() {
        cs.analyze(1, 1, 60, 60, 50, false);
        cs.analyze(1, 1, 60, 60, 50, false);
        cs.analyze(1, 1, 60, 60, 50, false);
        cs.analyze(1, 4, 60, 60, 50, false);
        cs.analyze(1, 1, 60, 60, 50, false);
        cs.analyze(1, 1, 60, 60, 50, false);
        Suggestion summary = cs.updateSummarySuggestion();
        String suggestion = summary.giveSuggestion();

        assertEquals("Shoot more to the right! Shoot more downwards!", suggestion);
    }


    @Test
    public void testUpdateSummarySuggestionWithLotsSuggestionLeftUp() {
        cs.analyze(0, 400, 60, 60, 50, false);
        cs.analyze(0, 400, 60, 60, 50, false);
        cs.analyze(0, 400, 60, 60, 50, false);
        cs.analyze(0, 400, 60, 60, 50, false);
        cs.analyze(0, 400, 60, 60, 50, false);
        cs.analyze(0, 400, 60, 60, 50, false);
        Suggestion summary = cs.updateSummarySuggestion();
        String suggestion = summary.giveSuggestion();

        assertEquals("Shoot more to the right! Shoot more upwards!", suggestion);
    }

    @Test
    public void testUpdateSummarySuggestionWithLotsSuggestionPerfect() {
        cs.analyze(59, 61, 60, 60, 50, true);
        cs.analyze(59, 61, 60, 60, 50, true);
        cs.analyze(59, 61, 60, 60, 50, true);
        cs.analyze(59, 61, 60, 60, 50, true);


        Suggestion summary = cs.updateSummarySuggestion();
        String suggestion = summary.giveSuggestion();

        assertEquals("Perfect!", suggestion);
    }

    @Test
    public void testUpdateSummarySuggestionWithLotsSuggestionJustUp() {
        cs.analyze(60, 300, 60, 60, 50, false);
        cs.analyze(60, 300, 60, 60, 50, false);
        cs.analyze(60, 300, 60, 60, 50, false);
        cs.analyze(60, 300, 60, 60, 50, false);
        cs.analyze(60, 300, 60, 60, 50, false);
        Suggestion summary = cs.updateSummarySuggestion();
        String suggestion = summary.giveSuggestion();

        assertEquals(" Shoot more upwards!", suggestion);
    }

    @Test
    public void testUpdateSummarySuggestionWithLotsSuggestionJustDown() {
        cs.analyze(60, 2, 60, 60, 50, false);
        cs.analyze(60, 2, 60, 60, 50, false);
        cs.analyze(60, 2, 60, 60, 50, false);
        cs.analyze(60, 2, 60, 60, 50, false);
        cs.analyze(60, 2, 60, 60, 50, false);

        Suggestion summary = cs.updateSummarySuggestion();
        String suggestion = summary.giveSuggestion();

        assertEquals(" Shoot more downwards!", suggestion);
    }

    @Test
    public void testUpdateSummarySuggestionWithLotsSuggestionJustLeft() {
        cs.analyze(1, 60, 60, 60, 50, false);
        cs.analyze(1, 60, 60, 60, 50, false);
        cs.analyze(1, 60, 60, 60, 50, false);
        cs.analyze(1, 60, 60, 60, 50, false);
        cs.analyze(1, 60, 60, 60, 50, false);

        Suggestion summary = cs.updateSummarySuggestion();
        String suggestion = summary.giveSuggestion();

        assertEquals("Shoot more to the right!", suggestion);
    }

    @Test
    public void testUpdateSummarySuggestionWithLotsSuggestionJustRight() {
        cs.analyze(200, 60, 60, 60, 50, false);
        cs.analyze(200, 60, 60, 60, 50, false);
        cs.analyze(200, 60, 60, 60, 50, false);
        cs.analyze(200, 60, 60, 60, 50, false);
        cs.analyze(200, 60, 60, 60, 50, false);

        Suggestion summary = cs.updateSummarySuggestion();
        String suggestion = summary.giveSuggestion();

        assertEquals("Shoot more to the left!", suggestion);
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
        cs.analyze(300, 300, 60, 60, 50, false);
        cs.analyze(1, 300, 60, 60, 50, false);
        cs.analyze(300, 300, 60, 60, 50, false);

        List<Suggestion> allSuggestions = cs.getAllSuggestions();

        assertEquals(3, allSuggestions.size());
        assertEquals("Shoot more to the left! Shoot more upwards!", allSuggestions.get(0).giveSuggestion());
        assertEquals("Shoot more to the right! Shoot more upwards!", allSuggestions.get(1).giveSuggestion());
        assertEquals("Shoot more to the left! Shoot more upwards!", allSuggestions.get(2).giveSuggestion());
    }

    @Test
    public void testAccuracy() {
        cs.analyze(300, 300, 60, 60, 50, false);
        cs.hit();
        double accuracy = cs.getAccuracy();

        assertEquals(100, accuracy);
    }

    @Test
    public void testAccuracyNoShots() {
        double accuracy = cs.getAccuracy();

        assertEquals(0, accuracy);
    }
}