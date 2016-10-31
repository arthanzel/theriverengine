package com.arthanzel.theriverengine.sim.environment;

import com.arthanzel.theriverengine.rivergen.RiverArc;
import com.arthanzel.theriverengine.rivergen.RiverNode;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by martin on 2016-10-31.
 */
public class EnvironmentTest {
    @Test
    public void fromCoordinateFunction() throws Exception {
        RiverArc arc = new RiverArc(new RiverNode("A", 0, 0), new RiverNode("B", 1, 1));
        Environment env = Environment.fromCoordinateFunction((x, y) -> x + 2 * y);
        assertEquals(0, env.get(arc, 0), 0.00001);
        assertEquals(1 + 2 * 1, env.get(arc, 1 * arc.length()), 0.00001);
        assertEquals(0.5 + 2 * 0.5, env.get(arc, 0.5 * arc.length()), 0.00001);
    }
}