/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Lead:  Barak Naveh (barak_naveh@users.sourceforge.net)
 *
 * (C) Copyright 2003, by Barak Naveh and Contributors.
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
/* --------------------------
 * ConnectivityInspector.java
 * --------------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 06-Aug-2003 : Initial revision (BN);
 *
 */
package org._3pq.jgrapht.alg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.GraphListener;
import org._3pq.jgrapht.traverse.BreadthFirstIterator;
import org._3pq.jgrapht.traverse.TraversalListenerAdapter;

/**
 * Allows obtaining various connectivity aspects of a graph. The <i>inspected
 * graph</i> is specified at construction time and cannot be modified.
 * Currently, the inspector supports connected components for an undirected
 * graph and weakly connected components for a directed graph.  (TODO:
 * implement Tarjan's algorithm for strongly connected component discovery.)
 * 
 * <p>
 * The inspector methods work in a lazy fashion: no computation is performed
 * unless immediately necessary. Computation are done once and results and
 * cached within this class for future need.
 * </p>
 * 
 * <p>
 * The inspector is also a {@link org._3pq.jgrapht.GraphListener}. If added as
 * a listener to the inspected graph, the inspector will amend internal cached
 * results instead of recomputing them. It is efficient when a few
 * modifications are applied to a large graph. If many modifications are
 * expected it will not be efficient due to added overhead on graph update
 * operations. If inspector is added as listener to a graph other than the one
 * it inspects, results are undefined.
 * </p>
 *
 * @author Barak Naveh
 * @author John V. Sichi (original author of a similar class in OpenJGraph)
 *
 * @since Aug 6, 2003
 */
public class ConnectivityInspector implements GraphListener {
    List          m_connectedSets;
    Map           m_vertexToConnectedSet;
    private Graph m_graph;

    /**
     * Creates a connectivity inspector for the specified graph.
     *
     * @param g the graph for which a connectivity inspector to be created.
     */
    public ConnectivityInspector( Graph g ) {
        init(  );
        m_graph = g;
    }

    /**
     * Test if the inspected graph is connected. An empty graph is <i>not</i>
     * considered connected.
     *
     * @return <code>true</code> if and only if inspected graph is connected.
     */
    public boolean isGraphConnected(  ) {
        return lazyFindConnectedSets(  ).size(  ) == 1;
    }


    /**
     * Returns a set of all vertices that are in the maximally connected
     * component together with the specified vertex. For more on maximally
     * connected component, see <a
     * href="http://www.nist.gov/dads/HTML/maximallyConnectedComponent.html">
     * http://www.nist.gov/dads/HTML/maximallyConnectedComponent.html</a>.
     *
     * @param vertex the vertex for which the connected set to be returned.
     *
     * @return a set of all vertices that are in the maximally connected
     *         component together with the specified vertex.
     */
    public Set connectedSetOf( Object vertex ) {
        Set connectedSet = (Set) m_vertexToConnectedSet.get( vertex );

        if( connectedSet == null ) {
            connectedSet = new HashSet(  );

            BreadthFirstIterator i =
                new BreadthFirstIterator( m_graph, vertex, true );

            while( i.hasNext(  ) ) {
                connectedSet.add( i.next(  ) );
            }

            m_vertexToConnectedSet.put( vertex, connectedSet );
        }

        return connectedSet;
    }


    /**
     * Returns a list of <code>Set</code>s, where each set contains all
     * vertices that are in the same maximally connected component. All graph
     * vertices occur in exactly one set.  For more on maximally connected
     * component, see <a
     * href="http://www.nist.gov/dads/HTML/maximallyConnectedComponent.html">
     * http://www.nist.gov/dads/HTML/maximallyConnectedComponent.html</a>.
     *
     * @return Returns a list of <code>Set</code>s, where each set contains all
     *         vertices that are in the same maximally connected component.
     */
    public List connectedSets(  ) {
        return lazyFindConnectedSets(  );
    }


    /**
     * @see org._3pq.jgrapht.GraphListener#edgeAdded(Edge)
     */
    public void edgeAdded( Edge e ) {
        init(  ); // for now invalidate cached results, in the future need to amend them. 
    }


    /**
     * @see org._3pq.jgrapht.GraphListener#edgeRemoved(Edge)
     */
    public void edgeRemoved( Edge e ) {
        init(  ); // for now invalidate cached results, in the future need to amend them. 
    }


    /**
     * Tests if there is a path from the specified source vertex to the
     * specified target vertices. For a directed graph, direction is ignored
     * for this interpretation of path.
     * 
     * <p>
     * TODO: BEFORE RELEASE: Ignoring edge direction for directed graph can be
     * confusing. For directed graphs, consider Dijkstra's algorithm.
     * </p>
     *
     * @param sourceVertex one end of the path.
     * @param targetVertex another end of the path.
     *
     * @return <code>true</code> if and only if there is a path from the source
     *         vertex to the target vertex.
     */
    public boolean pathExists( Object sourceVertex, Object targetVertex ) {
        Set sourceSet = connectedSetOf( sourceVertex );

        return sourceSet.contains( targetVertex );
    }


    /**
     * @see org._3pq.jgrapht.VertexSetListener#vertexAdded(Object)
     */
    public void vertexAdded( Object v ) {
        init(  ); // for now invalidate cached results, in the future need to amend them. 
    }


    /**
     * @see org._3pq.jgrapht.VertexSetListener#vertexRemoved(Object)
     */
    public void vertexRemoved( Object v ) {
        init(  ); // for now invalidate cached results, in the future need to amend them. 
    }


    private void init(  ) {
        m_connectedSets            = null;
        m_vertexToConnectedSet     = new HashMap(  );
    }


    private List lazyFindConnectedSets(  ) {
        if( m_connectedSets == null ) {
            m_connectedSets = new ArrayList(  );

            Set vertexSet = m_graph.vertexSet(  );

            if( vertexSet.size(  ) > 0 ) {
                BreadthFirstIterator i =
                    new BreadthFirstIterator( m_graph, null, true );
                i.addTraversalListener( new MyTraversalListener(  ) );

                while( i.hasNext(  ) ) {
                    i.next(  );
                }
            }
        }

        return m_connectedSets;
    }

    /**
     * A traversal listener that groups all vertices according to to their
     * containing connected set.
     *
     * @author Barak Naveh
     *
     * @since Aug 6, 2003
     */
    private class MyTraversalListener extends TraversalListenerAdapter {
        private Set m_currentConnectedSet;

        /**
         * @see org._3pq.jgrapht.TraversalListener#connectedComponentFinished()
         */
        public void connectedComponentFinished(  ) {
            m_connectedSets.add( m_currentConnectedSet );
        }


        /**
         * @see org._3pq.jgrapht.TraversalListener#connectedComponentStarted()
         */
        public void connectedComponentStarted(  ) {
            m_currentConnectedSet = new HashSet(  );
        }


        /**
         * @see org._3pq.jgrapht.TraversalListener#vertexVisited(Object)
         */
        public void vertexVisited( Object vertex ) {
            m_currentConnectedSet.add( vertex );
            m_vertexToConnectedSet.put( vertex, m_currentConnectedSet );
        }
    }
}
