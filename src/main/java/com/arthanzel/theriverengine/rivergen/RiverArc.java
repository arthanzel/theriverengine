package com.arthanzel.theriverengine.rivergen;

import javafx.geometry.Point2D;

/**
 * Represents a run of a river system between two RiverNodes.
 *
 * @author Martin
 */
public class RiverArc {
    private RiverNode upstreamNode, downstreamNode;
    private double len;

    public RiverArc() {}

    public RiverArc(RiverNode upstream, RiverNode downstream) {
        this.upstreamNode = upstream;
        this.downstreamNode = downstream;
    }

    /**
     * TODO
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
        if (f < 0 || f > this.length()) {
            throw new IllegalArgumentException("Position must be between 0 and 1.");
        }

        return getPoint(f * length());
    }

    public double length() {
        if (len != 0) {
            return len;
        }
        if (upstreamNode == null || downstreamNode == null) {
            return 0;
        }
        len = upstreamNode.getPosition().distance(downstreamNode.getPosition());
        return len;
    }

    public String toString() {
        if (upstreamNode == null || downstreamNode == null) {
            return "Unconnected Arc";
        }
        return upstreamNode.getName() + " -> " + downstreamNode.getName();
    }

    // ====== Accessors ======

    public RiverNode getUpstreamNode() {
        return upstreamNode;
    }

    public void setUpstreamNode(RiverNode upstreamNode) {
        this.upstreamNode = upstreamNode;
    }

    public RiverNode getDownstreamNode() {
        return downstreamNode;
    }

    public void setDownstreamNode(RiverNode downstreamNode) {
        this.downstreamNode = downstreamNode;
    }
}
