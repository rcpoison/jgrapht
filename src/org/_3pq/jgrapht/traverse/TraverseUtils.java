/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Lead:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
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
/* ------------------
 * TraverseUtils.java
 * ------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   Liviu Rau
 *                   John V. Sichi
 *
 * $Id$
 *
 * Changes
 * -------
 * 31-Jul-2003 : Initial revision (BN);
 * 11-Aug-2003 : Adaptation to new event model (BN);
 * 31-Jan-2004 : Put into end-of-life stage (BN);
 *
 */
package org._3pq.jgrapht.traverse;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.event.ConnectedComponentTraversalEvent;
import org._3pq.jgrapht.event.EdgeTraversalEvent;
import org._3pq.jgrapht.event.VertexTraversalEvent;
import org._3pq.jgrapht.traverse.CrossComponentIterator.FlyweightEdgeEvent;
import org._3pq.jgrapht.traverse.CrossComponentIterator.FlyweightVertexEvent;

/**
 * A collection of utilities used for implementing traversal algorithms.
 * 
 * <p>
 * TODO: Delete this utility class in next release.
 * </p>
 *
 * @author Barak Naveh
 *
 * @since Jul 31, 2003
 * @deprecated will be deleted in next release.
 */
final class TraverseUtils {
    private TraverseUtils(  ) {} // ensure non-instantiability.

    /**
     * .
     *
     * @deprecated will be deleted in next release.
     */
    static interface SimpleContainer {
        /**
         * Tests if this container is empty.
         *
         * @return <code>true</code> if empty, otherwise <code>false</code>.
         */
        public boolean isEmpty(  );


        /**
         * Adds the specified object to this container.
         *
         * @param o the object to be added.
         */
        public void add( Object o );


        /**
         * Remove an object from this container and return it.
         *
         * @return the object removed from this container.
         */
        public Object remove(  );
    }

    /**
     * A common superclass for BreadthFirstIterator and DepthFirstIterator.
     *
     * @deprecated use {@link org._3pq.jgrapht.traverse.CrossComponentIterator}
     *             instead. will be deleted in next release.
     */
    static class XXFirstIterator extends AbstractGraphIterator {
        private static final int CCS_BEFORE_COMPONENT = 1;
        private static final int CCS_WITHIN_COMPONENT = 2;
        private static final int CCS_AFTER_COMPONENT  = 3;

        //
        private final ConnectedComponentTraversalEvent m_ccFinishedEvent =
            new ConnectedComponentTraversalEvent( this,
                ConnectedComponentTraversalEvent.CONNECTED_COMPONENT_FINISHED );
        private final ConnectedComponentTraversalEvent m_ccStartedEvent =
            new ConnectedComponentTraversalEvent( this,
                ConnectedComponentTraversalEvent.CONNECTED_COMPONENT_STARTED );
        private FlyweightEdgeEvent               m_reuseableEdgeEvent;
        private FlyweightVertexEvent             m_reuseableVertexEvent;
        private Iterator                         m_vertexIterator = null;
        private Map                              m_seen           =
            new HashMap(  );
        private SimpleContainer                  m_pending;
        private CrossComponentIterator.Specifics m_specifics;

        /** the connected component state */
        private int m_state = CCS_BEFORE_COMPONENT;

        /**
         * Creates a new iterator for the specified graph. Iteration will start
         * at the specified start vertex. If the specified start vertex is
         * <code>null</code>, Iteration will start at an arbitrary graph
         * vertex.
         *
         * @param g the graph to be iterated.
         * @param startVertex the vertex iteration to be started.
         * @param pendingVerticesContainer
         *
         * @throws NullPointerException
         * @throws IllegalArgumentException
         */
        public XXFirstIterator( Graph g, Object startVertex,
            SimpleContainer pendingVerticesContainer ) {
            super(  );

            if( g == null ) {
                throw new NullPointerException( "graph must not be null" );
            }

            m_pending     = pendingVerticesContainer;

            m_specifics          = CrossComponentIterator.createGraphSpecifics( g );
            m_vertexIterator     = g.vertexSet(  ).iterator(  );
            setCrossComponentTraversal( startVertex == null );

            m_reuseableEdgeEvent       = new FlyweightEdgeEvent( this, null );
            m_reuseableVertexEvent     = new FlyweightVertexEvent( this, null );

            if( startVertex == null ) {
                // pick a start vertex if graph not empty 
                if( m_vertexIterator.hasNext(  ) ) {
                    Object vStart = m_vertexIterator.next(  );
                    encounterVertex( vStart, null );
                }
            }
            else if( g.containsVertex( startVertex ) ) {
                encounterVertex( startVertex, null );
            }
            else {
                throw new IllegalArgumentException( 
                    "graph must contain the start vertex" );
            }
        }

        /**
         * @see java.util.Iterator#hasNext()
         */
        public boolean hasNext(  ) {
            if( m_pending.isEmpty(  ) ) {
                if( m_state == CCS_WITHIN_COMPONENT ) {
                    m_state = CCS_AFTER_COMPONENT;
                    fireConnectedComponentFinished( m_ccFinishedEvent );
                }

                if( isCrossComponentTraversal(  ) ) {
                    while( m_vertexIterator.hasNext(  ) ) {
                        Object v = m_vertexIterator.next(  );

                        if( !m_seen.containsKey( v ) ) {
                            encounterVertex( v, null );
                            m_state = CCS_BEFORE_COMPONENT;

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
                if( m_state == CCS_BEFORE_COMPONENT ) {
                    m_state = CCS_WITHIN_COMPONENT;
                    fireConnectedComponentStarted( m_ccStartedEvent );
                }

                Object nextVertex = m_pending.remove(  );
                fireVertexTraversed( createVertexTraversalEvent( nextVertex ) );

                addUnseenChildrenOf( nextVertex );

                return nextVertex;
            }
            else {
                throw new NoSuchElementException(  );
            }
        }


        /**
         * Access the data stored by newSeenData.
         *
         * @param vertex a vertex which has already been seen
         *
         * @return data associated when vertex was first seen
         */
        protected Object getSeenData( Object vertex ) {
            return m_seen.get( vertex );
        }


        /**
         * Called whenever we re-encounter a vertex.  The default
         * implementation does nothing.
         *
         * @param vertex the vertex re-encountered
         * @param edge the edge via which the vertex was re-encountered
         */
        protected void encounterVertexAgain( Object vertex, Edge edge ) {}


        /**
         * Give subclasses a place to record private data associated with each
         * vertex.  The default implementation just returns the vertex itself,
         * causing m_seen to degenerate into a Set rather than a Map.
         *
         * @param vertex a vertex which has just been encountered
         * @param edge the edge via which the vertex was encountered
         *
         * @return the data to associate with this vertex
         */
        protected Object newSeenData( Object vertex, Edge edge ) {
            return vertex;
        }


        /**
         * Update data structures the first time we see a vertex.
         *
         * @param vertex the vertex encountered
         * @param edge the edge via which the vertex was encountered, or null
         *        if the vertex is a starting point
         */
        private final void encounterVertex( Object vertex, Edge edge ) {
            Object seenData = newSeenData( vertex, edge );
            m_seen.put( vertex, seenData );
            m_pending.add( seenData );
        }


        private void addUnseenChildrenOf( Object vertex ) {
            List edges = m_specifics.edgesOf( vertex );

            for( Iterator iter = edges.iterator(  ); iter.hasNext(  ); ) {
                Edge e = (Edge) iter.next(  );
                fireEdgeTraversed( createEdgeTraversalEvent( e ) );

                Object v = e.oppositeVertex( vertex );

                if( m_seen.containsKey( v ) ) {
                    encounterVertexAgain( v, e );
                }
                else {
                    encounterVertex( v, e );
                }
            }
        }


        private EdgeTraversalEvent createEdgeTraversalEvent( Edge edge ) {
            if( isReuseEvents(  ) ) {
                m_reuseableEdgeEvent.setEdge( edge );

                return m_reuseableEdgeEvent;
            }
            else {
                return new EdgeTraversalEvent( this, edge );
            }
        }


        private VertexTraversalEvent createVertexTraversalEvent( Object vertex ) {
            if( isReuseEvents(  ) ) {
                m_reuseableVertexEvent.setVertex( vertex );

                return m_reuseableVertexEvent;
            }
            else {
                return new VertexTraversalEvent( this, vertex );
            }
        }
    }
}
