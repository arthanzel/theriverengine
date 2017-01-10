package com.arthanzel.theriverengine.testutil;

import com.arthanzel.theriverengine.rivergen.RiverNetwork;

import java.io.IOException;

/**
 * Created by martin on 2016-10-26.
 */
public class TestNetworks {
    public static RiverNetwork get(String name) throws IOException {
        return RiverNetwork.fromResource("/graphs/test/" + name + ".ini");
    }
}
