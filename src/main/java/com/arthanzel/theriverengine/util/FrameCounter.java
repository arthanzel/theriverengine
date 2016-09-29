package com.arthanzel.theriverengine.util;

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

    /**
     * Creates a new FrameCounter with a given name and a reporting interval.
     * Call start() to start the frame counter.
     *
     * @param name
     *            Name.
     * @param millis
     *            Reporting interval, in milliseconds.
     */
    public FrameCounter(final String name, final long millis) {
        // The sleeper thread will sleep for a specified duration, then print
        // out the number of frames.
        Runnable r = () -> {
            try {
                while (true) {
                    Thread.sleep(millis);
                    double fps = 1.0 * frames / (millis / 1000);
                    System.out.println("Frame counter '" + name + "' averaged " + fps + " fps");
                    frames = 0;
                }
            } catch (InterruptedException e) {
                System.out.println("Frame counter '" + name + "' interrupted.");
            }
        };
        sleeper = new Thread(r);
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
}