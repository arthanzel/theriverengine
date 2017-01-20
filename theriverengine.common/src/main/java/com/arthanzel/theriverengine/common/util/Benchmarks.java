package com.arthanzel.theriverengine.common.util;

/**
 * Created by martin on 2017-01-20.
 */
public class Benchmarks {
    public static void run(Runnable r) {
        Benchmarks.run("[anonymous]", r);
    }

    public static void run(String name, Runnable r) {
        long start = System.nanoTime();
        r.run();
        long elapsed = System.nanoTime() - start;
        double elapsedMs = elapsed / 1.0e6;
        System.out.println(String.format("Benchmark %s completed in %.2f ms", name, elapsedMs));
    }
}
