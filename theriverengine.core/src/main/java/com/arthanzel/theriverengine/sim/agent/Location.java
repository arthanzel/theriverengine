package com.arthanzel.theriverengine.sim.agent;

import com.arthanzel.theriverengine.common.data.JsonSerializable;
import com.arthanzel.theriverengine.common.rivergen.RiverArc;
import com.google.gson.JsonObject;
import javafx.geometry.Point2D;

/**
 * Location specifies a deterministic location on a river network using a reference to an arc, and a downstream position
 * along that arc.
 *
 * @author Martin
 */
public class Location implements JsonSerializable {
    private RiverArc arc;
    private double position;

    public Location(RiverArc arc, double position) {
        this.arc = arc;
        this.setPosition(position);
    }

    public static Location fromLerp(RiverArc arc, double f) {
        if (f > 1 || f < 0) {
            throw new IllegalArgumentException("Position must be between 0 and 1.");
        }

        return new Location(arc, f * arc.length());
    }

    /**
     * Returns a point on the 2D plane represented by this Location.
     * @return Point on the 2D plane.
     */
    public Point2D getPoint() {
        return arc.getPoint(position);
    }

    @Override
    public JsonObject toJson() {
        JsonObject me = new JsonObject();
        me.addProperty("arc", arc.toString());
        me.addProperty("position", String.format("%.2f", position));
        return me;
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
