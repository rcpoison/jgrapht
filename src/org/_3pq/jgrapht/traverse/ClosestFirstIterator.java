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
 * ClosestFirstIterator.java
 * -------------------------
 * (C) Copyright 2003, by John V. Sichi and Contributors.
 *
 * Original Author:  John V. Sichi
 *
 * $Id$
 *
 * Changes
 * -------
 * 2-Sep-2003 : Initial revision (JVS);
 *
 */
package org._3pq.jgrapht.traverse;

import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.traverse.TraverseUtils.XXFirstIterator;
import org._3pq.jgrapht.util.FibonacciHeap;

/**
 * A closest-first iterator for a directed or undirected graph. For this
 * iterator to work correctly the graph must not be modified during iteration.
 * Currently there are no means to ensure that, nor to fail-fast. The result
 * of such modifications is undefined.
 * 
 * <p>
 * The metric for <i>closest</i> here is the path length from a start vertex.
 * Edge.getWeight() is summed to calculate path length. Negative edge weights
 * will result in an IllegalArgumentException.
 * </p>
 *
 * @author John V. Sichi
 *
 * @since Sep 2, 2003
 */
public class ClosestFirstIterator extends XXFirstIterator {
    /** Priority queue of fringe vertices (refined alias for super.m_pending). */
    private ClosestFirstQueue m_queue;

    /**
     * Creates a new closest-first iterator for the specified graph.
     *
     * @param g the graph to be iterated.
     */
    public ClosestFirstIterator( Graph g ) {
        this( g, null );
    }


    /**
     * Creates a new closest-first iterator for the specified graph. Iteration
     * will start at the specified start vertex and will be limited to the
     * connected component that includes that vertex. If the specified start
     * vertex is <code>null</code>, iteration will start at an arbitrary
     * vertex and will not be limited, that is, will be able to traverse all
     * the graph.
     *
     * @param g the graph to be iterated.
     * @param startVertex the vertex iteration to be started.
     */
    public ClosestFirstIterator( Graph g, Object startVertex ) {
        this( g, startVertex, new ClosestFirstQueue(  ) );
    }


    private ClosestFirstIterator( Graph g, Object startVertex,
        ClosestFirstQueue queue ) {
        super( g, startVertex, queue );

        // REVIEW: This is a little dangerous since it relies on the fact that
        // m_queue isn't accessed from any of the methods called from the
        // superclass constructor.  It would be cleaner to change the
        // superclass constructor to do nothing, and have the subclasses call
        // an init() method once they're fully initialized.
        m_queue = queue;
    }

    /**
     * Get the spanning tree edge reaching a vertex which has been seen already
     * in this traversal.  This edge is the last link in the shortest known
     * path between the start vertex and the requested vertex.  If the vertex
     * has already been visited, then it is truly the minimum spanning tree
     * edge; otherwise, it is the best candidate seen so far.
     *
     * @param vertex the spanned vertex.
     *
     * @return the spanning tree edge, or null if the vertex either has not
     *         been seen yet or is the start vertex.
     */
    public Edge getSpanningTreeEdge( Object vertex ) {
        QueueEntry entry = (QueueEntry) getSeenData( vertex );

        if( entry == null ) {
            return null;
        }

        return entry.m_spanningTreeEdge;
    }


    /**
     * Override superclass.  When we see a vertex again, we need to see if the
     * new edge provides a shorter path than the old edge.
     *
     * @param vertex the vertex re-encountered
     * @param edge the edge via which the vertex was re-encountered
     */
    protected void encounterVertexAgain( Object vertex, Edge edge ) {
        QueueEntry entry = (QueueEntry) getSeenData( vertex );

        if( entry.m_frozen ) {
            // no improvement for this vertex possible
            return;
        }

        double candidatePathLength = calculatePathLength( vertex, edge );

        if( candidatePathLength < entry.getShortestPathLength(  ) ) {
            entry.m_spanningTreeEdge = edge;
            m_queue.m_heap.decreaseKey( entry, candidatePathLength );
        }
    }


    /**
     * Override superclass.  The first time we see a vertex, make up a new
     * queue entry for it.  Superclass will add this to the queue for us.
     *
     * @param vertex a vertex which has just been encountered.
     * @param edge the edge via which the vertex was encountered.
     *
     * @return the new queue entry.
     */
    protected Object newSeenData( Object vertex, Edge edge ) {
        double shortestPathLength;

        if( edge == null ) {
            shortestPathLength = 0;
        }
        else {
            shortestPathLength = calculatePathLength( vertex, edge );
        }

        QueueEntry entry = new QueueEntry( shortestPathLength );
        entry.m_vertex               = vertex;
        entry.m_spanningTreeEdge     = edge;

        return entry;
    }


    private void assertNonNegativeEdge( Edge edge ) {
        if( edge.getWeight(  ) < 0 ) {
            throw new IllegalArgumentException( 
                "negative edge weights not allowed" );
        }
    }


    /**
     * Determine path length to a vertex via an edge, using the path length for
     * the opposite vertex.
     *
     * @param vertex the vertex for which to calculate the path length.
     * @param edge the edge via which the path is being extended.
     *
     * @return calculated path length.
     */
    private double calculatePathLength( Object vertex, Edge edge ) {
        assertNonNegativeEdge( edge );

        Object     otherVertex = edge.oppositeVertex( vertex );
        QueueEntry otherEntry = (QueueEntry) getSeenData( otherVertex );

        return otherEntry.getShortestPathLength(  ) + edge.getWeight(  );
    }

    /**
     * Adapter from FibonnaciHeap to SimpleContainer.
     */
    private static class ClosestFirstQueue
        implements TraverseUtils.SimpleContainer {
        FibonacciHeap m_heap;

        ClosestFirstQueue(  ) {
            m_heap = new FibonacciHeap(  );
        }

        /**
         * .
         *
         * @return
         */
        public boolean isEmpty(  ) {
            return m_heap.size(  ) == 0;
        }


        /**
         * .
         *
         * @param obj
         */
        public void add( Object obj ) {
            QueueEntry newEntry = (QueueEntry) obj;
            m_heap.insert( newEntry, newEntry.getShortestPathLength(  ) );
        }


        /**
         * .
         *
         * @return
         */
        public Object remove(  ) {
            QueueEntry entry = (QueueEntry) m_heap.removeMin(  );
            entry.m_frozen = true;

            return entry.m_vertex;
        }
    }


    /**
     * Private data to associate with each entry in the priority queue.
     */
    private static class QueueEntry extends FibonacciHeap.Node {
        /** Best spanning tree edge to vertex seen so far. */
        Edge m_spanningTreeEdge;

        /** The vertex reached. */
        Object m_vertex;

        /** True once m_spanningTreeEdge is guaranteed to be the true minimum. */
        boolean m_frozen;

        QueueEntry( double key ) {
            super( key );
        }

        double getShortestPathLength(  ) {
            return getKey(  );
        }
    }
}
