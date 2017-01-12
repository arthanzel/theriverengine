package com.arthanzel.theriverengine.common.util;

/**
 * Provides helper methods for manipulating times.
 */
public class TimeUtils {
    /**
     * Returns the number of days elapsed in the given number of seconds.
     */
    public static double days(double seconds) {
        return seconds / 86400;
    }

    /**
     * Returns the number of hours elapsed in the given number of seconds.
     */
    public static double hours(double seconds) {
        return seconds / 3600;
    }

    /**
     * Returns the number of minutes elapsed in the given number of seconds.
     */
    public static double minutes(double seconds) {
        return seconds / 60;
    }

    /**
     * Returns the number of year elapsed in the given number of seconds.
     */
    public static double years(double seconds) {
        return seconds / 31536000;
    }
}
