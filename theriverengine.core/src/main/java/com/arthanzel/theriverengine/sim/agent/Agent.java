package com.arthanzel.theriverengine.sim.agent;

import com.arthanzel.theriverengine.common.data.JsonSerializable;
import com.arthanzel.theriverengine.common.rivergen.RiverNetwork;
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
        attributes.put("hue", FishMath.randomInt(0, 360));
        attributes.put("energy", 4.0);
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
