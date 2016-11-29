package com.arthanzel.theriverengine.sim.environment;

import com.arthanzel.theriverengine.rivergen.RiverArc;
import javafx.geometry.Point2D;

/**
 * TODO: Documentation
 *
 * @author Martin
 */
public class DiscretePoint {
    private double value;
    final RiverArc arc;
    final double position;
    final boolean isNode;

    DiscretePoint(double value, RiverArc arc, double position) {
        this(value, arc, position, false);
    }

    DiscretePoint(double value, RiverArc arc, double position, boolean isNode) {
        this.value = value;
        this.arc = arc;
        this.position = position;
        this.isNode = isNode;
    }

    public DiscretePoint clone() {
        return new DiscretePoint(value, arc, position, isNode);
    }

    public double getValue() {
        return value;
    }

    public RiverArc getArc() {
        return arc;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getPosition() {
        return position;
    }

    public boolean isNode() {
        return isNode;
    }
}
