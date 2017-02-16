package com.arthanzel.theriverengine.reporting;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

/**
 * RiverReporter listens for messages broadcast by a class and forwards
 * them to consumers. Consumers receive events in the order that they were
 * added to this RiverReporter. Consumers receive events in series, so a
 * consumer blocks the chain until it is finished processing its message.
 */
public class RiverReporter {
    private boolean running = false;
    private final List<Consumer<String>> consumers = Collections.synchronizedList(new LinkedList<>());
    private Runnable spinner;

    /**
     * Construct a new RiverReporter that listens to a RiverRunner.
     */
    public RiverReporter(BlockingQueue<String> queue) {
        spinner = () -> {
            while (running) {
                try {
                    String message = queue.take(); // Blocking

                    synchronized (this.consumers) {
                        for (Consumer<String> consumer : this.consumers) {
                            consumer.accept(message);
                        }
                    }
                } catch (InterruptedException e) {
                    // Do nothing - thread exits by setting running to false.
                }
            }
        };
    }

    /**
     * Returns true if this RiverReporter is listening to messages.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Starts listening to messages on a separate thread. Throws an
     * IllegalStateException if already started.
     */
    public void start() {
        if (running) {
            throw new IllegalStateException("Reporter is already running!");
        }
        running = true;
        new Thread(spinner).start();
    }

    /**
     * Flags the RiverReporter to stop listening. Any message that is currently
     * being processed will finish being processed by all consumers. Pending
     * messages will not be processed. This method returns immediately, and
     * does not guarantee that the listener thread will stop at a specific time.
     */
    public void stop() {
        running = false;
    }

    /**
     * Gets a thread-safe list of consumers.
     */
    public List<Consumer<String>> getConsumers() {
        return consumers;
    }
}
