package com.arthanzel.theriverengine.rivergen;

import javafx.geometry.Point2D;

/**
 * Represents a run of a river system between two RiverNodes.
 *
 * @author Martin
 */
public class RiverArc {
    private RiverNode upstreamNode, downstreamNode;

    public RiverArc() {}

    public RiverArc(RiverNode upstream, RiverNode downstream) {
        this.upstreamNode = upstream;
        this.downstreamNode = downstream;
    }

    public double length() {
        if (upstreamNode == null || downstreamNode == null) {
            return 0;
        }
        return upstreamNode.getPosition().distance(downstreamNode.getPosition());
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
