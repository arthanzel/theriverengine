package com.arthanzel.theriverengine.sim;

import com.arthanzel.theriverengine.concurrent.ParallelService;
import com.arthanzel.theriverengine.sim.influence.Influence;
import com.arthanzel.theriverengine.util.FrameCounter;
import javafx.event.EventHandler;

import java.util.EventListener;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class RiverRunner {
    public static final int NUM_THREADS = 4;
    public static final int CLONE_INTERVAL_MILLIS = 1000/15; // 15 fps

    private List<Influence> influences = new LinkedList<>();
    private RiverSystem system;
    private boolean flagForStop = false;
    private ParallelService pool;

    // Event handler: called when the RiverSystem is cloned and the UI may be refreshed.
    private Consumer<RiverSystem> refreshHandler;

    // Fair lock that ensures that the RiverRunner does not perform more than one operation on its RiverSystem at once.
    ReentrantLock systemLock = new ReentrantLock(true);

    Thread cloner = new Thread(() -> {
        while (true) {
            try {
                Thread.sleep(CLONE_INTERVAL_MILLIS);
            } catch (InterruptedException e) {
                break;
            }

            // Try to clone
            systemLock.lock();
            try {
                RiverSystem cloned = system.clone();
                if (refreshHandler != null) {
                    refreshHandler.accept(cloned);
                }
            }
            finally {
                systemLock.unlock();
            }
        }
    });

    public RiverRunner(RiverSystem system) {
        this.system = system;
    }

    public void start() {
        pool = new ParallelService(NUM_THREADS);

        // Make sure every Influence has a reference to the thread pool for this runner
        for (Influence i : influences) {
            i.setPool(pool);
        }

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

        cloner.start();
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
        systemLock.lock();
        try {
            for (Influence i : influences) {
                if (!i.isEnabled()) {
                    continue;
                }

                i.influence(system, dt);
            }
        }
        finally {
            systemLock.unlock();
        }
    }

    // ====== Accessors ======

    public RiverSystem getSystem() {
        return system;
    }

    public List<Influence> getInfluences() {
        return influences;
    }

    public Consumer<RiverSystem> getRefreshHandler() {
        return refreshHandler;
    }

    public void setRefreshHandler(Consumer<RiverSystem> refreshHandler) {
        this.refreshHandler = refreshHandler;
    }
}
