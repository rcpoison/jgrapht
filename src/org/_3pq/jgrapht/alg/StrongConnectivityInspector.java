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
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */
package org._3pq.jgrapht.alg;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

import org._3pq.jgrapht.DirectedGraph;
import org._3pq.jgrapht.GraphHelper;
import org._3pq.jgrapht.edge.DirectedEdge;
import org._3pq.jgrapht.graph.DefaultDirectedGraph;

/**
 * <p>
 * Complements the {@link ConnectivityInspector} class with the capability to
 * compute the strongly connected components of a directed graph. The
 * algorithm is implemented after "Corman et al: Introduction to agorithms",
 * Chapter 25.2. It has a running time of O(V + E).
 * </p>
 * 
 * <p>
 * Unlike ConnectivityInspector, this class does not implement incremental
 * inspection. The full algorithm is executed at the first call of  {@link
 * StrongConnectivityInspector#stronglyConnectedSets()} or {@link
 * StrongConnectivityInspector#isStronglyConnected()}.
 * </p>
 *
 * @author Christian Soltenborn
 *
 * @since Feb 2, 2005
 */
public class StrongConnectivityInspector {
    // the graph to compute the strongly connected sets for
    private DirectedGraph m_graph;

    // stores the vertices, ordered by their finishing time in first dfs
    private LinkedList m_orderedVertices;

    // the result of the computation, cached for future calls
    private List m_stronglyConnectedSets;

    // maps vertices to their VertexData object
    private Map m_verticesToVerticesData;

    /**
     * The constructor for the StrongConnectivityInspector class.
     *
     * @param directedGraph the graph to inspect
     */
    public StrongConnectivityInspector( DirectedGraph directedGraph ) {
        m_graph                      = directedGraph;
        m_verticesToVerticesData     = null;
        m_orderedVertices            = null;
        m_stronglyConnectedSets      = null;
    }

    /**
     * Returns true if the graph of this
     * <code>StronglyConnectivityInspector</code> instance is strongly
     * connected.
     *
     * @return true if the graph is strongly connected
     */
    public boolean isStronglyConnected(  ) {
        return stronglyConnectedSets(  ).size(  ) == 1;
    }


    /**
     * Computes a {@link List} of {@link Set}s, where each set contains
     * vertices which together form a strongly connected component within the
     * given graph.
     *
     * @return <code>List</code> of <code>Set</code>s containing the strongly
     *         connected components
     */
    public List stronglyConnectedSets(  ) {
        if( m_stronglyConnectedSets == null ) {
            m_verticesToVerticesData =
                new HashMap( m_graph.vertexSet(  ).size(  ) );
            m_orderedVertices           = new LinkedList(  );
            m_stronglyConnectedSets =
                new Vector( m_graph.vertexSet(  ).size(  ) );

            // create VertexData objects for all vertices, store them
            Iterator iter = m_graph.vertexSet(  ).iterator(  );

            while( iter.hasNext(  ) ) {
                Object vertex = iter.next(  );
                m_verticesToVerticesData.put( vertex,
                    new VertexData( vertex, false, false ) );
            }

            // perform the first round of DFS, result is an ordering
            // of the vertices by decreasing finishing time
            iter = m_verticesToVerticesData.values(  ).iterator(  );

            while( iter.hasNext(  ) ) {
                VertexData data = (VertexData) iter.next(  );

                if( !data.m_discovered ) {
                    dfsVisit( m_graph, data, null );
                }
            }

            // calculate inverse graph (i.e. every edge is turned)
            DirectedGraph inverseGraph = new DefaultDirectedGraph(  );
            GraphHelper.addGraphReversed( inverseGraph, m_graph );

            // get ready for next dfs round
            resetVertexData(  );

            // second dfs round: vertices are considered in decreasing
            // finishing time order; every tree found is a strongly
            // connected set
            iter = m_orderedVertices.iterator(  );

            while( iter.hasNext(  ) ) {
                VertexData data = (VertexData) iter.next(  );

                if( !data.m_discovered ) {
                    // new strongly connected set
                    Set set = new HashSet(  );
                    m_stronglyConnectedSets.add( set );
                    dfsVisit( inverseGraph, data, set );
                }
            }

            // clean up for garbage collection
            m_graph                      = null;
            m_orderedVertices            = null;
            m_verticesToVerticesData     = null;
        }

        return m_stronglyConnectedSets;
    }


    /*
     * The subroutine of DFS.
     * NOTE: the set is used to distinguish between 1st and 2nd round of DFS.
     * set == null: finished vertices are stored (1st round).
     * set != null: all vertices found will be saved in the set (2nd round)
     */
    private void dfsVisit( DirectedGraph graph, VertexData vertexData,
        Set vertices ) {
        Stack stack = new Stack(  );
        stack.push( vertexData );

        while( !stack.isEmpty(  ) ) {
            VertexData data = (VertexData) stack.pop(  );

            if( !data.m_discovered ) {
                data.m_discovered = true;

                if( vertices != null ) {
                    vertices.add( data.m_vertex );
                }

                // TODO: other way to identify when this vertex is finished!?
                stack.push( new VertexData( data, true, true ) );

                // follow all edges
                Iterator iter =
                    graph.outgoingEdgesOf( data.m_vertex ).iterator(  );

                while( iter.hasNext(  ) ) {
                    DirectedEdge edge       = (DirectedEdge) iter.next(  );
                    VertexData   targetData =
                        (VertexData) m_verticesToVerticesData.get( edge
                            .getTarget(  ) );

                    if( !targetData.m_discovered ) {
                        // the "recursion"
                        stack.push( targetData );
                    }
                }
            }
            else if( data.m_finished ) {
                if( vertices == null ) {
                    // see TODO above
                    m_orderedVertices.addFirst( data.m_vertex );
                }
            }
        }
    }


    /*
     * Resets all VertexData objects.
     */
    private void resetVertexData(  ) {
        Iterator iter = m_orderedVertices.iterator(  );

        while( iter.hasNext(  ) ) {
            VertexData data = (VertexData) iter.next(  );
            data.m_discovered     = false;
            data.m_finished       = false;
        }
    }

    /*
     * Lightweight class storing some data vor every vertex.
     */
    private final class VertexData {
        private final Object m_vertex;
        private boolean      m_discovered;
        private boolean      m_finished;

        private VertexData( Object vertex, boolean discovered, boolean finished ) {
            m_vertex         = vertex;
            m_discovered     = discovered;
            m_finished       = finished;
        }
    }
}
