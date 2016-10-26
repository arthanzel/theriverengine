package com.arthanzel.theriverengine.util;

import java.util.Random;
import java.util.Set;

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

    public static double lerp(double min, double max, double f) {
        return min + (max - min) * f;
    }

    /**
     * Rounds the closest multiple to a value.
     * @param val Value to round.
     * @param multiple Multiple to which to round.
     */
    public static double round(double val, double multiple) {
        return Math.round(val / multiple) * multiple;
    }

    @SuppressWarnings("unchecked")
    public static <T> T sample(Set<T> set) {
        return (T) set.toArray()[new Random().nextInt(set.size())];
    }
}
