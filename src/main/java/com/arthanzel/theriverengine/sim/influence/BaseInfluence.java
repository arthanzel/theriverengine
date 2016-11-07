package com.arthanzel.theriverengine.sim.influence;

import com.arthanzel.theriverengine.concurrent.ParallelService;
import com.arthanzel.theriverengine.ui.BooleanBinding;
import com.arthanzel.theriverengine.util.TextUtils;

import java.util.concurrent.ExecutorService;

/**
 * Base implementation of an Influence. BaseInfluence does very little except provide getters and setters for the
 * influence's "enabled" flag. Every influence should extend BaseInfluence.
 *
 * @author Martin
 */
public abstract class BaseInfluence implements Influence {
    @BooleanBinding private volatile boolean enabled = true;
    private ParallelService pool;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public ParallelService getPool() {
        return pool;
    }

    public void setPool(ParallelService pool) {
        this.pool = pool;
    }
}
