package com.arthanzel.theriverengine.util;

import org.junit.Test;

/**
 * Created by martin on 2016-10-26.
 */
public class MultitypeHashMapTest {
    @Test
    public void test() {
        MultitypeHashMap map = new MultitypeHashMap();
        map.put("int", 3);
        map.put("double", Math.PI);
        map.put("long", Long.MAX_VALUE);
        map.put("string", "123");


    }
}