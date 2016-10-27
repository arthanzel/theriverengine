package com.arthanzel.theriverengine.util;

import com.arthanzel.theriverengine.rivergen.RiverNode;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;

import java.util.Random;
import java.util.Set;

/**
 * Provides a variety of static helper functions to deal with (directed) graphs.
 *
 * @author Martin
 */
public class Graphs {
    private Graphs() {
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
        T node = graph.getEdgeSource(edge);
        Set<E> downstreams = graph.edgesOf(node);
        downstreams.remove(edge);
        return downstreams;
    }

    /**
     * Determines a root vertex of a graph. If the graph is null, empty, or if no root vertex exists, returns null. A
     * root vertex is one that has no incoming edges. If a graph has more than one root vertex, one of them is returned,
     * with no particular preference.
     *
     * @param graph A directed graph.
     * @param <V>   The graph vertex type.
     * @param <E>   The graph edge type.
     * @return A root vertex.
     */
    public static <V, E> V getRootVertex(DirectedGraph<V, E> graph) {
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
    @SuppressWarnings("unchecked")
    public static <E> E randomEdge(Graph<?, E> graph) {
        if (graph == null || graph.vertexSet().isEmpty()) {
            return null;
        }

        Set<E> vertices = graph.edgeSet();
        int i = new Random().nextInt(vertices.size());
        return (E) vertices.toArray()[i];
    }

    /**
     * Randomly selects and returns a vertex from a graph. Returns null if the graph is null or has no vertices.
     *
     * @param graph A graph.
     * @param <V>   The graph vertex type.
     * @return A random vertex.
     */
    @SuppressWarnings("unchecked")
    public static <V> V randomVertex(Graph<V, ?> graph) {
        if (graph == null || graph.vertexSet().isEmpty()) {
            return null;
        }

        Set<V> vertices = graph.vertexSet();
        int i = new Random().nextInt(vertices.size());
        return (V) vertices.toArray()[i];
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
        T node = graph.getEdgeSource(edge);
        Set<E> upstreams = graph.edgesOf(node);
        upstreams.remove(edge);
        return upstreams;
    }
}
