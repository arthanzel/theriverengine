package com.arthanzel.theriverengine.util;

import com.arthanzel.theriverengine.rivergen.RiverArc;
import com.arthanzel.theriverengine.rivergen.RiverNetwork;
import com.arthanzel.theriverengine.rivergen.RiverNode;
import org.jgrapht.DirectedGraph;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by martin on 2016-10-26.
 */
public class GraphsTest {
    @Test
    public void testUpstreamDownstreamEdges() throws IOException {
        DirectedGraph<RiverNode, RiverArc> network = RiverNetwork.fromResource("/graphs/linear-5.ini");

        for (RiverArc arc : network.edgeSet()) {
            // Upstreams
            Set<RiverArc> upstreams = Graphs.upstreamEdges(network, arc);
            assertEquals(network.edgesOf(network.getEdgeSource(arc)).size() - 1, upstreams.size());
            assertTrue(network.edgesOf(network.getEdgeSource(arc)).containsAll(upstreams));

            // Downstreams
            Set<RiverArc> downstreams = Graphs.upstreamEdges(network, arc);
            assertEquals(network.edgesOf(network.getEdgeSource(arc)).size() - 1, downstreams.size());
            assertTrue(network.edgesOf(network.getEdgeSource(arc)).containsAll(downstreams));
        }
    }
}