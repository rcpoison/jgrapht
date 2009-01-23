package org.jgrapht.graph;

import org.jgrapht.*;

import java.util.*;

public class UndirectedGraphUnion<V, E>
        extends GraphUnion<V, E, UndirectedGraph<V, E>>
        implements UndirectedGraph<V, E> {

    UndirectedGraphUnion(UndirectedGraph<V, E> g1, UndirectedGraphUnion<V, E> g2) {
        super(g1, g2);
    }

    public int degreeOf(V vertex) {
        Set<E> r = edgesOf(vertex);
        return r.size();
    }
}
