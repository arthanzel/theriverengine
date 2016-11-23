package com.arthanzel.theriverengine.sim.environment;

import com.arthanzel.theriverengine.rivergen.RiverArc;
import com.arthanzel.theriverengine.rivergen.RiverNetwork;
import com.arthanzel.theriverengine.rivergen.RiverNode;
import com.arthanzel.theriverengine.util.FishMath;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.*;

/**
 * TODO: Documentation
 *
 * @author Martin
 */
public class DiscreteEnvironment implements Environment {
    /**
     * Determines the maximum distance by which two data points may be separated on a given river arc.
     */
    private static final double MAX_SEPARATION = 5;

    private final Map<RiverNode, DiscretePoint> nodeValues = new HashMap<>();
    private final Map<RiverArc, DiscretePoint[]> arcValues = new HashMap<>();
    private final Map<RiverArc, Double> separations = new HashMap<>();

    private DiscreteEnvironment() {}

    public DiscreteEnvironment(RiverNetwork network) {
        // Generate values for each node
        for (RiverNode node : network.vertexSet()) {
            if (network.outgoingEdgesOf(node).size() > 1) {
                nodeValues.put(node, new DiscretePoint(Math.random(), FishMath.sample(network.outgoingEdgesOf(node)), 0));
            }
            else {
                RiverArc arc = FishMath.sample(network.incomingEdgesOf(node));
                nodeValues.put(node, new DiscretePoint(Math.random(), arc, arc.length()));
            }
        }

        // Generate values for every arc, spaced at most MAX_SEPARATION apart.
        for (RiverArc arc : network.edgeSet()) {
            final double len = arc.length();
            final int nPoints = (int) Math.ceil(arc.length() / MAX_SEPARATION) - 1; // Number of data points in this arc, minus the upstream node
            final double separation = len / nPoints;
            separations.put(arc, separation);

            DiscretePoint[] vals = new DiscretePoint[nPoints];
            for (int i = 0; i < vals.length; i++) {
                vals[i] = new DiscretePoint(Math.random(), arc, (i + 1) * separation);
            }
            arcValues.put(arc, vals);
        }
    }

    public double get(RiverArc arc, double pos) {
        if (pos < 0 || pos > arc.length()) {
            throw new IllegalArgumentException("Position must be greater or equal to 0 and less than or equal to the arc's length (" + arc.length() + ")");
        }

        DiscretePoint[] vals = arcValues.get(arc);

        // If the arc is too short to have a gradient, interpolate between the upstream and downstream nodes
        if (vals.length == 0) {
            double startVal = nodeValues.get(arc.getUpstreamNode()).getValue();
            double endVal = nodeValues.get(arc.getDownstreamNode()).getValue();
            return FishMath.lerp(startVal, endVal, pos / arc.length());
        }

        double separation = separations.get(arc);

        // "Virtual index" of given position in the array.
        // E.x. separation is every 5 m. A position of 7.5 would give virtual index 1.5.
        // Virtual index of zero represents the upstream node.
        double vi = pos / separation;

        if (vi > 1 && vi < vals.length - 1) {
            // Interpolate between the neighbouring indices
            int start = (int) vi - 1;
            int end = start + 1;
            return FishMath.lerp(vals[start].getValue(), vals[end].getValue(), vi % 1);
        }
        else if (vi < 1) {
            // Interpolate between the upstream node's value and the first arc value
            return FishMath.lerp(nodeValues.get(arc.getUpstreamNode()).getValue(), vals[0].getValue(), vi % 1);
        }
        else {
            // Interpolate between the downstream node's value and the last arc value
            return FishMath.lerp(vals[vals.length - 1].getValue(), nodeValues.get(arc.getDownstreamNode()).getValue(), vi % 1);
        }
    }

    /**
     * Returns the distance by which two data points are separated on a given RiverArc.
     */
    public double getSeparation(RiverArc arc) {
        return separations.get(arc);
    }

    public void transform(Consumer<DiscretePoint> func) {
        for (DiscretePoint dp : nodeValues.values()) {
            func.accept(dp);
        }
        for (DiscretePoint[] dps : arcValues.values()) {
            for (DiscretePoint dp : dps) {
                func.accept(dp);
            }
        }
    }

     // ====== Accessors ======

    //TODO: Pluggable interpolation functions

    public Environment clone() {
        DiscreteEnvironment env = new DiscreteEnvironment();
        for (RiverNode node : this.nodeValues.keySet()) {
            env.nodeValues.put(node, nodeValues.get(node).clone());
        }
        for (RiverArc arc : this.arcValues.keySet()) {
            DiscretePoint[] values = arcValues.get(arc);
            DiscretePoint[] dps = new DiscretePoint[values.length];
            for (int i = 0; i < values.length; i++) {
                dps[i] = values[i].clone();
            }
            env.arcValues.put(arc, dps);

            env.separations.put(arc, separations.get(arc));
        }
        return env;
    }
}
