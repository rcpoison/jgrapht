package org.jgrapht.graph;

import org.jgrapht.*;
import org.jgrapht.util.*;

import java.util.*;

public class UndirectedGraphUnion<V, E>
        extends GraphUnion<V, E, UndirectedGraph<V, E>>
        implements UndirectedGraph<V, E> {

    private static final long serialVersionUID = -740199233080172450L;

    UndirectedGraphUnion(UndirectedGraph<V, E> g1, UndirectedGraphUnion<V, E> g2, WeightCombiner operator) {
        super(g1, g2, operator);
    }
    
    UndirectedGraphUnion(UndirectedGraph<V, E> g1, UndirectedGraphUnion<V, E> g2) {
        super(g1, g2);
    }

    public int degreeOf(V vertex) {
        Set<E> res = edgesOf(vertex);
        return res.size();
    }
}
