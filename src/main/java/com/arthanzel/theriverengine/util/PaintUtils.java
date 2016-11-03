package com.arthanzel.theriverengine.util;

import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;

/**
 * Created by martin on 2016-11-02.
 */
public class PaintUtils {
    public static final Stop[] HSB_STOPS = new Stop[40];
    static {
        for (int i = 0; i < HSB_STOPS.length; i++) {
            final double f = 1.0 * i / (HSB_STOPS.length - 1);
            HSB_STOPS[i] = new Stop(f, Color.hsb(f * 360, 1, 1));
        }
    }
}
