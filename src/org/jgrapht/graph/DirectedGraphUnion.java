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
        if (r == null) {
            return 0;
        }
        return r.size();
    }

    public Set<E> incomingEdgesOf(V vertex) {
        HashSet<E> r = new HashSet<E>();
        r.addAll(getG1().incomingEdgesOf(vertex));
        r.addAll(getG2().incomingEdgesOf(vertex));
        return r;
    }

    public int outDegreeOf(V vertex) {
        Set<E> r = outgoingEdgesOf(vertex);
        if (r == null) {
            return 0;
        }
        return r.size();
    }

    public Set<E> outgoingEdgesOf(V vertex) {
        HashSet<E> r = new HashSet<E>();
        r.addAll(getG1().outgoingEdgesOf(vertex));
        r.addAll(getG2().outgoingEdgesOf(vertex));
        return r;
    }
}
