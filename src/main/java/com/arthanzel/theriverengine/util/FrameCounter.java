package com.arthanzel.theriverengine.util;

import java.util.function.DoubleConsumer;

/**
 * FrameCounter keeps track of the number of operations performed over a given
 * interval. Consumers use the increment() method to indicate that some
 * operation has occurred. FrameCounter will print the average number of
 * operations per second to the standard output.
 *
 * @author martin
 */
public class FrameCounter {
    private volatile int frames = 0;
    private final Thread sleeper;
    private final int interval;
    private final String name;
    private boolean printToOut = false;
    private double fps = 0;
    private DoubleConsumer onReport;

    /**
     * Creates a new FrameCounter with a given name and a reporting interval.
     * Call start() to start the frame counter.
     *
     * @param name
     *            Name.
     * @param interval
     *            Reporting interval, in milliseconds.
     */
    public FrameCounter(final String name, final int interval) {
        this.name = name;
        this.interval = interval;
        this.sleeper = createSleeper();
    }

    public Thread createSleeper() {
        // The sleeper thread will sleep for a specified duration, then print
        // out the number of frames.
        return new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(interval);
                    fps = 1000 * frames / interval;
                    if (printToOut) {
                        System.out.println("Frame counter '" + name + "' averaged " + fps + " fps");
                    }
                    if (onReport != null) {
                        onReport.accept(fps);
                    }
                    frames = 0;
                }
            } catch (InterruptedException e) {
                // Frame counter has stopped. Reset state.
                frames = 0;
                fps = 0;
            }
        });
    }

    /**
     * Increments this frame counter.
     */
    public void increment() {
        this.frames++;
    }

    /**
     * Starts this frame counter.
     */
    public void start() {
        sleeper.start();
    }

    /**
     * Stops this frame counter.
     */
    public void stop() {
        sleeper.interrupt();
    }

    // ====== Accessors ======

    public boolean isPrintToOut() {
        return printToOut;
    }

    public void setPrintToOut(boolean printToOut) {
        this.printToOut = printToOut;
    }

    public double getFps() {
        return fps;
    }

    public void setOnReport(DoubleConsumer onReport) {
        this.onReport = onReport;
    }
}