package com.arthanzel.theriverengine.util;

import java.util.Random;
import java.util.Set;

/**
 * Provides fish-related math functions not provided by Java's standard library.
 *
 * @author Martin
 */
public class FishMath {
    public static double clamp(double val, double min, double max) {
        if (val < min) {
            return min;
        }
        else if (val > max) {
            return max;
        }
        return val;
    }

    public static double lerp(double min, double max, double f) {
        return min + (max - min) * f;
    }

    public static double inverseLerp(double min, double max, double val) {
        return (val - min) / (max - min);
    }

    /**
     * Rounds the closest multiple to a value.
     * @param val Value to round.
     * @param multiple Multiple to which to round.
     */
    public static double round(double val, double multiple) {
        return Math.round(val / multiple) * multiple;
    }

    /**
     * Returns the nearest multiple of a number that is greater than a value.
     * @param val Value to ceil.
     * @param multiple Multiple to which to ceil.
     */
    public static double ceil(double val, double multiple) {
        return Math.ceil(val / multiple) * multiple;
    }

    /**
     * Returns the nearest multiple of a number that is less than a value.
     * @param val Value to floor.
     * @param multiple Multiple to which to floor.
     */
    public static double floor(double val, double multiple) {
        return Math.floor(val / multiple) * multiple;
    }

    public static double random(double min, double max) {
        final double d = max - min;
        return Math.random() * d + min;
    }

    @SuppressWarnings("unchecked")
    public static <T> T sample(Set<T> set) {
        return (T) set.toArray()[new Random().nextInt(set.size())];
    }
}
