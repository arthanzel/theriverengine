package com.arthanzel.theriverengine.sim.environment;

import com.arthanzel.theriverengine.common.rivergen.RiverArc;

import java.util.HashSet;
import java.util.Set;

/**
 * TODO: Documentation
 *
 * @author Martin
 */
public class DiscretePoint {
    private double value;
    private final RiverArc arc;
    private final double position;
    private final Set<DiscreteNeighbor> neighbors = new HashSet<>();

    public class DiscreteNeighbor {
        private final double distance;
        private final DiscretePoint point;
        public DiscreteNeighbor(DiscretePoint point, double distance) {
            this.distance = distance;
            this.point = point;
        }

        public double getDistance() {
            return distance;
        }

        public DiscretePoint getPoint() {
            return point;
        }
    }

    DiscretePoint(double value, RiverArc arc, double position) {
        this.value = value;
        this.arc = arc;
        this.position = position;
    }

    public void addNeighbor(DiscretePoint p2, double distance) {
        for (DiscreteNeighbor n : neighbors) {
            if (n.getPoint() == p2) {
                return;
            }
        }
        this.neighbors.add(new DiscreteNeighbor(p2, distance));
        p2.getNeighbors().add(new DiscreteNeighbor(this, distance));
    }

    public double area() {
        double a = 0;
        for (DiscreteNeighbor n : neighbors) {
            a += n.getDistance() / 2;
        }
        return a;
    }

    public String toString() {
        return String.format("%f @ %s (%f)", value, arc.toString(), position);
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

    public Set<DiscreteNeighbor> getNeighbors() {
        return neighbors;
    }
}
