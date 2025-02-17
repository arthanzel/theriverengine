package com.arthanzel.theriverengine.sim;

import com.arthanzel.theriverengine.reporting.RiverReporter;
import com.arthanzel.theriverengine.sim.influence.Influence;
import com.google.gson.JsonObject;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * RiverRunner is the heart of The River Engine's simulation. It handles the
 * details of simulating a river system.
 *
 * <p>A RiverRunner continually simulates a river system and, at regular
 * intervals, sends messages containing the state of the system to a
 * RiverReporter. A RiverReporter is invariably bound to a RiverRunner.</p>
 */
public class RiverRunner {
    // RiverReporter receives messages in parallel.
    private final RiverReporter reporter;

    // Various options concerning how the sim is run and reported.
    private final RunnerOptions options = new RunnerOptions();

    // Queue containing messages that contain the state of the system.
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>(64);

    // Influences affect the behaviour of the river system.
    private final List<Influence> influences = new LinkedList<>();

    // The system to be simulated. Rather important.
    private final RiverSystem system;

    // Support pausing of the system.
    private volatile boolean enabled = false;
    private final Object runningMonitor = new Object();

    // Support finite quit time
    private double endTime = 0.0;
    private boolean hasQuit = false;
    private final Object quitMonitor = new Object();

    // Time of the last report.
    private long lastReportNanos = System.nanoTime();

    private volatile double interval = 0.5;

    public RiverRunner(RiverSystem system) {
        this.system = system;
        this.reporter = new RiverReporter(messageQueue);
    }

    public void forward() {
        System.out.println("Forwarding one frame");
        setEnabled(false);
        tick(interval, true);
    }

    private void sendMessage() {
        try {
            JsonObject json = system.toJson();

            // Add to the queue
            String message = json.toString();
            if (messageQueue.remainingCapacity() == 0) {
                System.err.println("Queue is full!");
            }
            switch (options.getQueueMode()) {
                case BLOCK:
                    messageQueue.put(message);
                    break;
                case DROP:
                    synchronized (messageQueue) {
                        if (messageQueue.remainingCapacity() == 0) {
                            messageQueue.poll();
                        }
                        messageQueue.add(message);
                    }
                    break;
                case SKIP:
                    messageQueue.offer(message);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.err.println("Interrupted!");
        }
    }

    public void start() {
        Thread t = new Thread(() -> {
            while (endTime != 0.0 && system.getTime() < endTime) {
                while (!enabled) {
                    try {
                        synchronized (runningMonitor) {
                            runningMonitor.wait();
                        }
                    } catch (InterruptedException e) {
                        System.out.println("RiverRunner: interrupted while paused.");
                    }
                }

                tick(interval); // tick takes an interval in seconds
            }
            sendMessage();
            hasQuit = true;
            synchronized (quitMonitor) {
                quitMonitor.notify();
            }
        });
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
        reporter.start();
    }

    public void tick(double dt) {
        tick(dt, false);
    }

    /**
     * Simulates the river system for one iteration.
     *
     * @param dt Time interval. Higher values are quicker, but less precise, and may lead to artifacts.
     */
    public void tick(double dt, boolean forceMessage) {
        for (Influence i : influences) {
            if (!i.isEnabled()) {
                continue;
            }

            i.influence(system, dt);
        }

        // Is it time to trigger a message?
        double timeElapsed = (System.nanoTime() - lastReportNanos) / 1.0e9;
        if (forceMessage || system.getTime() == 0 || timeElapsed > options.getReportingInterval()) {
            sendMessage();
            lastReportNanos = System.nanoTime();
        }

        system.setTime(system.getTime() + dt);
    }

    public void waitFor() {
        while (!hasQuit) {
            synchronized (quitMonitor) {
                try {
                    quitMonitor.wait();
                } catch (InterruptedException e) {
                    return;
                }
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
            runningMonitor.notifyAll(); // Wake the thread from pause
        }
    }

    public BlockingQueue<String> getMessageQueue() {
        return messageQueue;
    }

    public RunnerOptions getOptions() {
        return options;
    }

    public RiverReporter getReporter() {
        return reporter;
    }

    public double getEndTime() {
        return endTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }
}
