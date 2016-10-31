package com.arthanzel.theriverengine.sim.environment;

import com.arthanzel.theriverengine.rivergen.RiverArc;
import javafx.geometry.Point2D;

import java.util.function.ToDoubleBiFunction;

/**
 * Defines an environment, which is a structure that stores or determines some value relating to an environmental
 * factor, such as temperature or foliage cover, as a function of a point along some RiverArc.
 *
 * This is a functional interface.
 *
 * @author martin
 */
public interface Environment {
    /**
     * Creates a new Environment that follows a given two-dimensional function. This allows the creation of continuous
     * gradients or non-deterministic environments.
     *
     * @param fn Functional interface whose method accepts x and y coordinates, and
     *           returns a double that represents the state of the environment at that point.
     * @return An instance of an anonymous sub-class of Environment whose behaviour is defined by the given function.
     */
    static Environment fromCoordinateFunction(ToDoubleBiFunction<Double, Double> fn) {
        Environment env = (arc, position) -> {
            Point2D p = arc.getPoint(position);
            return fn.applyAsDouble(p.getX(), p.getY());
        };
        return env;
    }

    double get(RiverArc arc, double position);
}
