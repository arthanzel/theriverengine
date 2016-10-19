package com.arthanzel.theriverengine.sim.agent;

import com.arthanzel.theriverengine.rivergen.RiverArc;
import javafx.geometry.Point2D;

/**
 * Location specifies a deterministic location on a river network using a reference to an arc, and a downstream position
 * along that arc.
 *
 * @author Martin
 */
public class Location {
    private RiverArc arc;
    private double position;

    public Location(RiverArc arc, double position) {
        this.arc = arc;
        this.setPosition(position);
    }

    /**
     * Returns a point on the 2D plane represented by this Location.
     * @return Point on the 2D plane.
     */
    public Point2D getPoint() {
        Point2D origin = arc.getUpstreamNode().getPosition();
        Point2D dest = arc.getDownstreamNode().getPosition();
        Point2D delta = dest.subtract(origin).normalize().multiply(position);
        return origin.add(delta);
    }

    public String toString() {
        return "Location(" + arc + " @ " + position + ")";
    }

    // ====== Accessors ======

    public RiverArc getArc() {
        return arc;
    }

    public void setArc(RiverArc arc) {
        this.arc = arc;
    }

    public double getPosition() {
        return position;
    }

    public void setPosition(double position) {
        if (position > this.arc.length() || position < 0) {
            throw new IllegalArgumentException("Position must be less than the length of the river arc.");
        }

        this.position = position;
    }
}
