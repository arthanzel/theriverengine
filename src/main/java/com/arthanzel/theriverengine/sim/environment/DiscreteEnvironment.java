package com.arthanzel.theriverengine.sim.environment;

import com.arthanzel.theriverengine.rivergen.RiverArc;
import com.arthanzel.theriverengine.rivergen.RiverNetwork;
import com.arthanzel.theriverengine.util.FishMath;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Documentation
 *
 * @author Martin
 */
public class DiscreteEnvironment implements Environment {
    public static final double RESOLUTION = 8;

    private Map<RiverArc, double[]> values = new HashMap<>();

    private DiscreteEnvironment() {}

    public DiscreteEnvironment(RiverNetwork network) {
        for (RiverArc arc : network.edgeSet()) {
            double len = arc.length();

            // Creates a field of doubles, spaces RESOLUTION units apart, that fit the whole arc.
            double[] arcValues = new double[(int) (len / RESOLUTION) + 1];
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

        // If the arc is too short to have a gradient.
        if (vals.length == 1) {
            return vals[0];
        }

        // The arc may not have a value for its far end, for instance, if its length is 10.5 m and its resolution is 1 m.
        // That leaves 0.5 m unaccounted for. In this case, extrapolate from the previous values).
        double remainder = arc.length() - pos;
        if (remainder < RESOLUTION) {
            return FishMath.lerp(vals[vals.length - 2], vals[vals.length - 1], 1 + remainder);
        }

        // Interpolate between the nearest value points.
        double idx = pos / RESOLUTION; // Virtual "index" in the values array
        int wholePart = (int) idx;
        double fracPart = idx % 1;

        // Math.min prevents overflows in the case that pos == arc.length()
        return FishMath.lerp(vals[wholePart], vals[Math.min(wholePart + 1, vals.length)], fracPart);
    }

     // ====== Accessors ======

    public Map<RiverArc, double[]> getValues() {
        return values;
    }

    //TODO: Pluggable interpolation functions

    public Environment clone() {
        DiscreteEnvironment env = new DiscreteEnvironment();
        for (RiverArc arc : this.values.keySet()) {
            double[] doubles = this.values.get(arc);
            env.values.put(arc, Arrays.copyOf(doubles, doubles.length));
        }
        return env;
    }
}
