package com.arthanzel.theriverengine.util;

import com.arthanzel.theriverengine.common.data.JsonSerializable;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.scene.paint.Color;

import java.util.HashMap;

/**
 * MultitypeHashMap is an extension of HashMap<String, Object> that allows for
 * retrieving entries and casting them directly to their appropriate types.
 * Getters are defined for double, float, int, long, and String. If a getter is
 * used to retrieve a value that does not fit the type, a ClassCastException is
 * thrown. If the value doesn't exist in the map, a NullPointerException is
 * thrown.
 *
 * @author martin
 */
public class MultitypeHashMap extends HashMap<String, Object> implements JsonSerializable {
    private static final long serialVersionUID = -5500702486726234370L;

    public boolean getBoolean(String key) {
        return (Boolean) this.get(key);
    }

    public double getDouble(String key) {
        return (Double) this.get(key);
    }

    public int getInt(String key) {
        return (Integer) this.get(key);
    }

    public long getLong(String key) {
        return (Long) this.get(key);
    }

    public String getString(String key) {
        return (String) this.get(key);
    }

    @Override
    public JsonObject toJson() {
        JsonObject me = new JsonObject();
        for (String k : this.keySet()) {
            Object v = this.get(k);
            if (v instanceof Number) {
                me.addProperty(k, (Number) v);
            }
            else if (v instanceof Boolean) {
                me.addProperty(k, (Boolean) v);
            }
            else if (v instanceof String) {
                me.addProperty(k, (String) v);
            }
            else if (v instanceof Color) {
                Color clr = (Color) v;
                JsonObject c = new JsonObject();
                c.addProperty("r", String.format("%.2f", clr.getRed()));
                c.addProperty("g", String.format("%.2f", clr.getGreen()));
                c.addProperty("b", String.format("%.2f", clr.getBlue()));
                me.add(k, c);
            }
            else {
                me.add(k, new Gson().toJsonTree(v));
            }
        }
        return me;
    }
}