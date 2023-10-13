package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// tests for Vector class
public class VectorTest {

    Vector zero;
    Vector sameVal;
    Vector justX;
    Vector justY;
    Vector diffVal;

    @BeforeEach
    public void runBefore(){
        zero = new Vector(0,0);
        sameVal = new Vector(100, 100);
        justX = new Vector(100, 0);
        justY = new Vector(0, 100);
        diffVal = new Vector(3, 4);
    }

    @Test
    public void testGetVectorLengthZero() {
        double zeroLength = zero.getVectorLength();

        assertEquals(0, zeroLength);
    }
    @Test
    public void testGetVectorLengthSame() {
        double sameValLength = sameVal.getVectorLength();

        assertEquals(Math.sqrt(20000), sameValLength);
    }

    @Test
    public void testGetVectorLengthX() {
        double justXLength = justX.getVectorLength();

        assertEquals(100, justXLength);
    }

    @Test
    public void testGetVectorLengthY() {
        double justYLength = justY.getVectorLength();

        assertEquals(100, justYLength);
    }

    @Test
    public void testGetVectorLengthDiff() {
        double diffValLength = diffVal.getVectorLength();
        assertEquals(5, diffValLength);
    }

    @Test
    //zero vector not tested because no unit vector
    public void testGetUnitVectorSame() {
        Vector sameValUnitVector = sameVal.getUnitVector();

        assertEquals(1 / Math.sqrt(2), sameValUnitVector.getCompX());
        assertEquals(1 / Math.sqrt(2), sameValUnitVector.getCompY());
    }

    @Test
    public void testGetUnitVectorX() {
        Vector justXUnitVector = justX.getUnitVector();

        assertEquals(1, justXUnitVector.getCompX());
        assertEquals(0, justXUnitVector.getCompY());
    }

    @Test
    public void testGetUnitVectorY() {
        Vector justYUnitVector = justY.getUnitVector();

        assertEquals(0, justYUnitVector.getCompX());
        assertEquals(1, justYUnitVector.getCompY());
    }

    @Test
    public void testGetUnitVectorDiff() {
        Vector diffValUnitVector = diffVal.getUnitVector();

        assertEquals(0.6, diffValUnitVector.getCompX());
        assertEquals(0.8, diffValUnitVector.getCompY());
    }

}
