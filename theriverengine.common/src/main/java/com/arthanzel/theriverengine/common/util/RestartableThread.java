package com.arthanzel.theriverengine.common.util;

/**
 * Created by Martin on 2017-01-29.
 */
public class RestartableThread {
    private Thread t;
    private Runnable r;
    private boolean running = false;

    // Flag to stop
    private boolean flag = false;

    public RestartableThread(Runnable r) {
        this.r = r;
    }

    /**
     * Starts or re-starts the thread. Has no effect if the thread is already started.
     */
    public void start() {
        if (t.isAlive()) {
            flag = false;
            return;
        };

        t = new Thread(() -> {
            running = true;
            while (!flag) {
                r.run();
            }
            running = false;
        });
    }

    /**
     * Requests to stop the thread. The thread will finish running its task,
     * then stop. This method does not make any guarantee of when the thread
     * will stop. If start() is invoked before the thread had a chance to stop,
     * the thread will continue as normal.
     */
    public void stop() {
        flag = true;
    }

    /**
     * Determines if the thread is currently running. The thread may be running
     * briefly even though stop() was called.
     */
    public boolean isRunning() {
        return running;
    }
}
