package com.arthanzel.theriverengine.sim;

import com.arthanzel.theriverengine.concurrent.QueueMode;
import com.arthanzel.theriverengine.sim.influence.Influence;
import com.arthanzel.theriverengine.util.FrameCounter;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class RiverRunner {
    private final RunnerOptions options = new RunnerOptions();
    final private BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>(64);
    private List<Influence> influences = new LinkedList<>();
    private RiverSystem system;
    private volatile boolean enabled = false;
    private final Object runningMonitor = new Object();
    long lastReportNanos = System.nanoTime();

    private volatile double interval = 0.5;


    public RiverRunner(RiverSystem system) {
        this.system = system;
    }

    public void forward() {
        System.out.println("Forwarding one frame");
        tick(interval);
    }

    private void sendMessage() {
        try {
            String message = system.toJson().toString();

            // Add to the queue
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
    }

    /**
     * Simulates the river system for one iteration.
     *
     * @param dt Time interval. Higher values are quicker, but less precise, and may lead to artifacts.
     */
    public void tick(double dt) {
        for (Influence i : influences) {
            if (!i.isEnabled()) {
                continue;
            }

            i.influence(system, dt);
        }
        system.setTime(system.getTime() + dt);

        // Is it time to trigger a message?
        double timeElapsed = (System.nanoTime() - lastReportNanos) / 1.0e9;
        if (timeElapsed > options.getReportingInterval()) {
            sendMessage();
            lastReportNanos = System.nanoTime();
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
            runningMonitor.notifyAll();
        }
    }

    public BlockingQueue<String> getMessageQueue() {
        return messageQueue;
    }

    public RunnerOptions getOptions() {
        return options;
    }
}
