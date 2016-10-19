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

    @SuppressWarnings("unchecked")
    public static <T> T sample(Set<T> set) {
        return (T) set.toArray()[new Random().nextInt(set.size())];
    }
}
