package com.arthanzel.theriverengine.sim.agent;

import com.arthanzel.theriverengine.common.data.JsonSerializable;
import com.arthanzel.theriverengine.common.util.FishMath;
import com.arthanzel.theriverengine.util.MultitypeHashMap;
import com.google.gson.JsonObject;
import javafx.scene.paint.Color;

/**
 * Represents an autonomous Agent that acts in an agent-based model.
 *
 * @author Martin
 */
public class Agent implements JsonSerializable {
    private MultitypeHashMap attributes = new MultitypeHashMap();
    private Location location;

    public Agent() {
        attributes.put("color", Color.hsb(Math.random() * 360, 1, FishMath.random(0.75, 1)));
        attributes.put("energy", 2.0);
        attributes.put("velocity", 0.0);
    }

    @Override
    public JsonObject toJson() {
        JsonObject me = new JsonObject();
        me.add("location", location.toJson());

        JsonObject attrs = attributes.toJson();
        me.add("attributes", attributes.toJson());


        return me;
    }

    // ====== Accessors ======

    public MultitypeHashMap getAttributes() {
        return attributes;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
