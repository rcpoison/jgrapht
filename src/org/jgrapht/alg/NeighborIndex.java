/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Lead:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2005, by Barak Naveh and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */
/* --------------------------
 * NeighborIndex.java
 * --------------------------
 * (C) Copyright 2005, by Charles Fry and Contributors.
 *
 * Original Author:  Charles Fry
 *
 * $Id$
 *
 * Changes
 * -------
 * 13-Dec-2005 : Initial revision (CF);
 *
 */
package org.jgrapht.alg;

import java.util.*;

import org.jgrapht.*;
import org.jgrapht.event.*;
import org.jgrapht.graph.*;
import org.jgrapht.util.*;

/**
 * Maintains a cache of each vertex's neighbors. While
 * lists of neighbors can be obtained from {@link Graphs}, they are
 * re-calculated at each invocation by walking a vertex's incident edges, Which
 * becomes inordinately expensive when performed often.
 *
 * <p>Edge direction is ignored when evaluating neighbors; to take edge
 * direction into account when indexing neighbors, use {@link
 * DirectedNeighborIndex}.
 *
 * <p>A vertex's neighbors are cached the first time they are asked for (i.e.
 * the index is built on demand). The index will only be updated automatically
 * if it is added to the associated grpah as a listener. If it is added as a
 * listener to a graph other than the one it indexes, results are
 * undefined.</p>
 *
 * @author Charles Fry
 * @since Dec 13, 2005
 */
public class NeighborIndex<V, E> implements GraphListener<V, E>
{

    //~ Instance fields -------------------------------------------------------

    Map<V, Neighbors> m_neighborMap = new HashMap<V, Neighbors>();
    private Graph<V, E> m_graph;

    //~ Constructors ----------------------------------------------------------

    /**
     * Creates a neighbor index for the specified undirected graph.
     *
     * @param g the graph for which a neighbor index is to be created.
     */
    public NeighborIndex(Graph<V, E> g)
    {
        // no need to distinguish directedgraphs as we don't do traversals
        m_graph = g;
    }

    //~ Methods ---------------------------------------------------------------

    /**
     * Returns the set of vertices which are adjacent to a specified vertex.
     * The returned set is backed
     * by the index, and will be updated when the graph changes as long as
     * the index has been added as a listener to the graph.
     *
     * @param v the vertex whose neighbors are desired
     * @return all unique neighbors of the specified vertex
     */
    public Set<V> neighborsOf(V v)
    {
        return getNeighbors(v).getNeighbors();
    }

    /**
     * Returns a list of vertices which are adjacent to a specified vertex.
     * If the graph is a multigraph, vertices may appear more than once in
     * the returned list. Because a list of neighbors
     * can not be efficiently maintained, it is reconstructed on every
     * invocation, by duplicating entries in the neighbor set.
     * It is thus more effecient to use {@link neighborsOf(V)}
     * unless dupliate neighbors are important.
     *
     * @param v the vertex whose neighbors are desired
     * @return all neighbors of the specified vertex
     */
    public List<V> neighborListOf(V v)
    {
        return getNeighbors(v).getNeighborList();
    }

    /**
     * @see GraphListener#edgeAdded(GraphEdgeChangeEvent)
     */
    public void edgeAdded(GraphEdgeChangeEvent<V, E> e)
    {
        E edge = e.getEdge();
        V source = m_graph.getEdgeSource(edge);
        V target = m_graph.getEdgeTarget(edge);
        getNeighbors(source).addNeighbor(target);
        getNeighbors(target).addNeighbor(source);
    }

    /**
     * @see GraphListener#edgeRemoved(GraphEdgeChangeEvent)
     */
    public void edgeRemoved(GraphEdgeChangeEvent<V, E> e)
    {
        E edge = e.getEdge();
        V source = m_graph.getEdgeSource(edge);
        V target = m_graph.getEdgeTarget(edge);
        if (m_neighborMap.containsKey(source)) {
            m_neighborMap.get(source).removeNeighbor(target);
        }
        if (m_neighborMap.containsKey(target)) {
            m_neighborMap.get(target).removeNeighbor(source);
        }
    }

    /**
     * @see org.jgrapht.event.VertexSetListener#vertexAdded(GraphVertexChangeEvent)
     */
    public void vertexAdded(GraphVertexChangeEvent<V> e)
    {
        // nothing to cache until there are edges
    }

    /**
     * @see org.jgrapht.event.VertexSetListener#vertexRemoved(GraphVertexChangeEvent)
     */
    public void vertexRemoved(GraphVertexChangeEvent<V> e)
    {
        m_neighborMap.remove(e.getVertex());
    }

    private Neighbors getNeighbors(V v)
    {
        Neighbors neighbors = m_neighborMap.get(v);
        if (neighbors == null) {
            neighbors = new Neighbors<V, E>(v,
                    Graphs.neighborListOf(m_graph, v));
            m_neighborMap.put(v, neighbors);
        }
        return neighbors;
    }

    //~ Inner Classes ---------------------------------------------------------

    /**
     * Stores cached neighbors  for a single vertex. Includes support for
     * live neighbor sets and duplicate neighbors.
     */
    static class Neighbors<V, E>
    {
        private Map<V,ModifiableInteger> m_neighborCounts = new LinkedHashMap<V,ModifiableInteger>();
        // TODO could eventually make neighborSet modifiable, resulting
        //      in edge removals from the graph
        private Set<V> m_neighborSet = Collections.unmodifiableSet(
                m_neighborCounts.keySet());

        public Neighbors(V v, Collection<V> neighbors)
        {
            // add all current neighbors
            for (V neighbor : neighbors) {
                addNeighbor(neighbor);
            }
        }

        public void addNeighbor(V v)
        {
            ModifiableInteger count = m_neighborCounts.get(v);
            if (count == null) {
                count = new ModifiableInteger(1);
                m_neighborCounts.put(v, count);
            }
            else {
                count.increment();
            }
        }

        public void removeNeighbor(V v)
        {
            ModifiableInteger count = m_neighborCounts.get(v);
            if (count == null) {
                throw new IllegalArgumentException("Attempting to remove a neighbor that wasn't present");
            }

            count.decrement();
            if (count.getValue() == 0) {
                m_neighborCounts.remove(v);
            }
        }

        public Set<V> getNeighbors()
        {
            return m_neighborSet;
        }

        public List<V> getNeighborList()
        {
            List neighbors = new ArrayList();
            for (Map.Entry<V,ModifiableInteger> entry : m_neighborCounts.entrySet()) {
                V v = entry.getKey();
                int count = entry.getValue().intValue();
                for (int i = 0; i < count; i++) {
                    neighbors.add(v);
                }
            }
            return neighbors;
        }
    }

}
