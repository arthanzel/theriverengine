package com.arthanzel.theriverengine.sim.influence;

import com.arthanzel.theriverengine.common.ui.binding.BooleanBinding;

/**
 * Base implementation of an Influence. BaseInfluence does very little except provide getters and setters for the
 * influence's "enabled" flag. Every influence should extend BaseInfluence.
 *
 * @author Martin
 */
public abstract class BaseInfluence implements Influence {
    @BooleanBinding private volatile boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
