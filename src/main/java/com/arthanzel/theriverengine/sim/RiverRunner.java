package com.arthanzel.theriverengine.sim;

/**
 * Created by martin on 2016-10-06.
 */
public class RiverRunner {
    private RiverSystem system;

    public RiverRunner(RiverSystem system) {
        this.system = system;
    }

    /**
     * Simulates the river system for one iteration.
     *
     * @param dt Time interval. Higher values are quicker, but less precise, and may lead to artifacts.
     */
    public void tick(double dt) {

    }

    // ====== Accessors ======

    public RiverSystem getSystem() {
        return system;
    }
}
