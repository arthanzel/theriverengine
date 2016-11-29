package com.arthanzel.theriverengine.util;

import com.arthanzel.theriverengine.rivergen.RiverNetwork;
import com.arthanzel.theriverengine.rivergen.RiverNode;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Provides a variety of static helper functions to deal with (directed) graphs.
 *
 * @author Martin
 */
public class Graphs {
    public static Rectangle2D dimensions(RiverNetwork network) {
        if (network.edgeSet().isEmpty()) {
            throw new IllegalArgumentException("Network is empty");
        }

        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;

        for (RiverNode node : network.vertexSet()) {
            Point2D pos = node.getPosition();
            if (pos.getX() < minX) minX = pos.getX();
            if (pos.getX() > maxX) maxX = pos.getX();
            if (pos.getY() < minY) minY = pos.getY();
            if (pos.getY() > maxY) maxY = pos.getY();
        }

        return new Rectangle2D(minX, minY, maxX - minX, maxY - minY);
    }

    /**
     * Returns a list of outgoing edges immediately downstream to a given edge.
     *
     * @param graph A directed graph.
     * @param edge  An edge.
     * @param <E>   The graph edge type.
     * @return Set of downstream edges.
     */
    public static <T, E> Set<E> downstreamEdges(DirectedGraph<T, E> graph, E edge) {
        Set<E> downstreams = graph.edgesOf(graph.getEdgeTarget(edge));
        HashSet<E> set = new HashSet<>(downstreams); // Copy because downstreams is an immutable view
        set.remove(edge);
        return set;
    }

    /**
     * Determines a root vertex of a graph. If the graph is null, empty, or if no root vertex exists, returns null. A
     * root vertex is one that has no incoming edges. If a graph has more than one root vertex, one of them is returned,
     * with no particular preference.
     *
     * @param graph A directed graph.
     * @param <V>   The graph vertex type.
     * @return A root vertex.
     */
    public static <V> V getRootVertex(DirectedGraph<V, ?> graph) {
        if (graph == null || graph.vertexSet().isEmpty()) {
            return null;
        }

        for (V vertex : graph.vertexSet()) {
            if (graph.incomingEdgesOf(vertex).isEmpty()) {
                return vertex;
            }
        }

        return null;
    }

    /**
     * Randomly selects and returns an edge from a graph. Returns null if the graph is null or has no edges.
     *
     * @param graph A graph.
     * @param <E>   The graph edge type.
     * @return A random edge.
     */
    public static <E> E randomEdge(Graph<?, E> graph) {
        if (graph == null || graph.edgeSet().isEmpty()) {
            return null;
        }

        return FishMath.sample(graph.edgeSet());
    }

    /**
     * Randomly selects and returns a vertex from a graph. Returns null if the graph is null or has no vertices.
     *
     * @param graph A graph.
     * @param <V>   The graph vertex type.
     * @return A random vertex.
     */
    public static <V> V randomVertex(Graph<V, ?> graph) {
        if (graph == null || graph.vertexSet().isEmpty()) {
            return null;
        }

        return FishMath.sample(graph.vertexSet());
    }

    /**
     * Returns a list of incoming edges immediately upstream to a given edge.
     *
     * @param graph A directed graph.
     * @param edge  An edge.
     * @param <E>   The graph edge type.
     * @return Set of upstream edges.
     */
    public static <T, E> Set<E> upstreamEdges(DirectedGraph<T, E> graph, E edge) {
        Set<E> upstreams = graph.edgesOf(graph.getEdgeSource(edge));
        HashSet<E> set = new HashSet<>(upstreams);
        set.remove(edge);
        return set;
    }
}
