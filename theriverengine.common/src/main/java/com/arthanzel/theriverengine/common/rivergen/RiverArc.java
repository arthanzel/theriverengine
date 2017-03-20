package com.arthanzel.theriverengine.common.rivergen;

import com.arthanzel.theriverengine.common.data.JsonSerializable;
import com.google.gson.JsonObject;
import javafx.geometry.Point2D;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a reach of a river system between two RiverNodes.
 *
 * @author Martin
 */
public class RiverArc implements JsonSerializable {
    private final RiverNode upstreamNode, downstreamNode;
    private Set<RiverArc> upstreamArcs = new HashSet<>(), downstreamArcs = new HashSet<>();
    private final double len;

    public RiverArc(RiverNode upstream, RiverNode downstream) {
        this.upstreamNode = upstream;
        this.downstreamNode = downstream;
        this.len = upstreamNode.getPosition().distance(downstreamNode.getPosition());
    }

    /**
     * Returns the cartesian coordinates of a point {@code p} units downstream
     * of this RiverArc's source.
     */
    public Point2D getPoint(double p) {
        if (p < 0 || p > this.length()) {
            throw new IllegalArgumentException("Position must be greater or equal to 0 and less than or equal to the arc's length (" + this.length() + ")");
        }

        Point2D origin = this.getUpstreamNode().getPosition();
        Point2D dest = this.getDownstreamNode().getPosition();
        double pn = p / length();
        return new Point2D(pn * (dest.getX() - origin.getX()) + origin.getX(),
                pn * (dest.getY() - origin.getY()) + origin.getY());
    }

    public Point2D getPointLerp(double f) {
        return getPoint(f * length());
    }

    public double length() {
        return len;
    }

    public JsonObject toJson() {
        JsonObject me = new JsonObject();
        me.addProperty("up", upstreamNode.getName());
        me.addProperty("down", downstreamNode.getName());
        return me;
    }

    public String toString() {
        if (upstreamNode == null || downstreamNode == null) {
            return "Unconnected Arc";
        }
        return upstreamNode.getName() + "_" + downstreamNode.getName();
    }

    // ====== Accessors ======

    public RiverNode getUpstreamNode() {
        return upstreamNode;
    }

    public RiverNode getDownstreamNode() {
        return downstreamNode;
    }

    public Set<RiverArc> getUpstreamArcs() {
        return upstreamArcs;
    }

    public Set<RiverArc> getDownstreamArcs() {
        return downstreamArcs;
    }
}
