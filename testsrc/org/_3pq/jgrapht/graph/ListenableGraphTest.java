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
/* ------------------------
 * ListenableGraphTest.java
 * ------------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 03-Aug-2003 : Initial revision (BN);
 * 10-Aug-2003 : Adaptation to new event model (BN);
 *
 */
package org._3pq.jgrapht.graph;

import junit.framework.TestCase;

import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.ListenableGraph;
import org._3pq.jgrapht.event.GraphEdgeChangeEvent;
import org._3pq.jgrapht.event.GraphListener;
import org._3pq.jgrapht.event.GraphVertexChangeEvent;
import org._3pq.jgrapht.event.VertexSetListener;

/**
 * Unit test for {@link ListenableGraph} class.
 *
 * @author Barak Naveh
 *
 * @since Aug 3, 2003
 */
public class ListenableGraphTest extends TestCase {
    Edge   m_lastAddedEdge;
    Edge   m_lastRemovedEdge;
    Object m_lastAddedVertex;
    Object m_lastRemovedVertex;

    /**
     * @see junit.framework.TestCase#TestCase(java.lang.String)
     */
    public ListenableGraphTest( String name ) {
        super( name );
    }

    /**
     * Tests GraphListener listener.
     */
    public void testGraphListener(  ) {
        init(  );

        ListenableGraph g        = new ListenableUndirectedGraph(  );
        GraphListener   listener = new MyGraphListner(  );
        g.addGraphListener( listener );

        String v1 = new String( "v1" );
        String v2 = new String( "v2" );

        // test vertex notification     
        g.addVertex( v1 );
        assertEquals( v1, m_lastAddedVertex );
        assertEquals( null, m_lastRemovedVertex );

        init(  );
        g.removeVertex( v1 );
        assertEquals( v1, m_lastRemovedVertex );
        assertEquals( null, m_lastAddedVertex );

        // test edge notification      
        g.addVertex( v1 );
        g.addVertex( v2 );

        init(  );

        Edge e = g.addEdge( v1, v2 );
        assertEquals( e, m_lastAddedEdge );
        assertEquals( null, m_lastRemovedEdge );

        init(  );
        assertTrue( g.removeEdge( e ) );
        assertEquals( e, m_lastRemovedEdge );
        assertEquals( null, m_lastAddedEdge );

        g.removeVertex( v1 );
        g.removeVertex( v2 );

        // 
        // test notification stops when removing listener
        //
        g.removeGraphListener( listener );
        init(  );
        g.addVertex( v1 );
        g.addVertex( v2 );
        e = g.addEdge( v1, v2 );
        g.removeEdge( e );

        assertEquals( null, m_lastAddedEdge );
        assertEquals( null, m_lastAddedVertex );
        assertEquals( null, m_lastRemovedEdge );
        assertEquals( null, m_lastRemovedVertex );
    }


    /**
     * Tests VertexSetListener listener.
     */
    public void testVertexSetListener(  ) {
        init(  );

        ListenableGraph   g        = new ListenableUndirectedGraph(  );
        VertexSetListener listener = new MyGraphListner(  );
        g.addVertexSetListener( listener );

        String v1 = new String( "v1" );
        String v2 = new String( "v2" );

        // test vertex notification     
        g.addVertex( v1 );
        assertEquals( v1, m_lastAddedVertex );
        assertEquals( null, m_lastRemovedVertex );

        init(  );
        g.removeVertex( v1 );
        assertEquals( v1, m_lastRemovedVertex );
        assertEquals( null, m_lastAddedVertex );

        // test edge notification      
        g.addVertex( v1 );
        g.addVertex( v2 );

        init(  );

        Edge e = g.addEdge( v1, v2 );
        assertEquals( null, m_lastAddedEdge );
        assertEquals( null, m_lastRemovedEdge );

        init(  );
        assertTrue( g.removeEdge( e ) );
        assertEquals( null, m_lastRemovedEdge );
        assertEquals( null, m_lastAddedEdge );

        g.removeVertex( v1 );
        g.removeVertex( v2 );

        // 
        // test notification stops when removing listener
        //
        g.removeVertexSetListener( listener );
        init(  );
        g.addVertex( v1 );
        g.addVertex( v2 );
        e = g.addEdge( v1, v2 );
        g.removeEdge( e );

        assertEquals( null, m_lastAddedEdge );
        assertEquals( null, m_lastAddedVertex );
        assertEquals( null, m_lastRemovedEdge );
        assertEquals( null, m_lastRemovedVertex );
    }


    private void init(  ) {
        m_lastAddedEdge         = null;
        m_lastAddedVertex       = null;
        m_lastRemovedEdge       = null;
        m_lastRemovedVertex     = null;
    }

    /**
     * A listener on the tested graph.
     *
     * @author Barak Naveh
     *
     * @since Aug 3, 2003
     */
    private class MyGraphListner implements GraphListener {
        /**
         * @see GraphListener#edgeAdded(GraphEdgeChangeEvent)
         */
        public void edgeAdded( GraphEdgeChangeEvent e ) {
            m_lastAddedEdge = e.getEdge(  );
        }


        /**
         * @see GraphListener#edgeRemoved(GraphEdgeChangeEvent)
         */
        public void edgeRemoved( GraphEdgeChangeEvent e ) {
            m_lastRemovedEdge = e.getEdge(  );
        }


        /**
         * @see VertexSetListener#vertexAdded(GraphVertexChangeEvent)
         */
        public void vertexAdded( GraphVertexChangeEvent e ) {
            m_lastAddedVertex = e.getVertex(  );
        }


        /**
         * @see VertexSetListener#vertexRemoved(GraphVertexChangeEvent)
         */
        public void vertexRemoved( GraphVertexChangeEvent e ) {
            m_lastRemovedVertex = e.getVertex(  );
        }
    }
}
