package com.arthanzel.theriverengine.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by martin on 2016-10-26.
 */
public class FishMathTest {
    @Test
    public void clamp() {
        assertEquals(0.0, FishMath.clamp(-4, 0, 1), 0.0001);
        assertEquals(1, FishMath.clamp(200, 0, 1), 0.0001);
        assertEquals(0.1, FishMath.clamp(0.1, 0, 1), 0.0001);
        assertEquals(5, FishMath.clamp(6, 0, 5), 0.0001);
        assertEquals(5, FishMath.clamp(5, 0, 5), 0.0001);
    }

    @Test
    public void lerp() {
        assertEquals(0, FishMath.lerp(0, 100, 0), 0.0001);
        assertEquals(100, FishMath.lerp(0, 100, 1), 0.0001);
        assertEquals(150, FishMath.lerp(0, 100, 1.5), 0.0001);
        assertEquals(-100, FishMath.lerp(0, 100, -1), 0.0001);
        assertEquals(100 * 2.0 / 3, FishMath.lerp(0, 100, 2.0 / 3), 0.0001);
    }

    @Test
    public void round() {
        assertEquals(0.5, FishMath.round(0.6, 0.5), 0.0001);
        assertEquals(1, FishMath.round(0.75, 0.5), 0.0001);
        assertEquals(100, FishMath.round(60, 100), 0.0001);
        assertEquals(2, FishMath.round(2.1, 1), 0.0001);
        assertEquals(2.5, FishMath.round(2.6, 0.5), 0.0001);
    }

    @Test
    public void sample() {
        Set<Integer> set = new HashSet<>();
        Set<Integer> source = new HashSet<>(Arrays.asList(new Integer[] { 0, 1, 2, 3, 4 }));
        for (int i = 0; i < 100; i++) {
            set.add(FishMath.sample(source));
        }

        assertEquals(5, set.size());
    }
}