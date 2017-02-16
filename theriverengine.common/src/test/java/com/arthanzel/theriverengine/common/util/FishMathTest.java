package com.arthanzel.theriverengine.common.util;

import org.junit.Test;

import static org.junit.Assert.*;

import static com.arthanzel.theriverengine.common.util.FishMath.*;

public class FishMathTest {
    @Test
    public void testClamp() throws Exception {
        assertEquals(0, clamp(0, 0, 1), 0.0001);
        assertEquals(1, clamp(1, 0, 1), 0.0001);
        assertEquals(0, clamp(-1, 0, 1), 0.0001);
        assertEquals(1, clamp(2, 0, 1), 0.0001);
        assertEquals(0.5, clamp(0.5, 0, 1), 0.0001);
        assertEquals(0.5, clamp(0.5, -20, 20), 0.0001);
    }

    @Test
    public void testLerp() throws Exception {
        assertEquals(10, lerp(0,20,0.5), 0.0001);
        assertEquals(20, lerp(0,20,1), 0.0001);
        assertEquals(0, lerp(0,20,0), 0.0001);
        assertEquals(30, lerp(0,20,1.5), 0.0001);
        assertEquals(-10, lerp(0,20,-0.5), 0.0001);
    }

    @Test
    public void testInverseLerp() throws Exception {
        assertEquals(0.5, inverseLerp(0, 20, 10), 0.0001);
        assertEquals(0.0, inverseLerp(0, 20, 0), 0.0001);
        assertEquals(1, inverseLerp(0, 20, 20), 0.0001);
        assertEquals(1.5, inverseLerp(0, 20, 30), 0.0001);
        assertEquals(-0.5, inverseLerp(0, 20, -10), 0.0001);
    }

    @Test
    public void testRound() throws Exception {
        assertEquals(2, round(1, 2), 0.0001);
        assertEquals(2, round(2, 2), 0.0001);
        assertEquals(0, round(0, 2), 0.0001);
        assertEquals(2, round(2.99999999999, 2), 0.0001);
        assertEquals(1.5, round(1.4, 0.5), 0.0001);
        assertEquals(-1.5, round(-1.4, 0.5), 0.0001);
        assertEquals(-1.5, round(-1.4, -0.5), 0.0001);
    }

    @Test
    public void ceil() throws Exception {

    }

    @Test
    public void floor() throws Exception {

    }

    @Test
    public void random() throws Exception {

    }

    @Test
    public void randomInt() throws Exception {

    }

    @Test
    public void sample() throws Exception {

    }

}