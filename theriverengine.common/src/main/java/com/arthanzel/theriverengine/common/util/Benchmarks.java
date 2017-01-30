package com.arthanzel.theriverengine.common.util;

/**
 * Provides methods for benchmarking code.
 */
public class Benchmarks {
    /**
     * Benchmarks a task and prints the elapsed time.
     * @param r Task to run.
     */
    public static void print(Runnable r) {
        Benchmarks.print("[anonymous]", r);
    }

    /**
     * Benchmarks a task and prints the elapsed time.
     * @param name Human-readable task name.
     * @param r Task to run.
     */
    public static void print(String name, Runnable r) {
        double elapsedMs = run(r);
        System.out.println(String.format("Benchmark %s completed in %.2f ms", name, elapsedMs));
    }

    /**
     * Benchmarks a task.
     * @param r Task to run.
     * @return Elapsed time, in milliseconds.
     */
    public static double run(Runnable r) {
        long start = System.nanoTime();
        r.run();
        long elapsed = System.nanoTime() - start;
        return elapsed / 1.0e6;
    }
}
