package com.arthanzel.theriverengine.rivergen;

import com.arthanzel.theriverengine.util.GraphFiles;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.SimpleDirectedGraph;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a river network.
 *
 * @author Martin
 */
public class RiverNetwork {
    public static DirectedGraph<RiverNode, RiverArc> fromResource(String resource) throws IOException {
        DirectedGraph<RiverNode, RiverArc> graph = new SimpleDirectedGraph<>(RiverArc.class);

        // Locate the file and parse it into a resource
        Ini ini;
        try {
            ini = new Ini(RiverNetwork.class.getResource(resource));
        } catch(InvalidFileFormatException e) {
            throw e;
        } catch(IOException | NullPointerException e) {
            throw new IOException("Can't find file " + resource);
        }

        // Collect nodes
        Map<String, RiverNode> nodes = new HashMap<>();
        for (String k : ini.get("vertices").keySet()) {
            String v = ini.get("vertices").get(k);
            double[] doubles = GraphFiles.toDoubles(v);
            RiverNode node = new RiverNode(k, doubles[0], doubles[1]);
            nodes.put(k, node);
            graph.addVertex(node);
        }

        // Collect edges
        for (String k : ini.get("edges").keySet()) {
            String v = ini.get("edges").get(k);
            String[] destinations = GraphFiles.toStrings(v);
            RiverNode origin = nodes.get(k);
            for (String dest : destinations) {
                graph.addEdge(origin, nodes.get(dest), new RiverArc(origin, nodes.get(dest)));
            }
        }

        return graph;
    }
}
