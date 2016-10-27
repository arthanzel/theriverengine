package com.arthanzel.theriverengine.util;

import com.arthanzel.theriverengine.rivergen.RiverArc;
import com.arthanzel.theriverengine.rivergen.RiverNetwork;
import com.arthanzel.theriverengine.rivergen.RiverNode;
import com.arthanzel.theriverengine.testutil.TestNetworks;
import javafx.geometry.Point2D;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by martin on 2016-10-26.
 */
public class GraphsTest {
    @Test
    public void getRootVertex() throws Exception {
        RiverNetwork network = TestNetworks.get("binarytree-3");

        RiverNode root = Graphs.getRootVertex(network);
        assertTrue(new Point2D(350, 0).equals(root.getPosition()));
    }

    @Test
    public void randomEdge() throws Exception {
        RiverNetwork network = TestNetworks.get("linear-5");
        Set<RiverArc> set = new HashSet<>(network.edgeSet());
        for (int i = 0; i < 100; i++) {
            set.remove(Graphs.randomEdge(network));

            if (set.size() == 0) {
                return; // Pass
            }
        }
        fail("Not all edges were found using randomEdge().");
    }

    @Test
    public void randomVertex() throws Exception {
        RiverNetwork network = TestNetworks.get("linear-5");
        Set<RiverNode> set = new HashSet<>(network.vertexSet());
        for (int i = 0; i < 100; i++) {
            set.remove(Graphs.randomVertex(network));

            if (set.size() == 0) {
                return; // Pass
            }
        }
        fail("Not all nodes were found using randomEdge().");
    }

    @Test
    public void upstreamDownstreamEdges() throws IOException {
        RiverNetwork network = TestNetworks.get("binarytree-3");

        for (RiverArc arc : network.edgeSet()) {
            // Upstreams
            Set<RiverArc> upstreams = Graphs.upstreamEdges(network, arc);
            assertEquals(network.edgesOf(network.getEdgeSource(arc)).size() - 1, upstreams.size());
            assertTrue(network.edgesOf(network.getEdgeSource(arc)).containsAll(upstreams));

            // Downstreams
            Set<RiverArc> downstreams = Graphs.downstreamEdges(network, arc);
            assertEquals(network.edgesOf(network.getEdgeTarget(arc)).size() - 1, downstreams.size());
            assertTrue(network.edgesOf(network.getEdgeTarget(arc)).containsAll(downstreams));
        }
    }
}