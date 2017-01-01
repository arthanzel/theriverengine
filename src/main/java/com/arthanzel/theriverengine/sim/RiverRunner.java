package com.arthanzel.theriverengine.sim;

import com.arthanzel.theriverengine.sim.influence.Influence;
import com.arthanzel.theriverengine.util.FrameCounter;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class RiverRunner {
//    public static final int NUM_THREADS = 1;
    public static final int CLONE_INTERVAL_MILLIS = 1000 / 15; // 15 fps

    private List<Influence> influences = new LinkedList<>();
    private RiverSystem system;
    private volatile boolean enabled = false;
    private final Object runningMonitor = new Object();

    private volatile double interval = 0.5;

    // Event handler: called when the RiverSystem is cloned and the UI may be refreshed.
    private Consumer<RiverSystem> refreshHandler;

    // Fair lock that ensures that the RiverRunner does not perform more than one operation on its RiverSystem at once.
    private ReentrantLock systemLock = new ReentrantLock(true);

    // Thread that tries to clone the RiverSystem at a certain interval and calls an event handler.
    private Thread cloner = new Thread(() -> {
        while (true) {
            try {
                Thread.sleep(CLONE_INTERVAL_MILLIS);
            } catch (InterruptedException e) {
                break;
            }
            if (Thread.interrupted()) {
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

    public void forward() {
        System.out.println("Forwarding one frame");
        tick(interval);
    }

    public void start() {
        Thread t = new Thread(() -> {
            FrameCounter counter = new FrameCounter("Simulation", 1000);
            counter.setPrintToOut(true);
            counter.start();
            while (true) {
                while (!enabled) {
                    try {
                        synchronized (runningMonitor) {
                            runningMonitor.wait();
                        }
                    } catch (InterruptedException e) {
                        System.out.println("RiverRunner: interrupted while paused.");
                    }
                }

                counter.increment();
                tick(interval); // tick takes an interval in seconds
            }
        });
        t.start();

        cloner.start();
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
                //pool.waitForCompletion();
            }
        }
        finally {
            systemLock.unlock();
            system.setTime(system.getTime() + (long) (dt * 1000));
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

    public double getInterval() {
        return interval;
    }

    public void setInterval(double interval) {
        this.interval = interval;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        synchronized (runningMonitor) {
            runningMonitor.notifyAll();
        }
    }
}
