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

    DiscretePoint(double value, RiverArc arc, double position) {
        this.value = value;
        this.arc = arc;
        this.position = position;
    }

    public DiscretePoint clone() {
        return new DiscretePoint(value, arc, position);
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
}
