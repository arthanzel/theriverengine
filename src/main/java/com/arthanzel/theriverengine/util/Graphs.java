package com.arthanzel.theriverengine.util;

import org.jgrapht.DirectedGraph;
import java.util.Set;

/**
 * TODO: Documentation
 *
 * @author Martin
 */
public class Graphs {
    public static <V, E> Set<E> downstreamEdges(DirectedGraph<V, E> graph, E edge) {
        return graph.outgoingEdgesOf(graph.getEdgeTarget(edge));
    }

    public static <V, E> Set<E> upstreamEdges(DirectedGraph<V, E> graph, E edge) {
        return graph.incomingEdgesOf(graph.getEdgeSource(edge));
    }
}
