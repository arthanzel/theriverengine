package com.arthanzel.theriverengine.util;

/**
 * Created by martin on 2016-11-30.
 */
public class TimeUtils {
    public static int S_IN_DAY = 60 * 60 * 24;

    public static double days(double seconds) {
        return seconds / 86400;
    }

    public static double hours(double seconds) {
        return seconds / 3600;
    }

    public static double minutes(double seconds) {
        return seconds / 60;
    }
}
