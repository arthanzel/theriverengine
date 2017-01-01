package com.arthanzel.theriverengine.rivergen;

import javafx.geometry.Point2D;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a node in a river network where the river changes direction or branches into two more more runs.
 *
 * @author Martin
 */
public class RiverNode {
    private String name = "";
    private Point2D position = Point2D.ZERO;
    private Set<RiverArc> downstreamArcs = new HashSet<>();
    private Set<RiverArc> upstreamArcs = new HashSet<>();

    public RiverNode() {
        this.name = UUID.randomUUID().toString();
    }

    public RiverNode(String name, double x, double y) {
        this(name, new Point2D(x, y));
    }

    public RiverNode(String name, Point2D position) {
        this.name = name;
        this.position = position;
    }

    public String toString() {
        return name + "(" + position.getX() + ", " + position.getY() + ")";
    }

    // ====== Accessors ======

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public Set<RiverArc> getDownstreamArcs() {
        return downstreamArcs;
    }

    public Set<RiverArc> getUpstreamArcs() {
        return upstreamArcs;
    }
}
