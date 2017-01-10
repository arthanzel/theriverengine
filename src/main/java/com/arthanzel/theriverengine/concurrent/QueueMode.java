package com.arthanzel.theriverengine.concurrent;

/**
 * Describes strategies for adding elements to a blocking queue if the queue is full.
 */
public enum QueueMode {
    /**
     * Blocks until there is room available in the queue, if the queue is full.
     */
    BLOCK,

    /**
     * Drops the oldest element in the queue if the queue is full.
     */
    DROP,

    /**
     * Skips adding the element into the queue if the queue is full.
     */
    SKIP
}
