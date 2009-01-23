package org.jgrapht.graph;

import org.jgrapht.*;

import java.util.*;

public class DirectedGraphUnion<V, E>
        extends GraphUnion<V, E, DirectedGraph<V, E>>
        implements DirectedGraph<V, E> {

    DirectedGraphUnion(DirectedGraph<V, E> g1, DirectedGraph<V, E> g2) {
        super(g1, g2);
    }

    public int inDegreeOf(V vertex) {
        Set<E> r = incomingEdgesOf(vertex);
        return r.size();
    }

    public Set<E> incomingEdgesOf(V vertex) {
        Set<E> r = new HashSet<E>();
        if (getG1().containsVertex(vertex)) {
            r.addAll(getG1().incomingEdgesOf(vertex));
        }
        if (getG2().containsVertex(vertex)) {
            r.addAll(getG2().incomingEdgesOf(vertex));
        }
        return Collections.unmodifiableSet(r);
    }

    public int outDegreeOf(V vertex) {
        Set<E> r = outgoingEdgesOf(vertex);
        return r.size();
    }

    public Set<E> outgoingEdgesOf(V vertex) {
        Set<E> r = new HashSet<E>();
        if (getG1().containsVertex(vertex)) {
            r.addAll(getG1().outgoingEdgesOf(vertex));
        }
        if (getG2().containsVertex(vertex)) {
            r.addAll(getG2().outgoingEdgesOf(vertex));
        }
        return Collections.unmodifiableSet(r);
    }
}
