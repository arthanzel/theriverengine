package com.arthanzel.theriverengine.common.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Random;
import java.util.Set;

/**
 * Provides fish-related math functions not provided by Java's standard library.
 *
 * @author Martin
 */
public class FishMath {
    private static Random r = new Random();

    /**
     * Restricts a value to a certain range.
     * @param val The value to clamp.
     * @param min Minimum bound. If {@code val < min}, {@code min} is returned.
     * @param max Maximum bound. If {@code val > max}, {@code max} is returned.
     * @return The clamped value.
     */
    public static double clamp(double val, double min, double max) {
        if (val < min) {
            return min;
        }
        else if (val > max) {
            return max;
        }
        return val;
    }

    /**
     * Performs a linear interpolation between two values.
     * @param min The lower bound.
     * @param max The upper bound.
     * @param f Interpolation parameter.
     * @return The interpolated value. If {@code f == 0}, {@code min} is returned.
     * If {@code f == 1}, {@code max} is returned. {@code f} may be lesser than
     * 0 or greater than 1.
     */
    public static double lerp(double min, double max, double f) {
        return min + (max - min) * f;
    }

    /**
     * Calculates the linear parameter that produces {@code val} when interpolated
     * between {@code min} and {@code max}.
     * @param min The lower bound.
     * @param max The upper bound.
     * @param val The result from the linear interpolation.
     * @return The interpolation parameter.
     */
    public static double inverseLerp(double min, double max, double val) {
        return (val - min) / (max - min);
    }

    /**
     * Rounds a value to some multiple.
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

    /**
     * Returns a normally-distributed value.
     * @param mean Mean.
     * @param sd Standard deviation.
     */
    public static double gaussian(double mean, double sd) {
        return r.nextGaussian() * sd + mean;
    }

    /**
     * Returns a normally-distributed value between bounds.
     * @param mean Mean.
     * @param sd Standard deviation.
     * @param pm Bound, as a measure of {@code mean} plus/minus {@code pm}.
     */
    public static double gaussian(double mean, double sd, double pm) {
        double val;
        do {
            val = gaussian(mean, sd);
        } while (Math.abs(val - mean) > pm);
        return val;
    }

    /**
     * Returns a pseudo-random number between {@code min} and {@code max}, exclusive.
     * @param min The lower bound.
     * @param max The exclusive upper bound.
     */
    public static double random(double min, double max) {
        final double d = max - min;
        return Math.random() * d + min;
    }

    /**
     * Returns a pseudo-random integer between {@code min} and {@code max}, exclusive.
     * @param min The lower bound.
     * @param max The exclusive upper bound.
     */
    public static int randomInt(int min, int max) {
        return new Random().nextInt(max - min) + min;
    }

    /**
     * Selects a random member from a set.
     * @param set The set.
     */
    @SuppressWarnings("unchecked")
    public static <T> T sample(Set<T> set) {
        return (T) set.toArray()[new Random().nextInt(set.size())];
    }

    /**
     * Converts a double to a BigDecimal rounded to 2 decimal places..
     */
    public static BigDecimal toDecimal(double val) {
        return new BigDecimal(val).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Converts a double to a BigDecimal rounded to a fixed number of decimal places.
     */
    public static BigDecimal toDecimal(double val, int scale) {
        return new BigDecimal(val).setScale(scale, RoundingMode.HALF_UP);
    }
}
