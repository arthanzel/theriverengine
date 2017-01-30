package com.arthanzel.theriverengine.sim.environment;

import com.arthanzel.theriverengine.common.rivergen.RiverArc;
import com.arthanzel.theriverengine.common.rivergen.RiverNetwork;
import com.arthanzel.theriverengine.common.rivergen.RiverNode;
import com.arthanzel.theriverengine.common.util.FishMath;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
            if (network.outgoingEdgesOf(node).size() > 0) {
                RiverArc arc = network.outgoingEdgesOf(node).iterator().next();
                nodeValues.put(node, new DiscretePoint(Math.random(), arc, 0, true));
            }
            else {
                RiverArc arc = network.incomingEdgesOf(node).iterator().next();
                nodeValues.put(node, new DiscretePoint(Math.random(), arc, arc.length(), true));
            }
        }

        // Generate values for every arc, spaced at most MAX_SEPARATION apart.
        for (RiverArc arc : network.edgeSet()) {
            final double len = arc.length();
            final int nPoints = (int) Math.ceil(arc.length() / MAX_SEPARATION) + 1; // Include node termini
            final double separation = len / (nPoints - 1);
            separations.put(arc, separation);

            DiscretePoint[] vals = new DiscretePoint[nPoints];
            vals[0] = nodeValues.get(arc.getUpstreamNode());
            vals[vals.length - 1] = nodeValues.get(arc.getDownstreamNode());
            for (int i = 1; i < vals.length - 1; i++) {
                vals[i] = new DiscretePoint(Math.random(), arc, (i) * separation);

            }
            arcValues.put(arc, vals);
        }
    }

    /**
     * Checks whether pos is a valid position in the given arc and raises an exception if it is not.
     */
    private void checkPos(RiverArc arc, double pos) {
        if (pos < 0 || pos > arc.length()) {
            throw new IllegalArgumentException("Position must be greater or equal to 0 and less than or equal to the arc's length (" + arc.length() + ")");
        }
    }

    public double get(RiverArc arc, double pos) {
        checkPos(arc, pos);

        // Interpolate between two neighbouring data points
        DiscretePoint[] vals = arcValues.get(arc);
        double v = getVirtualIndex(arc, pos);
        int v1 = (int) v;

        // Optimize if virtual index is exact, and prevent out-of-bounds
        // if it is exactly at the end of the array.
        if (v % 1 == 0 || pos == arc.length()) {
            return vals[v1].getValue();
        }

        int v2 = v1 + 1;
        return FishMath.lerp(vals[v1].getValue(), vals[v2].getValue(), v % 1);
    }

    public DiscretePoint[] getPoints(RiverArc arc) {
        return arcValues.get(arc);
    }

    /**
     *
     * @param arc
     * @param pos
     * @return
     */
    public double getVirtualIndex(RiverArc arc, double pos) {
        checkPos(arc, pos);
        return pos / separations.get(arc);
    }

    public double getPosition(RiverArc arc, double vi) {
        return getSeparation(arc) * vi;
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
            // Omit the first and last, since they are actually nodes
            for (int i = 1; i < dps.length - 1; i++) {
                func.accept(dps[i]);
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

    public JsonObject toJson() {
        JsonObject me = new JsonObject();

        JsonObject nodes = new JsonObject();
        for (RiverNode node : this.nodeValues.keySet()) {
            nodes.addProperty(node.getName(), String.format("%.2f", nodeValues.get(node).getValue()));
        }
        me.add("nodes", nodes);

        JsonObject arcs = new JsonObject();
        for (RiverArc arc : this.arcValues.keySet()) {
            JsonArray arr = new JsonArray();
            for (DiscretePoint dp : arcValues.get(arc)) {
                arr.add(String.format("%.2f", dp.getValue()));
            }
            arcs.add(arc.toString(), arr);
        }
        me.add("arcs", arcs);

        return me;
    }
}
