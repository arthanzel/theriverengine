package com.arthanzel.theriverengine.concurrent;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * TODO: Documentation
 *
 * @author Martin
 */
public class ParallelService {
    private BlockingQueue<Runnable> tasks;
    private Thread[] workers;
    private volatile boolean doShutdown = false;

   private final Object waitMonitor = new Object();

    private Runnable workerMethod = () -> {
        while (!doShutdown) {
            try {
                Runnable r = tasks.take();
                r.run();
                synchronized (waitMonitor) {
                    waitMonitor.notify();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    public ParallelService(int threads) {
        String uuid = UUID.randomUUID().toString();
        this.tasks = new LinkedBlockingQueue<>();

        this.workers = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            Thread t = this.workers[i] = new Thread(workerMethod);
            t.setName("ParallelService " + uuid + "." + i + "/" + threads);
            t.start();
        }
    }

    public void submit(Runnable runnable) {
        tasks.offer(runnable);
    }

    public void waitForCompletion() throws InterruptedException {
        synchronized (waitMonitor) {
            while (tasks.size() > 0) {
                waitMonitor.wait();
            }
        }
    }

    public boolean isShutdown() {
        return doShutdown;
    }

    public void shutdown() {
        doShutdown = true;
    }
}
