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
    public static final String m_v1 = new String( "v1" );
    public static final String m_v2 = new String( "v2" );
    public static final String m_v3 = new String( "v3" );
    public static final String m_v4 = new String( "v4" );

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

        assertEquals( g.vertexSet(  ).size(  ), 0 );
        g.addVertex( m_v1 );
        assertEquals( g.vertexSet(  ).size(  ), 1 );
        g.addVertex( m_v2 );
        assertEquals( g.vertexSet(  ).size(  ), 2 );
        g.addVertex( m_v3 );
        assertEquals( g.vertexSet(  ).size(  ), 3 );
        g.addVertex( m_v4 );
        assertEquals( g.vertexSet(  ).size(  ), 4 );

        assertEquals( g.edgeSet(  ).size(  ), 0 );

        m_e1 = g.addEdge( m_v1, m_v2 );
        assertEquals( g.edgeSet(  ).size(  ), 1 );

        m_e2 = g.addEdge( m_v2, m_v3 );
        assertEquals( g.edgeSet(  ).size(  ), 2 );

        m_e3 = g.addEdge( m_v3, m_v1 );
        assertEquals( g.edgeSet(  ).size(  ), 3 );

        m_e3_b = g.addEdge( m_v3, m_v1 );
        assertEquals( g.edgeSet(  ).size(  ), 4 );
        assertNotNull( m_e3_b );

        m_u = g.addEdge( m_v1, m_v1 );
        assertEquals( g.edgeSet(  ).size(  ), 5 );
        m_u = g.addEdge( m_v1, m_v1 );
        assertEquals( g.edgeSet(  ).size(  ), 6 );

        return g;
    }


    /**
     * .
     */
    public void testDirectedGraph(  ) {
        ListenableDirectedGraph g = new ListenableDirectedGraph(  );
        g.addVertex( m_v1 );
        g.addVertex( m_v2 );
        g.addVertex( m_v3 );

        g.addEdge( m_v1, m_v2 );

        ConnectivityInspector inspector = new ConnectivityInspector( g );
        g.addGraphListener( inspector );

        assertEquals( false, inspector.isGraphConnected(  ) );

        g.addEdge( m_v1, m_v3 );

        assertEquals( true, inspector.isGraphConnected(  ) );
    }


    /**
     * .
     */
    public void testIsGraphConnected(  ) {
        Pseudograph           g         = create(  );
        ConnectivityInspector inspector = new ConnectivityInspector( g );

        assertEquals( false, inspector.isGraphConnected(  ) );

        g.removeVertex( m_v4 );
        inspector = new ConnectivityInspector( g );
        assertEquals( true, inspector.isGraphConnected(  ) );

        g.removeVertex( m_v1 );
        assertEquals( g.edgeSet(  ).size(  ), 1 );

        g.removeEdge( m_e2 );
        g.addEdge( m_v2, m_v2 );
        assertEquals( g.edgeSet(  ).size(  ), 1 );

        inspector = new ConnectivityInspector( g );
        assertEquals( false, inspector.isGraphConnected(  ) );
    }
}
