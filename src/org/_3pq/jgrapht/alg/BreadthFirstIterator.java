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
/* -------------------------
 * BreadthFirstIterator.java
 * -------------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 24-Jul-2003 : Initial revision (BN);
 *
 */
package org._3pq.jgrapht.alg;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org._3pq.jgrapht.DirectedGraph;
import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.UndirectedGraph;

/**
 * A breadth-first iterator for a directed and an undirected graph. For this
 * iterator to work correctly the graph must not be modified during iteration.
 * Currently there are no means to ensure that, nor to fail-fast. The result
 * of such modifications are undefined.
 *
 * @author Barak Naveh
 *
 * @since Jul 19, 2003
 */
public class BreadthFirstIterator extends AbstractGraphIterator {
    // todo: support ConcurrentModificationException if graph modified during iteration. 
    private Iterator    m_vertexIterator = null;
    private Set         m_seen      = new HashSet(  );
    private SimpleQueue m_queue     = new SimpleQueue(  );
    private Specifics   m_specifics;

    /**
     * Creates a new BreadthFirstIterator object.
     *
     * @param g the directed graph to be iterated.
     * @param startVertex the vertex iteration to be started.
     */
    public BreadthFirstIterator( DirectedGraph g, Object startVertex ) {
        super(  );
        init( g, startVertex );
        m_specifics = new DirectedSpecifics( g );
    }


    /**
     * Creates a new BreadthFirstIterator object.
     *
     * @param g the undirected graph to be iterated.
     * @param startVertex the vertex iteration to be started.
     */
    public BreadthFirstIterator( UndirectedGraph g, Object startVertex ) {
        super(  );
        init( g, startVertex );
        m_specifics = new UndirectedSpecifics( g );
    }

    /**
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext(  ) {
        if( m_queue.isEmpty(  ) ) {
            if( isCrossComponentTraversal(  ) ) {
                while( m_vertexIterator.hasNext(  ) ) {
                    Object v = m_vertexIterator.next(  );

                    if( !m_seen.contains( v ) ) {
                        m_queue.add( v );
                        m_seen.add( v );

                        return true;
                    }
                }

                return false;
            }
            else {
                return false;
            }
        }
        else {
            return true;
        }
    }


    /**
     * @see java.util.Iterator#next()
     */
    public Object next(  ) {
        if( hasNext(  ) ) {
            Object nextVertex = m_queue.remove(  );
            fireVertexVisited( nextVertex );

            List edges = m_specifics.edgesOf( nextVertex );

            for( Iterator iter = edges.iterator(  ); iter.hasNext(  ); ) {
                Edge e = (Edge) iter.next(  );
                fireEdgeVisited( e );

                Object v = e.oppositeVertex( nextVertex );

                if( m_seen.add( v ) ) { // means: if NOT seen
                    m_queue.add( v );
                }
            }

            return nextVertex;
        }
        else {
            throw new NoSuchElementException(  );
        }
    }


    private void init( Graph g, Object startVertex ) {
        if( startVertex == null || g == null ) {
            throw new NullPointerException(  );
        }

        if( !g.containsVertex( startVertex ) ) {
            throw new IllegalArgumentException( "start vertex not in graph" );
        }

        // init BFS
        m_queue.add( startVertex );
        m_seen.add( startVertex );
        m_vertexIterator = g.vertexSet(  ).iterator(  );
    }

    /**
     * Provides unified interface for operations that are different in directed
     * graphs and in undirected graphs.
     *
     * @author Barak Naveh
     *
     * @since Jul 28, 2003
     */
    private abstract static class Specifics {
        /**
         * Returns the edges outgoing from the specified vertex in case of
         * directed graph, and the edge touching the specified vertex in case
         * of undirected graph.
         *
         * @param vertex the vertex whose outgoing edges are to be returned.
         *
         * @return
         */
        public abstract List edgesOf( Object vertex );
    }


    private static class DirectedSpecifics extends Specifics {
        private DirectedGraph m_graph;

        /**
         * Creates a new DirectedSpecifics object.
         *
         * @param g
         */
        public DirectedSpecifics( DirectedGraph g ) {
            m_graph = g;
        }

        /**
         * @see BreadthFirstIterator.Specifics#edgesOf(Object)
         */
        public List edgesOf( Object vertex ) {
            return m_graph.outgoingEdgesOf( vertex );
        }
    }


    /**
     * A simple queue structure.
     *
     * @author Barak Naveh
     *
     * @since Jul 19, 2003
     */
    private static class SimpleQueue {
        private LinkedList m_elementList = new LinkedList(  );

        /**
         * Tests if this queue is empty.
         *
         * @return <code>true</code> if empty, otherwise <code>false</code>.
         */
        public boolean isEmpty(  ) {
            return m_elementList.size(  ) == 0;
        }


        /**
         * Adds the specified object to the tail of the queue.
         *
         * @param o the object to be added.
         */
        public void add( Object o ) {
            m_elementList.addLast( o );
        }


        /**
         * Remove the object at the head of the queue and return it.
         *
         * @return the object and the head of the queue.
         */
        public Object remove(  ) {
            return m_elementList.removeFirst(  );
        }
    }


    private static class UndirectedSpecifics extends Specifics {
        private UndirectedGraph m_graph;

        /**
         * Creates a new DirectedSpecifics object.
         *
         * @param g
         */
        public UndirectedSpecifics( UndirectedGraph g ) {
            m_graph = g;
        }

        /**
         * @see BreadthFirstIterator.Specifics#edgesOf(Object)
         */
        public List edgesOf( Object vertex ) {
            return m_graph.edgesOf( vertex );
        }
    }
}
