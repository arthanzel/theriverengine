package com.arthanzel.theriverengine.common.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class BenchmarksTest {
    @Test
    public void run() throws Exception {
        double elapsed = Benchmarks.run(() -> {
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException e) {
                fail();
            }
        });
        System.out.println(elapsed);
        assertTrue("Elapsed time was " + elapsed,elapsed > 90);
    }
}