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
/* ---------------------------
 * DefaultListenableGraph.java
 * ---------------------------
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
 * 04-Aug-2003 : Strong refs to listeners instead of weak refs (BN);
 *
 */
package org._3pq.jgrapht.graph;

import java.util.ArrayList;

import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.GraphListener;
import org._3pq.jgrapht.ListenableGraph;
import org._3pq.jgrapht.VertexSetListener;
import org._3pq.jgrapht.WeightedGraph;

/**
 * A graph backed by the the graph specified at the constructor, which can be
 * listened by <code>GraphListener</code>s and by
 * <code>VertexSetListener</code>s. Operations on this graph "pass through" to
 * the to the backing graph. Any modification made to this graph or the
 * backing graph is reflected by the other.
 * 
 * <p>
 * This graph does <i>not</i> pass the hashCode and equals operations through
 * to the backing graph, but relies on <tt>Object</tt>'s <tt>equals</tt> and
 * <tt>hashCode</tt> methods.
 * </p>
 *
 * @author Barak Naveh
 *
 * @see GraphListener
 * @see VertexSetListener
 * @since Jul 20, 2003
 */
public class DefaultListenableGraph extends GraphDelegator
    implements ListenableGraph, Cloneable {
    private ArrayList m_graphListeners     = new ArrayList(  );
    private ArrayList m_vertexSetListeners = new ArrayList(  );

    /**
     * Constructor for DefaultListenableGraph.
     *
     * @param g the backing graph.
     *
     * @throws IllegalArgumentException
     *
     * @see DefaultListenableGraph
     */
    public DefaultListenableGraph( Graph g ) {
        super( g );

        // the following restriction could be relaxed in the future.
        if( g instanceof ListenableGraph ) {
            throw new IllegalArgumentException( 
                "base graph cannot be listenable" );
        }
    }

    /**
     * @see WeightedGraph#addEdge(Object, Object, double)
     */
    public Edge addEdge( Object sourceVertex, Object targetVertex, double weight ) {
        Edge e =
            ( (WeightedGraph) m_delegate ).addEdge( sourceVertex, targetVertex,
                weight );

        if( e != null ) {
            fireEdgeAdded( e );
        }

        return e;
    }


    /**
     * @see Graph#addEdge(Object, Object)
     */
    public Edge addEdge( Object sourceVertex, Object targetVertex ) {
        Edge e = m_delegate.addEdge( sourceVertex, targetVertex );

        if( e != null ) {
            fireEdgeAdded( e );
        }

        return e;
    }


    /**
     * @see Graph#addEdge(Edge)
     */
    public boolean addEdge( Edge e ) {
        boolean modified = m_delegate.addEdge( e );

        if( modified ) {
            fireEdgeAdded( e );
        }

        return modified;
    }


    /**
     * @see ListenableGraph#addGraphListener(GraphListener)
     */
    public void addGraphListener( GraphListener l ) {
        if( !m_graphListeners.contains( l ) ) {
            m_graphListeners.add( l );
        }
    }


    /**
     * @see Graph#addVertex(Object)
     */
    public boolean addVertex( Object v ) {
        boolean modified = m_delegate.addVertex( v );

        if( modified ) {
            fireVertexAdded( v );
        }

        return modified;
    }


    /**
     * @see ListenableGraph#addVertexSetListener(VertexSetListener)
     */
    public void addVertexSetListener( VertexSetListener l ) {
        if( !m_vertexSetListeners.contains( l ) ) {
            m_vertexSetListeners.add( l );
        }
    }


    /**
     * @see java.lang.Object#clone()
     */
    public Object clone(  ) {
        try {
            DefaultListenableGraph g = (DefaultListenableGraph) super.clone(  );
            g.m_graphListeners         = (ArrayList) m_graphListeners.clone(  );
            g.m_vertexSetListeners     = (ArrayList) m_vertexSetListeners.clone(  );

            return g;
        }
         catch( CloneNotSupportedException e ) {
            // should never get here since we're Cloneable
            e.printStackTrace(  );
            throw new RuntimeException( "internal error" );
        }
    }


    /**
     * @see Graph#removeEdge(Object, Object)
     */
    public Edge removeEdge( Object sourceVertex, Object targetVertex ) {
        Edge e = m_delegate.removeEdge( sourceVertex, targetVertex );

        if( e != null ) {
            fireEdgeRemoved( e );
        }

        return e;
    }


    /**
     * @see Graph#removeEdge(Edge)
     */
    public boolean removeEdge( Edge e ) {
        boolean modified = m_delegate.removeEdge( e );

        if( modified ) {
            fireEdgeRemoved( e );
        }

        return modified;
    }


    /**
     * @see ListenableGraph#removeGraphListener(GraphListener)
     */
    public void removeGraphListener( GraphListener l ) {
        m_graphListeners.remove( l );
    }


    /**
     * @see Graph#removeVertex(Object)
     */
    public boolean removeVertex( Object v ) {
        boolean modified = m_delegate.removeVertex( v );

        if( modified ) {
            fireVertexRemoved( v );
        }

        return modified;
    }


    /**
     * @see ListenableGraph#removeVertexSetListener(VertexSetListener)
     */
    public void removeVertexSetListener( VertexSetListener l ) {
        m_vertexSetListeners.remove( l );
    }


    /**
     * Notify listeners that the specified edge was added.
     *
     * @param edge the edge that was added.
     */
    protected void fireEdgeAdded( Edge edge ) {
        int len = m_graphListeners.size(  );

        for( int i = 0; i < len; i++ ) {
            GraphListener l = (GraphListener) m_graphListeners.get( i );

            l.edgeAdded( edge );
        }
    }


    /**
     * Notify listeners that the specified edge was removed.
     *
     * @param edge the edge that was removed.
     */
    protected void fireEdgeRemoved( Edge edge ) {
        int len = m_graphListeners.size(  );

        for( int i = 0; i < len; i++ ) {
            GraphListener l = (GraphListener) m_graphListeners.get( i );

            l.edgeRemoved( edge );
        }
    }


    /**
     * Notify listeners that the specified vertex was added.
     *
     * @param vertex the vertex that was added.
     */
    protected void fireVertexAdded( Object vertex ) {
        int len;

        len = m_vertexSetListeners.size(  );

        for( int i = 0; i < len; i++ ) {
            VertexSetListener l =
                (VertexSetListener) m_vertexSetListeners.get( i );

            l.vertexAdded( vertex );
        }

        len = m_graphListeners.size(  );

        for( int i = 0; i < len; i++ ) {
            GraphListener l = (GraphListener) m_graphListeners.get( i );

            l.vertexAdded( vertex );
        }
    }


    /**
     * Notify listeners that the specified vertex was removed.
     *
     * @param vertex the vertex that was removed.
     */
    protected void fireVertexRemoved( Object vertex ) {
        int len;

        len = m_vertexSetListeners.size(  );

        for( int i = 0; i < len; i++ ) {
            VertexSetListener l =
                (VertexSetListener) m_vertexSetListeners.get( i );

            l.vertexRemoved( vertex );
        }

        len = m_graphListeners.size(  );

        for( int i = 0; i < len; i++ ) {
            GraphListener l = (GraphListener) m_graphListeners.get( i );

            l.vertexRemoved( vertex );
        }
    }
}
