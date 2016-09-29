package com.arthanzel.theriverengine;

import com.arthanzel.theriverengine.rivergen.RiverArc;
import com.arthanzel.theriverengine.rivergen.RiverNetwork;
import com.arthanzel.theriverengine.rivergen.RiverNode;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.alg.DijkstraShortestPath;

import java.io.IOException;
import java.util.Random;
import java.util.Set;

/**
 * Entry point for The River Engine.
 *
 * @author Martin
 */
public class Main {
    public static void main(String[] args) {
        try {
            DirectedGraph<RiverNode, RiverArc> g = RiverNetwork.fromResource("/graphs/binarytree-3.ini");
            System.out.println(DijkstraShortestPath.findPathBetween(g, root(g), sample(g)));
            g = RiverNetwork.fromResource("/graphs/comb-4.ini");
            System.out.println(DijkstraShortestPath.findPathBetween(g, root(g), sample(g)));
            g = RiverNetwork.fromResource("/graphs/linear-5.ini");
            System.out.println(DijkstraShortestPath.findPathBetween(g, root(g), sample(g)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static RiverNode root(DirectedGraph<RiverNode, RiverArc> g) {
        RiverNode node = g.vertexSet().iterator().next();
        while (g.incomingEdgesOf(node).size() > 0) {
            node = g.getEdgeSource(g.incomingEdgesOf(node).iterator().next());
        }
        return node;
    }

    private static RiverNode sample(Graph<RiverNode, ?> g) {
        Set<RiverNode> set = g.vertexSet();
        int i = new Random().nextInt(set.size());
        return (RiverNode) set.toArray()[i];
    }
}
