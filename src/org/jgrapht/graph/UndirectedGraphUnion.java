package org.jgrapht.graph;

import java.util.*;

import org.jgrapht.*;
import org.jgrapht.util.*;


public class UndirectedGraphUnion<V, E>
    extends GraphUnion<V, E, UndirectedGraph<V, E>>
    implements UndirectedGraph<V, E>
{
    //~ Static fields/initializers ---------------------------------------------

    private static final long serialVersionUID = -740199233080172450L;

    //~ Constructors -----------------------------------------------------------

    UndirectedGraphUnion(
        UndirectedGraph<V, E> g1,
        UndirectedGraphUnion<V, E> g2,
        WeightCombiner operator)
    {
        super(g1, g2, operator);
    }

    UndirectedGraphUnion(
        UndirectedGraph<V, E> g1,
        UndirectedGraphUnion<V, E> g2)
    {
        super(g1, g2);
    }

    //~ Methods ----------------------------------------------------------------

    public int degreeOf(V vertex)
    {
        Set<E> res = edgesOf(vertex);
        return res.size();
    }
}

// End $file.name$
