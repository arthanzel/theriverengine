package com.arthanzel.theriverengine.gui.util;

import com.google.gson.JsonObject;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Martin on 2017-01-16.
 */
public class JsonUtils {
    public static List<String> keys(JsonObject o) {
        LinkedList<String> k = new LinkedList<>();
        for (Map.Entry<String, ?> e : o.entrySet()) {
            k.add(e.getKey());
        }
        return k;
    }

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
