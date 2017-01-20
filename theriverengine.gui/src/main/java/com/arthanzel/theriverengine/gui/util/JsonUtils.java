package com.arthanzel.theriverengine.gui.util;

import com.google.gson.JsonObject;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**
 * Created by Martin on 2017-01-16.
 */
public class JsonUtils {
    public static Color parseColor(JsonObject obj) {
        return Color.color(
                obj.get("r").getAsDouble(),
                obj.get("g").getAsDouble(),
                obj.get("b").getAsDouble()
        );
    }

    public static Point2D parseLocation(JsonObject obj) {
        return new Point2D(
                obj.get("position").getAsDouble(),
                20
        );
    }
}
