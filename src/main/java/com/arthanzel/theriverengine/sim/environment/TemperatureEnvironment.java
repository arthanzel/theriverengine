package com.arthanzel.theriverengine.sim.environment;

import com.arthanzel.theriverengine.rivergen.RiverArc;
import javafx.geometry.Point2D;

/**
 * Created by martin on 2016-11-02.
 */
public class TemperatureEnvironment implements Environment {
    // TODO: Refactor into LinearEnvironment with slope, intercept, angle

    public double get(RiverArc arc, double position) {
        Point2D p = arc.getPoint(position);
        return 0.01 * p.getY() * 2 + 14;
    }
}
