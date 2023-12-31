package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// tests the CircleTarget class
class TargetTest {

    private Target c;

    @BeforeEach
    public void runBefore() {
        c = new Target(200, 200);
    }

    @Test
    public void testConstructor() {
        assertEquals(200, c.getCenterX());
        assertEquals(200, c.getCenterY());
        assertEquals(50, c.getRadius());
    }

    @Test
    public void testHitTargetDistLessThanRadius(){
        boolean hit = c.hitTarget(200, 200);

        assertTrue(hit);
    }

    @Test
    public void testHitTargetDistEqualRadius(){
        boolean hit = c.hitTarget(200, 205);

        assertTrue(hit);
    }

    @Test
    public void testHitTargetDistMoreThanRadius(){
        boolean hit = c.hitTarget(400, 400);

        assertFalse(hit);
    }

    @Test
    public void changeDist() {
        c.changeDist(10);
        assertEquals(250, c.getRadius());
    }



}