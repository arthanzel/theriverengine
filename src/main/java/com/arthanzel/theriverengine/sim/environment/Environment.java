package com.arthanzel.theriverengine.sim.environment;

import com.arthanzel.theriverengine.rivergen.RiverArc;
import javafx.geometry.Point2D;

import java.util.function.ToDoubleBiFunction;

/**
 * Defines an environment, which is a structure that stores or determines some value relating to an environmental
 * factor, such as temperature or foliage cover, as a function of a point along some RiverArc.
 *
 * @author martin
 */
public interface Environment {
    double get(RiverArc arc, double position);

    /**
     *
     * @param val
     * @return
     */
    double toFraction(double val);
}
