package com.arthanzel.theriverengine.concurrent;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * TODO: Documentation
 *
 * @author Martin
 */
public class ParallelServiceTest {
    private class Proxy<V> {
        private V val;
        Proxy(V val) { this.val = val; }
        V get() { return val; }
        void set(V val) { this.val = val; }
    }

    @Test
    public void test() throws Exception {
        final ParallelService ps = new ParallelService(2);

        final Proxy<Long> sum1 = new Proxy<>(0L);
        ps.submit(() -> {
            long sum = 0;
            for (int i = 0; i < 100000; i++) {
                sum += i;
            }
            sum1.set(sum);
            System.out.println(sum);
        });

        final Proxy<Long> sum2 = new Proxy<>(0L);
        ps.submit(() -> {
            long sum = 0;
            for (int i = 0; i > -100000; i--) {
                sum += i;
            }
            sum2.set(sum);
            System.out.println(sum);
        });

        assertEquals(0, (long) sum1.get());
        assertEquals(0, (long) sum2.get());
        ps.waitForCompletion();
        assertEquals(4999950000L, (long) sum1.get());
        assertEquals(-4999950000L, (long) sum2.get());
    }
}