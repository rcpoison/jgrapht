/**
 *
 */
package org.jgrapht.experimental.alg;

import java.util.*;

import org.jgrapht.*;


/**
 * @author micha
 */
public abstract class IntArrayGraphAlgorithm<V, E>
{
    //~ Instance fields --------------------------------------------------------

    protected final List<V> _vertices;
    protected final int [][] _neighbors;
    protected final Map<V, Integer> _vertexToPos;

    //~ Constructors -----------------------------------------------------------

    /**
     * @param g
     */
    public IntArrayGraphAlgorithm(final Graph<V, E> g)
    {
        final int numVertices = g.vertexSet().size();
        _vertices = new ArrayList<V>(numVertices);
        _neighbors = new int[numVertices][];
        _vertexToPos = new HashMap<V, Integer>(numVertices);
        int index = 0;
        for (V vertex : g.vertexSet()) {
            _vertices.add(vertex);
            _neighbors[index++] = new int[g.edgesOf(vertex).size()];
            _vertexToPos.put(vertex, index);
        }
        for (int i = 0; i < numVertices; i++) {
            int nbIndex = 0;
            final V vertex = _vertices.get(i);
            for (E e : g.edgesOf(vertex)) {
                _neighbors[i][nbIndex++] =
                    _vertexToPos.get(Graphs.getOppositeVertex(g, e, vertex));
            }
        }
    }
}

// End IntArrayGraphAlgorithm.java
