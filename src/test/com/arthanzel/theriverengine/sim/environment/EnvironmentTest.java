package com.arthanzel.theriverengine.sim.environment;

import com.arthanzel.theriverengine.rivergen.RiverArc;
import com.arthanzel.theriverengine.rivergen.RiverNetwork;
import com.arthanzel.theriverengine.rivergen.RiverNode;
import com.arthanzel.theriverengine.testutil.TestNetworks;
import com.arthanzel.theriverengine.util.Graphs;
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
        assertEquals(3, env.get(arc, 1 * arc.length()), 0.00001);
        assertEquals(0.5 + 2 * 0.5, env.get(arc, 0.5 * arc.length()), 0.00001);
    }

    @Test
    public void testClone() throws Exception {
        // Assert that immutable Environments are cloned by reference
        Environment refCloneEnvironment = Environment.fromCoordinateFunction((x, y) -> x + y);
        assertEquals(refCloneEnvironment, Environment.clone(refCloneEnvironment));

        // Assert that mutable environments are deep-cloned
//        RiverNetwork network = TestNetworks.get("binarytree-3");
//        RiverArc arc = Graphs.randomEdge(network);
//        DiscreteEnvironment de = new DiscreteEnvironment(network);
//        DiscreteEnvironment clone = (DiscreteEnvironment) Environment.clone(de);
//        assertEquals(de.get(arc, 0.1), clone.get(arc, 0.1), 0.0001);
//        assertArrayEquals(de.getValues().get(arc), clone.getValues().get(arc), 0.0001);
//        de.getValues().clear();
//        assertTrue(clone.getValues().containsKey(arc));
    }
}