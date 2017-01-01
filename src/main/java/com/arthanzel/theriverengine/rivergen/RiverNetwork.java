package com.arthanzel.theriverengine.rivergen;

import com.arthanzel.theriverengine.util.GraphFiles;
import com.arthanzel.theriverengine.util.Graphs;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.jgrapht.graph.SimpleDirectedGraph;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents a river network.
 *
 * TODO: Move Graphs logic here.
 *
 * @author Martin
 */
public class RiverNetwork extends SimpleDirectedGraph<RiverNode, RiverArc> {
    public RiverNetwork() {
        super(RiverArc.class);
    }

    public static RiverNetwork fromResource(String resource) throws IOException {
        RiverNetwork network = new RiverNetwork();

        // Locate the file and parse it into a resource
        Ini ini;
        try {
            ini = new Ini(RiverNetwork.class.getResource(resource));
        } catch (InvalidFileFormatException e) {
            throw e;
        } catch (IOException | NullPointerException e) {
            throw new IOException("Can't find file " + resource);
        }

        // Get the scale
        Double scaleX = ini.get("graph", "scalex", Double.class);
        if (scaleX == null) {
            scaleX = 1.0;
        }
        Double scaleY = ini.get("graph", "scaley", Double.class);
        if (scaleY == null) {
            scaleY = 1.0;
        }

        // Collect nodes
        Map<String, RiverNode> nodes = new HashMap<>();
        for (String k : ini.get("vertices").keySet()) {
            String v = ini.get("vertices").get(k);
            double[] doubles = GraphFiles.toDoubles(v);
            RiverNode node = new RiverNode(k, doubles[0] * scaleX, doubles[1] * scaleY);
            nodes.put(k, node);
            network.addVertex(node);
        }

        // Collect edges
        for (String k : ini.get("edges").keySet()) {
            String v = ini.get("edges").get(k);
            String[] destinations = GraphFiles.toStrings(v);
            RiverNode origin = nodes.get(k);
            for (String dest : destinations) {
                network.addEdge(origin, nodes.get(dest), new RiverArc(origin, nodes.get(dest)));
            }
        }

        network.optimize();
        return network;
    }

    /**
     * Caches several elements within vertices and edges of this RiverNetwork for easier and faster access.
     */
    public void optimize() {
        for (RiverArc arc : this.edgeSet()) {
            arc.getUpstreamArcs().clear();
            arc.getUpstreamArcs().addAll(Graphs.upstreamEdgesOf(this, arc));
            arc.getDownstreamArcs().addAll(Graphs.downstreamEdgesOf(this, arc));
        }

        for (RiverNode node : this.vertexSet()) {
            node.getDownstreamArcs().addAll(this.outgoingEdgesOf(node));
            node.getUpstreamArcs().addAll(this.incomingEdgesOf(node));
        }
    }
}
