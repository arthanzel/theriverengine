package com.arthanzel.theriverengine.sim;

import com.arthanzel.theriverengine.rivergen.RiverArc;
import com.arthanzel.theriverengine.rivergen.RiverNetwork;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Documentation
 *
 * @author Martin
 */
public class Environment {
    public static final double RESOLUTION = 0.5;

    private Map<RiverArc, double[]> values = new HashMap<>();

    public Environment(RiverNetwork network) {
        for (RiverArc arc : network.edgeSet()) {
            double len = arc.length();

            double[] arcValues = new double[(int) (len / RESOLUTION)];
            for (int i = 0; i < arcValues.length; i++) {
                arcValues[i] = Math.random();
            }

            values.put(arc, arcValues);
        }
    }

    public double get(RiverArc arc, double pos) {
        if (pos < 0 || pos > arc.length()) {
            throw new IllegalArgumentException("Position must be greater or equal to 0 and less than or equal to the arc's length (" + arc.length() + ")");
        }

        double[] vals = values.get(arc);
        double idx = pos / RESOLUTION;
        int minIdx = (int) Math.floor(idx);
        int maxIdx = minIdx + 1;
        return lerp(vals[minIdx], vals[maxIdx], idx % 1);
    }

    private double lerp(double min, double max, double f) {
        return (max - min) * f + min;
    }

    //TODO: Pluggable interpolation functions
}
