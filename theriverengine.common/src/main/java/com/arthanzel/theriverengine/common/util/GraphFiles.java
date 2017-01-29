package com.arthanzel.theriverengine.common.util;

/**
 * Provides helper functions for dealing with graph files.
 *
 * @author Martin
 */
public class GraphFiles {
    public static double[] toDoubles(String text) {
        String[] components = text.split(",");
        double[] doubles = new double[components.length];

        for (int i = 0; i < components.length; i++) {
            doubles[i] = Double.parseDouble(components[i]);
        }

        return doubles;
    }

    public static String[] toStrings(String text) {
        String[] components = text.split(",");
        String[] strings = new String[components.length];

        for (int i = 0; i < components.length; i++) {
            strings[i] = components[i].trim();
        }

        return strings;
    }
}
