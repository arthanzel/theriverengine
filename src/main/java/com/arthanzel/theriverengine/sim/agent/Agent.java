package com.arthanzel.theriverengine.sim.agent;

import com.arthanzel.theriverengine.sim.RiverSystem;
import com.arthanzel.theriverengine.util.FishMath;
import com.arthanzel.theriverengine.util.MultitypeHashMap;
import javafx.scene.paint.Color;

/**
 * Represents an autonomous Agent that acts in an agent-based model.
 *
 * @author Martin
 */
public class Agent {
    private MultitypeHashMap attributes = new MultitypeHashMap();
    private Location location;

    public Agent() {
        attributes.put("color", Color.hsb(Math.random() * 360, 1, FishMath.random(0.75, 1)));
        attributes.put("velocity", 0.0);
    }

    public Agent clone() {
        Agent a = new Agent();
        // The river network is immutable, so we can get away with a reference to the arc
        a.location = new Location(location.getArc(), location.getPosition());

        a.attributes = attributes.clone();

        return a;
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
