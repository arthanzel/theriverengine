package com.arthanzel.theriverengine.util;

/**
 * Provides fish-related math functions not provided by Java's standard library.
 *
 * @author Martin
 */
public class FishMath {
    private FishMath() {}

    public static double clamp(double val, double min, double max) {
        return Math.min(max, Math.max(min, val));
    }
}
