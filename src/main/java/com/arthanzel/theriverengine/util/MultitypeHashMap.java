package com.arthanzel.theriverengine.util;

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
public class MultitypeHashMap extends HashMap<String, Object> {
    private static final long serialVersionUID = -5500702486726234370L;

    public double getDouble(String key) {
        return (Double) this.get(key);
    }

    public double getFloat(String key) {
        return (Float) this.get(key);
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
}