/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Lead:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2004, by Barak Naveh and Contributors.
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
 * StrongConnectivityInspector.java
 * --------------------------
 * (C) Copyright 2005, by Christian Soltenborn and Contributors.
 *
 * Original Author:  Christian Soltenborn
 *
 * $Id: StrongConnectivityInspector.java,v 1.7 2005/07/19 09:03:29 hammerc Exp
 * $
 *
 * Changes
 * -------
 * 2-Feb-2005 : Initial revision (CS);
 *
 */
package org.jgrapht.alg;

import java.util.*;

import org.jgrapht.*;
import org.jgrapht.graph.*;


/**
 * <p>Complements the {@link org.jgrapht.alg.ConnectivityInspector} class
 * with the capability to compute the strongly connected components of a
 * directed graph. The algorithm is implemented after "Corman et al:
 * Introduction to agorithms", Chapter 25.2. It has a running time of O(V + E).
 * </p>
 *
 * <p>Unlike {@link org.jgrapht.alg.ConnectivityInspector}, this class
 * does not implement incremental inspection. The full algorithm is executed at
 * the first call of {@link
 * StrongConnectivityInspector#stronglyConnectedSets()} or {@link
 * StrongConnectivityInspector#isStronglyConnected()}.</p>
 *
 * @author Christian Soltenborn
 * @author Christian Hammer
 * @since Feb 2, 2005
 */
public class StrongConnectivityInspector<V, E>
{

    //~ Instance fields -------------------------------------------------------

    // the graph to compute the strongly connected sets for
    private final DirectedGraph<V, E> m_graph;

    // stores the vertices, ordered by their finishing time in first dfs
    private LinkedList<VertexData<V>> m_orderedVertices;

    // the result of the computation, cached for future calls
    private List<Set<V>> m_stronglyConnectedSets;

    // the result of the computation, cached for future calls
    private List<DirectedSubgraph<V,E>> m_stronglyConnectedSubgraphs;

    // maps vertices to their VertexData object
    private Map<V, VertexData<V>> m_vertexToVertexData;

    //~ Constructors ----------------------------------------------------------

    /**
     * The constructor of the StrongConnectivityInspector class.
     *
     * @param directedGraph the graph to inspect
     *
     * @throws IllegalArgumentException
     */
    public StrongConnectivityInspector(DirectedGraph<V, E> directedGraph)
    {
        if (directedGraph == null) {
            throw new IllegalArgumentException("null not allowed for graph!");
        }

        m_graph = directedGraph;
        m_vertexToVertexData = null;
        m_orderedVertices = null;
        m_stronglyConnectedSets = null;
        m_stronglyConnectedSubgraphs = null;
    }

    //~ Methods ---------------------------------------------------------------

    /**
     * Returns the graph inspected by the StrongConnectivityInspector.
     *
     * @return the graph inspected by this StrongConnectivityInspector
     */
    public DirectedGraph<V, E> getGraph()
    {
        return m_graph;
    }

    /**
     * Returns true if the graph of this <code>
     * StronglyConnectivityInspector</code> instance is strongly connected.
     *
     * @return true if the graph is strongly connected, false otherwise
     */
    public boolean isStronglyConnected()
    {
        return stronglyConnectedSets().size() == 1;
    }

    /**
     * Computes a {@link List} of {@link Set}s, where each set contains
     * vertices which together form a strongly connected component within the
     * given graph.
     *
     * @return <code>List</code> of <code>Set</code> s containing the strongly
     *         connected components
     */
    public List<Set<V>> stronglyConnectedSets()
    {
        if (m_stronglyConnectedSets == null) {
            m_orderedVertices = new LinkedList<VertexData<V>>();
            m_stronglyConnectedSets = new Vector<Set<V>>();

            // create VertexData objects for all vertices, store them
            createVertexData();

            // perform the first round of DFS, result is an ordering
            // of the vertices by decreasing finishing time
            Iterator<VertexData<V>> iter =
                m_vertexToVertexData.values().iterator();

            while (iter.hasNext()) {
                VertexData<V> data = iter.next();

                if (!data.m_discovered) {
                    dfsVisit(m_graph, data, null);
                }
            }

            // calculate inverse graph (i.e. every edge is reversed)
            DirectedGraph<V, E> inverseGraph =
                new DefaultDirectedGraph<V, E>(m_graph.getEdgeFactory());
            Graphs.addGraphReversed(inverseGraph, m_graph);

            // get ready for next dfs round
            resetVertexData();

            // second dfs round: vertices are considered in decreasing
            // finishing time order; every tree found is a strongly
            // connected set
            iter = m_orderedVertices.iterator();

            while (iter.hasNext()) {
                VertexData<V> data = iter.next();

                if (!data.m_discovered) {
                    // new strongly connected set
                    Set<V> set = new HashSet<V>();
                    m_stronglyConnectedSets.add(set);
                    dfsVisit(inverseGraph, data, set);
                }
            }

            // clean up for garbage collection
            m_orderedVertices = null;
            m_vertexToVertexData = null;
        }

        return m_stronglyConnectedSets;
    }

    /**
     * <p>Computes a list of {@link DirectedSubgraph}s of the given graph. Each
     * subgraph will represent a strongly connected component and will contain
     * all vertices of that component. The subgraph will have an edge (u,v) iff
     * u and v are contained in the strongly connected component.</p>
     *
     * <p>NOTE: Calling this method will first execute {@link
     * StrongConnectivityInspector#stronglyConnectedSets()}. If you don't need
     * subgraphs, use that method.</p>
     *
     * @return a list of subgraphs representing the strongly connected
     *         components
     */
    public List<DirectedSubgraph<V,E>> stronglyConnectedSubgraphs()
    {
        if (m_stronglyConnectedSubgraphs == null) {
            List<Set<V>> sets = stronglyConnectedSets();
            m_stronglyConnectedSubgraphs = new Vector<DirectedSubgraph<V, E>>(sets.size());

            Iterator<Set<V>> iter = sets.iterator();

            while (iter.hasNext()) {
                m_stronglyConnectedSubgraphs.add(new DirectedSubgraph<V, E>(
                        m_graph,
                        iter.next(),
                        null));
            }
        }

        return m_stronglyConnectedSubgraphs;
    }

    /*
     * Creates a VertexData object for every vertex in the graph and stores
     * them
     * in a HashMap.
     */
    private void createVertexData()
    {
        m_vertexToVertexData =
            new HashMap<V, VertexData<V>>(m_graph.vertexSet().size());

        Iterator<V> iter = m_graph.vertexSet().iterator();

        while (iter.hasNext()) {
            V vertex = iter.next();
            m_vertexToVertexData.put(
                vertex,
                new VertexData<V>(null, vertex, false, false));
        }
    }

    /*
     * The subroutine of DFS. NOTE: the set is used to distinguish between 1st
     * and 2nd round of DFS. set == null: finished vertices are stored (1st
     * round). set != null: all vertices found will be saved in the set (2nd
     * round)
     */
    private void dfsVisit(DirectedGraph<V, E> graph,
        VertexData<V> vertexData,
        Set<V> vertices)
    {
        Stack<VertexData<V>> stack = new Stack<VertexData<V>>();
        stack.push(vertexData);

        while (!stack.isEmpty()) {
            VertexData<V> data = stack.pop();

            if (!data.m_discovered) {
                data.m_discovered = true;

                if (vertices != null) {
                    vertices.add(data.m_vertex);
                }

                stack.push(new VertexData<V>(data, null, true, true));

                // follow all edges
                Iterator<? extends E> iter =
                    graph.outgoingEdgesOf(data.m_vertex).iterator();

                while (iter.hasNext()) {
                    E edge = iter.next();
                    VertexData<V> targetData =
                        m_vertexToVertexData.get(m_graph.getEdgeTarget(edge));

                    if (!targetData.m_discovered) {
                        // the "recursion"
                        stack.push(targetData);
                    }
                }
            } else if (data.m_finished) {
                if (vertices == null) {
                    m_orderedVertices.addFirst(data.m_finishedData);
                }
            }
        }
    }

    /*
     * Resets all VertexData objects.
     */
    private void resetVertexData()
    {
        Iterator<VertexData<V>> iter = m_vertexToVertexData.values().iterator();

        while (iter.hasNext()) {
            VertexData<V> data = iter.next();
            data.m_discovered = false;
            data.m_finished = false;
        }
    }

    //~ Inner Classes ---------------------------------------------------------

    /*
     * Lightweight class storing some data for every vertex.
     */
    private static final class VertexData<V>
    {
        // TODO jvs 24-June-2006:  more compact representation;
        // I added m_finishedData to clean up the generics warnings
        private final VertexData<V> m_finishedData;
        private final V m_vertex;
        private boolean m_discovered;
        private boolean m_finished;

        private VertexData(
            VertexData<V> finishedData,
            V vertex,
            boolean discovered,
            boolean finished)
        {
            m_finishedData = finishedData;
            m_vertex = vertex;
            m_discovered = discovered;
            m_finished = finished;
        }
    }
}
