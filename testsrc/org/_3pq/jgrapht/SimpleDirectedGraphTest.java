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
package org._3pq.jgrapht;

import org._3pq.jgrapht.DirectedEdge;
import org._3pq.jgrapht.DirectedGraph;
import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.EdgeFactory;
import org._3pq.jgrapht.GraphFactory;

/**
 * .
 *
 * @author Barak Naveh
 *
 * @since Jul 25, 2003
 */
public class SimpleDirectedGraphTest extends EnhancedTestCase {
    DirectedEdge  e1_1;
    DirectedEdge  eLoop;
    DirectedGraph g1;
    DirectedGraph g2;
    DirectedGraph g3;
    DirectedGraph g4;
    DirectedGraph gEmpty;
    EdgeFactory   eFactory;
    String        v1 = "v1";
    String        v2 = "v2";
    String        v3 = "v3";
    String        v4 = "v4";

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
            g1.addEdge( eLoop ); // loops not allowed
            assertFalse(  );
        }
         catch( IllegalArgumentException e ) {
            assertTrue(  );
        }

        try {
            g3.addEdge( null );
            assertFalse(  ); // NPE
        }
         catch( NullPointerException e ) {
            assertTrue(  );
        }

        Edge e = eFactory.createEdge( v2, v1 );

        try {
            g1.addEdge( e ); // no such vertex in graph
            assertFalse(  );
        }
         catch( IllegalArgumentException ile ) {
            assertTrue(  );
        }

        assertEquals( false, g2.addEdge( e ) );
        assertEquals( false, g3.addEdge( e ) );
        assertEquals( true, g4.addEdge( e ) );
    }


    /**
     * Class to test for Edge addEdge(Object, Object)
     */
    public void testAddEdgeObjectObject(  ) {
        init(  );

        try {
            g1.addEdge( v1, v1 ); // loops not allowed
            assertFalse(  );
        }
         catch( IllegalArgumentException e ) {
            assertTrue(  );
        }

        try {
            g3.addEdge( null, null );
            assertFalse(  ); // NPE
        }
         catch( NullPointerException e ) {
            assertTrue(  );
        }

        try {
            g1.addEdge( v2, v1 ); // no such vertex in graph
            assertFalse(  );
        }
         catch( IllegalArgumentException ile ) {
            assertTrue(  );
        }

        assertNull( g2.addEdge( v2, v1 ) );
        assertNull( g3.addEdge( v2, v1 ) );
        assertNotNull( g4.addEdge( v2, v1 ) );
    }


    /**
     * .
     */
    public void testAddVertex(  ) {
        init(  );

        assertEquals( 1, g1.vertexSet(  ).size(  ) );
        assertEquals( 2, g2.vertexSet(  ).size(  ) );
        assertEquals( 3, g3.vertexSet(  ).size(  ) );
        assertEquals( 4, g4.vertexSet(  ).size(  ) );

        assertFalse( g1.addVertex( v1 ) );
        assertTrue( g1.addVertex( v2 ) );
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

        assertFalse( g1.containsEdge( v1, v2 ) );
        assertFalse( g1.containsEdge( v1, v1 ) );

        assertTrue( g2.containsEdge( v1, v2 ) );
        assertTrue( g2.containsEdge( v2, v1 ) );

        assertTrue( g3.containsEdge( v1, v2 ) );
        assertTrue( g3.containsEdge( v2, v1 ) );
        assertTrue( g3.containsEdge( v3, v2 ) );
        assertTrue( g3.containsEdge( v2, v3 ) );
        assertTrue( g3.containsEdge( v1, v3 ) );
        assertTrue( g3.containsEdge( v3, v1 ) );

        assertFalse( g4.containsEdge( v1, v4 ) );
        g4.addEdge( v1, v4 );
        assertTrue( g4.containsEdge( v1, v4 ) );

        assertFalse( g3.containsEdge( v4, v2 ) );
        assertFalse( g3.containsEdge( null, null ) );
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

        assertEquals( 0, g1.inDegreeOf( v1 ) );

        assertEquals( 1, g2.inDegreeOf( v1 ) );
        assertEquals( 1, g2.inDegreeOf( v2 ) );

        assertEquals( 2, g3.inDegreeOf( v1 ) );
        assertEquals( 2, g3.inDegreeOf( v2 ) );
        assertEquals( 2, g3.inDegreeOf( v3 ) );

        assertEquals( 1, g4.inDegreeOf( v1 ) );
        assertEquals( 1, g4.inDegreeOf( v2 ) );
        assertEquals( 1, g4.inDegreeOf( v3 ) );
        assertEquals( 1, g4.inDegreeOf( v4 ) );

        try {
            g3.inDegreeOf( new Object(  ) );
            assertFalse(  );
        }
         catch( IllegalArgumentException e ) {
            assertTrue(  );
        }

        try {
            g3.inDegreeOf( null );
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
        GraphFactory gf = GraphFactory.getFactory();
        gEmpty     = gf.createSimpleDirectedGraph(  );
        g1         = gf.createSimpleDirectedGraph(  );
        g2         = gf.createSimpleDirectedGraph(  );
        g3         = gf.createSimpleDirectedGraph(  );
        g4         = gf.createSimpleDirectedGraph(  );

        eFactory     = g1.getEdgeFactory(  );
        eLoop        = (DirectedEdge) eFactory.createEdge( v1, v1 );

        g1.addVertex( v1 );

        g2.addVertex( v1 );
        g2.addVertex( v2 );
        g2.addEdge( v1, v2 );
        g2.addEdge( v2, v1 );

        g3.addVertex( v1 );
        g3.addVertex( v2 );
        g3.addVertex( v3 );
        g3.addEdge( v1, v2 );
        g3.addEdge( v2, v1 );
        g3.addEdge( v2, v3 );
        g3.addEdge( v3, v2 );
        g3.addEdge( v3, v1 );
        g3.addEdge( v1, v3 );

        g4.addVertex( v1 );
        g4.addVertex( v2 );
        g4.addVertex( v3 );
        g4.addVertex( v4 );
        g4.addEdge( v1, v2 );
        g4.addEdge( v2, v3 );
        g4.addEdge( v3, v4 );
        g4.addEdge( v4, v1 );
    }
}
