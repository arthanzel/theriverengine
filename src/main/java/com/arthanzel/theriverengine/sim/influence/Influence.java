package com.arthanzel.theriverengine.sim.influence;

import com.arthanzel.theriverengine.sim.RiverSystem;

/**
 * An Influence defines some operation to perform on a RiverSystem. The next state of a RiverSystem can thus be
 * computed by applying a bunch of Influences on it. Influences directly modify a RiverSystem.
 *
 * @author Martin
 */
public interface Influence {
    boolean isEnabled();
    void setEnabled(boolean enabled);

    String getName();

    void influence(RiverSystem system, double dt);
}
