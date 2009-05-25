package org.jgrapht.graph;

import java.util.*;

import org.jgrapht.*;
import org.jgrapht.util.*;


public class DirectedGraphUnion<V, E>
    extends GraphUnion<V, E, DirectedGraph<V, E>>
    implements DirectedGraph<V, E>
{
    //~ Static fields/initializers ---------------------------------------------

    private static final long serialVersionUID = -740199233080172450L;

    //~ Constructors -----------------------------------------------------------

    DirectedGraphUnion(
        DirectedGraph<V, E> g1,
        DirectedGraph<V, E> g2,
        WeightCombiner operator)
    {
        super(g1, g2, operator);
    }

    DirectedGraphUnion(DirectedGraph<V, E> g1, DirectedGraph<V, E> g2)
    {
        super(g1, g2);
    }

    //~ Methods ----------------------------------------------------------------

    public int inDegreeOf(V vertex)
    {
        Set<E> res = incomingEdgesOf(vertex);
        return res.size();
    }

    public Set<E> incomingEdgesOf(V vertex)
    {
        Set<E> res = new HashSet<E>();
        if (getG1().containsVertex(vertex)) {
            res.addAll(getG1().incomingEdgesOf(vertex));
        }
        if (getG2().containsVertex(vertex)) {
            res.addAll(getG2().incomingEdgesOf(vertex));
        }
        return Collections.unmodifiableSet(res);
    }

    public int outDegreeOf(V vertex)
    {
        Set<E> res = outgoingEdgesOf(vertex);
        return res.size();
    }

    public Set<E> outgoingEdgesOf(V vertex)
    {
        Set<E> res = new HashSet<E>();
        if (getG1().containsVertex(vertex)) {
            res.addAll(getG1().outgoingEdgesOf(vertex));
        }
        if (getG2().containsVertex(vertex)) {
            res.addAll(getG2().outgoingEdgesOf(vertex));
        }
        return Collections.unmodifiableSet(res);
    }
}

// End $file.name$
