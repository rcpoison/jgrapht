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
/* -----------------------------
 * DefaultDirectedGraphTest.java
 * -----------------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 09-Aug-2003 : Initial revision (BN);
 *
 */
package org._3pq.jgrapht.graph;

import java.util.List;

import org._3pq.jgrapht.DirectedGraph;
import org._3pq.jgrapht.EnhancedTestCase;

/**
 * A unit test for directed multigraph.
 *
 * @author Barak Naveh
 *
 * @since Aug 9, 2003
 */
public class DefaultDirectedGraphTest extends EnhancedTestCase {
    private String m_v1 = "v1";
    private String m_v2 = "v2";
    private String m_v3 = "v3";

    /**
     * .
     */
    public void testEdgesOf(  ) {
        DirectedGraph g = createMultiTriangleWithMultiLoop(  );

        assertEquals( 3, g.edgesOf( m_v1 ).size(  ) );
        assertEquals( 2, g.edgesOf( m_v2 ).size(  ) );
    }


    /**
     * .
     */
    public void testGetAllEdges(  ) {
        DirectedGraph g = createMultiTriangleWithMultiLoop(  );

        List          loops = g.getAllEdges( m_v1, m_v1 );
        assertEquals( 1, loops.size(  ) );
    }


    /**
     * .
     */
    public void testInDegreeOf(  ) {
        DirectedGraph g = createMultiTriangleWithMultiLoop(  );

        assertEquals( 2, g.inDegreeOf( m_v1 ) );
        assertEquals( 1, g.inDegreeOf( m_v2 ) );
    }


    /**
     * .
     */
    public void testOutDegreeOf(  ) {
        DirectedGraph g = createMultiTriangleWithMultiLoop(  );

        assertEquals( 2, g.outDegreeOf( m_v1 ) );
        assertEquals( 1, g.outDegreeOf( m_v2 ) );
    }


    private DirectedGraph createMultiTriangleWithMultiLoop(  ) {
        DirectedGraph g = new DirectedMultigraph(  );

        g.addVertex( m_v1 );
        g.addVertex( m_v2 );
        g.addVertex( m_v3 );

        g.addEdge( m_v1, m_v1 );
        g.addEdge( m_v1, m_v2 );
        g.addEdge( m_v2, m_v3 );
        g.addEdge( m_v3, m_v1 );

        return g;
    }
}
