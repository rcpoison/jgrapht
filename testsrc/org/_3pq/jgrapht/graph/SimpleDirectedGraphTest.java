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
/* ----------------------------
 * SimpleDirectedGraphTest.java
 * ----------------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 25-Jul-2003 : Initial revision (BN);
 *
 */
package org._3pq.jgrapht.graph;

import org._3pq.jgrapht.DirectedEdge;
import org._3pq.jgrapht.DirectedGraph;
import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.EdgeFactory;
import org._3pq.jgrapht.EnhancedTestCase;

/**
 * A unit test for simple directed graph.
 *
 * @author Barak Naveh
 *
 * @since Jul 25, 2003
 */
public class SimpleDirectedGraphTest extends EnhancedTestCase {
    DirectedGraph         m_gEmpty;
    private DirectedEdge  m_eLoop;
    private DirectedGraph m_g1;
    private DirectedGraph m_g2;
    private DirectedGraph m_g3;
    private DirectedGraph m_g4;
    private EdgeFactory   m_eFactory;
    private String        m_v1 = "v1";
    private String        m_v2 = "v2";
    private String        m_v3 = "v3";
    private String        m_v4 = "v4";

    /**
     * @see junit.framework.TestCase#TestCase(java.lang.String)
     */
    public SimpleDirectedGraphTest( String name ) {
        super( name );
    }

    /**
     * Class to test for boolean addEdge(Edge)
     */
    public void testAddEdgeEdge(  ) {
        init(  );

        try {
            m_g1.addEdge( m_eLoop ); // loops not allowed
            assertFalse(  );
        }
         catch( IllegalArgumentException e ) {
            assertTrue(  );
        }

        try {
            m_g3.addEdge( null );
            assertFalse(  ); // NPE
        }
         catch( NullPointerException e ) {
            assertTrue(  );
        }

        Edge e = m_eFactory.createEdge( m_v2, m_v1 );

        try {
            m_g1.addEdge( e ); // no such vertex in graph
            assertFalse(  );
        }
         catch( IllegalArgumentException ile ) {
            assertTrue(  );
        }

        assertEquals( false, m_g2.addEdge( e ) );
        assertEquals( false, m_g3.addEdge( e ) );
        assertEquals( true, m_g4.addEdge( e ) );
    }


    /**
     * Class to test for Edge addEdge(Object, Object)
     */
    public void testAddEdgeObjectObject(  ) {
        init(  );

        try {
            m_g1.addEdge( m_v1, m_v1 ); // loops not allowed
            assertFalse(  );
        }
         catch( IllegalArgumentException e ) {
            assertTrue(  );
        }

        try {
            m_g3.addEdge( null, null );
            assertFalse(  ); // NPE
        }
         catch( NullPointerException e ) {
            assertTrue(  );
        }

        try {
            m_g1.addEdge( m_v2, m_v1 ); // no such vertex in graph
            assertFalse(  );
        }
         catch( IllegalArgumentException ile ) {
            assertTrue(  );
        }

        assertNull( m_g2.addEdge( m_v2, m_v1 ) );
        assertNull( m_g3.addEdge( m_v2, m_v1 ) );
        assertNotNull( m_g4.addEdge( m_v2, m_v1 ) );
    }


    /**
     * .
     */
    public void testAddVertex(  ) {
        init(  );

        assertEquals( 1, m_g1.vertexSet(  ).size(  ) );
        assertEquals( 2, m_g2.vertexSet(  ).size(  ) );
        assertEquals( 3, m_g3.vertexSet(  ).size(  ) );
        assertEquals( 4, m_g4.vertexSet(  ).size(  ) );

        assertFalse( m_g1.addVertex( m_v1 ) );
        assertTrue( m_g1.addVertex( m_v2 ) );
    }


    /**
     * Class to test for boolean containsEdge(Edge)
     */
    public void testContainsEdgeEdge(  ) {
        init(  );

        //TODO Implement containsEdge().
    }


    /**
     * Class to test for boolean containsEdge(Object, Object)
     */
    public void testContainsEdgeObjectObject(  ) {
        init(  );

        assertFalse( m_g1.containsEdge( m_v1, m_v2 ) );
        assertFalse( m_g1.containsEdge( m_v1, m_v1 ) );

        assertTrue( m_g2.containsEdge( m_v1, m_v2 ) );
        assertTrue( m_g2.containsEdge( m_v2, m_v1 ) );

        assertTrue( m_g3.containsEdge( m_v1, m_v2 ) );
        assertTrue( m_g3.containsEdge( m_v2, m_v1 ) );
        assertTrue( m_g3.containsEdge( m_v3, m_v2 ) );
        assertTrue( m_g3.containsEdge( m_v2, m_v3 ) );
        assertTrue( m_g3.containsEdge( m_v1, m_v3 ) );
        assertTrue( m_g3.containsEdge( m_v3, m_v1 ) );

        assertFalse( m_g4.containsEdge( m_v1, m_v4 ) );
        m_g4.addEdge( m_v1, m_v4 );
        assertTrue( m_g4.containsEdge( m_v1, m_v4 ) );

        assertFalse( m_g3.containsEdge( m_v4, m_v2 ) );
        assertFalse( m_g3.containsEdge( null, null ) );
    }


    /**
     * .
     */
    public void testContainsVertex(  ) {
        init(  );

        //TODO Implement containsVertex().
    }


    /**
     * .
     */
    public void testEdgeSet(  ) {
        init(  );

        //TODO Implement edgeSet().
    }


    /**
     * .
     */
    public void testEdgesOf(  ) {
        init(  ); //TODO Implement edgesOf().
    }


    /**
     * .
     */
    public void testGetAllEdges(  ) {
        init(  ); //TODO Implement getAllEdges().
    }


    /**
     * .
     */
    public void testGetEdge(  ) {
        init(  ); //TODO Implement getEdge().
    }


    /**
     * .
     */
    public void testGetEdgeFactory(  ) {
        init(  ); //TODO Implement getEdgeFactory().
    }


    /**
     * .
     */
    public void testInDegreeOf(  ) {
        init(  );

        assertEquals( 0, m_g1.inDegreeOf( m_v1 ) );

        assertEquals( 1, m_g2.inDegreeOf( m_v1 ) );
        assertEquals( 1, m_g2.inDegreeOf( m_v2 ) );

        assertEquals( 2, m_g3.inDegreeOf( m_v1 ) );
        assertEquals( 2, m_g3.inDegreeOf( m_v2 ) );
        assertEquals( 2, m_g3.inDegreeOf( m_v3 ) );

        assertEquals( 1, m_g4.inDegreeOf( m_v1 ) );
        assertEquals( 1, m_g4.inDegreeOf( m_v2 ) );
        assertEquals( 1, m_g4.inDegreeOf( m_v3 ) );
        assertEquals( 1, m_g4.inDegreeOf( m_v4 ) );

        try {
            m_g3.inDegreeOf( new Object(  ) );
            assertFalse(  );
        }
         catch( IllegalArgumentException e ) {
            assertTrue(  );
        }

        try {
            m_g3.inDegreeOf( null );
            assertFalse(  );
        }
         catch( NullPointerException e ) {
            assertTrue(  );
        }
    }


    /**
     * .
     */
    public void testIncomingEdgesOf(  ) {
        init(  ); //TODO Implement incomingEdgesOf().
    }


    /**
     * .
     */
    public void testOutDegreeOf(  ) {
        init(  ); //TODO Implement outDegreeOf().
    }


    /**
     * .
     */
    public void testOutgoingEdgesOf(  ) {
        init(  ); //TODO Implement outgoingEdgesOf().
    }


    /**
     * Class to test for boolean removeEdge(Edge)
     */
    public void testRemoveEdgeEdge(  ) {
        init(  ); //TODO Implement removeEdge().
    }


    /**
     * Class to test for Edge removeEdge(Object, Object)
     */
    public void testRemoveEdgeObjectObject(  ) {
        init(  ); //TODO Implement removeEdge().
    }


    /**
     * .
     */
    public void testRemoveVertex(  ) {
        init(  ); //TODO Implement removeVertex().
    }


    /**
     * .
     */
    public void testVertexSet(  ) {
        init(  ); //TODO Implement vertexSet().
    }


    private void init(  ) {
        m_gEmpty     = new SimpleDirectedGraph(  );
        m_g1         = new SimpleDirectedGraph(  );
        m_g2         = new SimpleDirectedGraph(  );
        m_g3         = new SimpleDirectedGraph(  );
        m_g4         = new SimpleDirectedGraph(  );

        m_eFactory     = m_g1.getEdgeFactory(  );
        m_eLoop        = (DirectedEdge) m_eFactory.createEdge( m_v1, m_v1 );

        m_g1.addVertex( m_v1 );

        m_g2.addVertex( m_v1 );
        m_g2.addVertex( m_v2 );
        m_g2.addEdge( m_v1, m_v2 );
        m_g2.addEdge( m_v2, m_v1 );

        m_g3.addVertex( m_v1 );
        m_g3.addVertex( m_v2 );
        m_g3.addVertex( m_v3 );
        m_g3.addEdge( m_v1, m_v2 );
        m_g3.addEdge( m_v2, m_v1 );
        m_g3.addEdge( m_v2, m_v3 );
        m_g3.addEdge( m_v3, m_v2 );
        m_g3.addEdge( m_v3, m_v1 );
        m_g3.addEdge( m_v1, m_v3 );

        m_g4.addVertex( m_v1 );
        m_g4.addVertex( m_v2 );
        m_g4.addVertex( m_v3 );
        m_g4.addVertex( m_v4 );
        m_g4.addEdge( m_v1, m_v2 );
        m_g4.addEdge( m_v2, m_v3 );
        m_g4.addEdge( m_v3, m_v4 );
        m_g4.addEdge( m_v4, m_v1 );
    }
}
