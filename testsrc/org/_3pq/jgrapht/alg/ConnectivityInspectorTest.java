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
/* ------------------------------
 * ConnectivityInspectorTest.java
 * ------------------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 07-Aug-2003 : Initial revision (BN);
 *
 */
package org._3pq.jgrapht.alg;

import junit.framework.TestCase;

import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.graph.ListenableDirectedGraph;
import org._3pq.jgrapht.graph.Pseudograph;

/**
 * .
 *
 * @author Barak Naveh
 */
public class ConnectivityInspectorTest extends TestCase {
    private static final String V1 = new String( "v1" );
    private static final String V2 = new String( "v2" );
    private static final String V3 = new String( "v3" );
    private static final String V4 = new String( "v4" );

    //
    Edge m_e1;
    Edge m_e2;
    Edge m_e3;
    Edge m_e3_b;
    Edge m_u;

    /**
     * .
     *
     * @return
     */
    public Pseudograph create(  ) {
        Pseudograph g = new Pseudograph(  );

        assertEquals( 0, g.vertexSet(  ).size(  ) );
        g.addVertex( V1 );
        assertEquals( 1, g.vertexSet(  ).size(  ) );
        g.addVertex( V2 );
        assertEquals( 2, g.vertexSet(  ).size(  ) );
        g.addVertex( V3 );
        assertEquals( 3, g.vertexSet(  ).size(  ) );
        g.addVertex( V4 );
        assertEquals( 4, g.vertexSet(  ).size(  ) );

        assertEquals( 0, g.edgeSet(  ).size(  ) );

        m_e1 = g.addEdge( V1, V2 );
        assertEquals( 1, g.edgeSet(  ).size(  ) );

        m_e2 = g.addEdge( V2, V3 );
        assertEquals( 2, g.edgeSet(  ).size(  ) );

        m_e3 = g.addEdge( V3, V1 );
        assertEquals( 3, g.edgeSet(  ).size(  ) );

        m_e3_b = g.addEdge( V3, V1 );
        assertEquals( 4, g.edgeSet(  ).size(  ) );
        assertNotNull( m_e3_b );

        m_u = g.addEdge( V1, V1 );
        assertEquals( 5, g.edgeSet(  ).size(  ) );
        m_u = g.addEdge( V1, V1 );
        assertEquals( 6, g.edgeSet(  ).size(  ) );

        return g;
    }


    /**
     * .
     */
    public void testDirectedGraph(  ) {
        ListenableDirectedGraph g = new ListenableDirectedGraph(  );
        g.addVertex( V1 );
        g.addVertex( V2 );
        g.addVertex( V3 );

        g.addEdge( V1, V2 );

        ConnectivityInspector inspector = new ConnectivityInspector( g );
        g.addGraphListener( inspector );

        assertEquals( false, inspector.isGraphConnected(  ) );

        g.addEdge( V1, V3 );

        assertEquals( true, inspector.isGraphConnected(  ) );
    }


    /**
     * .
     */
    public void testIsGraphConnected(  ) {
        Pseudograph           g         = create(  );
        ConnectivityInspector inspector = new ConnectivityInspector( g );

        assertEquals( false, inspector.isGraphConnected(  ) );

        g.removeVertex( V4 );
        inspector = new ConnectivityInspector( g );
        assertEquals( true, inspector.isGraphConnected(  ) );

        g.removeVertex( V1 );
        assertEquals( 1, g.edgeSet(  ).size(  ) );

        g.removeEdge( m_e2 );
        g.addEdge( V2, V2 );
        assertEquals( 1, g.edgeSet(  ).size(  ) );

        inspector = new ConnectivityInspector( g );
        assertEquals( false, inspector.isGraphConnected(  ) );
    }
}
