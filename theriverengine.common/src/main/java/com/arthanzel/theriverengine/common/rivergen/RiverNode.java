package com.arthanzel.theriverengine.common.rivergen;

import com.arthanzel.theriverengine.common.data.JsonSerializable;
import com.google.gson.JsonObject;
import javafx.geometry.Point2D;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a node in a river network where the river changes direction or branches into two more more runs.
 *
 * @author Martin
 */
public class RiverNode implements JsonSerializable {
    private final String name;
    private final Point2D position;
    private Set<RiverArc> downstreamArcs = new HashSet<>();
    private Set<RiverArc> upstreamArcs = new HashSet<>();

    public RiverNode(String name, double x, double y) {
        this(name, new Point2D(x, y));
    }

    public RiverNode(String name, Point2D position) {
        this.name = name;
        this.position = position;
    }

    @Override
    public JsonObject toJson() {
        JsonObject me = new JsonObject();
        me.addProperty("x", position.getX());
        me.addProperty("y", position.getY());
        return me;
    }

    public String toString() {
        return name + "(" + position.getX() + ", " + position.getY() + ")";
    }

    // ====== Accessors ======

    public String getName() {
        return name;
    }

    public Point2D getPosition() {
        return position;
    }

    public Set<RiverArc> getDownstreamArcs() {
        return downstreamArcs;
    }

    public Set<RiverArc> getUpstreamArcs() {
        return upstreamArcs;
    }
}
