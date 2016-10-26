package com.arthanzel.theriverengine.sim;

import com.arthanzel.theriverengine.sim.influence.Influence;
import com.arthanzel.theriverengine.util.FrameCounter;

import java.util.LinkedList;
import java.util.List;

public class RiverRunner {
    private List<Influence> influences = new LinkedList<>();
    private final RiverSystem system;
    private boolean flagForStop = false;

    public RiverRunner(RiverSystem system) {
        this.system = system;
    }

    public void start() {
        Thread t = new Thread(() -> {
            FrameCounter counter = new FrameCounter("Simulation", 1000);
            counter.setPrintToOut(true);
            counter.start();
            while (true) {
                counter.increment();
                tick(1/500.0);

                system.setTime(system.getTime() + 500);

                if (flagForStop) {
                    break;
                }
            }
        });
        t.start();
    }

    public void stop() {
        this.flagForStop = true;
    }

    /**
     * Simulates the river system for one iteration.
     *
     * @param dt Time interval. Higher values are quicker, but less precise, and may lead to artifacts.
     */
    public void tick(double dt) {
        synchronized (system) {
            for (Influence i : influences) {
                i.influence(system, dt);
            }
        }
    }

    // ====== Accessors ======

    public RiverSystem getSystem() {
        return system;
    }

    public List<Influence> getInfluences() {
        return influences;
    }
}
