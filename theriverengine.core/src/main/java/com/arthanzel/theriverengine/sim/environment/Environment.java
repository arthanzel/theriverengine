package com.arthanzel.theriverengine.sim.environment;

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

    // ====== Clone ======

    /**
     * Creates a copy of an Environment. This method is static because not every Environment has to implement a clone()
     * method (in fact, requiring a public clone() method would break the functional syntax of Environments). Further,
     * some Environments are stateless, so all references to such Environments are guaranteed to behave the same.
     *
     * This method delegates to a clone() method defined in implementing classes via reflection, if such a method is
     * defined. Otherwise, a reference to the passed Environment is returned.
     *
     * @param env Environment to clone.
     * @return Clone of the environment, or a reference to an immutable environment.
     */
    static Environment clone(Environment env) {
        if (env.getClass() == Environment.class) {
            return env;
        }

        // Delegate to subclass
        try {
            return (Environment) env.getClass().getMethod("clone").invoke(env);
        }
        catch (Exception ex) {
            // Subclass does not implement the proper clone() method
            return env;
        }
    }
}
