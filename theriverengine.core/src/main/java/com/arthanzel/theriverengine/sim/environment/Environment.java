package com.arthanzel.theriverengine.sim.environment;

import com.arthanzel.theriverengine.common.data.JsonSerializable;
import com.arthanzel.theriverengine.common.rivergen.RiverArc;
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
public interface Environment extends JsonSerializable {
//    static Environment fromCoordinateFunction(ToDoubleBiFunction<Double, Double> fn) {
//        Environment env = (arc, position) -> {
//            Point2D p = arc.getPoint(position);
//            return fn.applyAsDouble(p.getX(), p.getY());
//        };
//        return env;
//    }

    double get(RiverArc arc, double position);
}
