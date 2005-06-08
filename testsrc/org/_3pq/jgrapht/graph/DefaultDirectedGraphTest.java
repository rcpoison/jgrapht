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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org._3pq.jgrapht.DirectedGraph;
import org._3pq.jgrapht.Edge;
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
    public void testEdgeListFactory(  ) {
        DirectedMultigraph g = new DirectedMultigraph(  );
        g.setEdgeListFactory( new LinkedListFactory(  ) );
        initMultiTriangleWithMultiLoop( g );
    }


    /**
     * .
     */
    public void testEdgeOrderDeterminism(  ) {
        DirectedGraph g = new DirectedMultigraph(  );
        g.addVertex( m_v1 );
        g.addVertex( m_v2 );
        g.addVertex( m_v3 );

        Edge     e1 = g.addEdge( m_v1, m_v2 );
        Edge     e2 = g.addEdge( m_v2, m_v3 );
        Edge     e3 = g.addEdge( m_v3, m_v1 );

        Iterator iter = g.edgeSet(  ).iterator(  );
        assertEquals( e1, iter.next(  ) );
        assertEquals( e2, iter.next(  ) );
        assertEquals( e3, iter.next(  ) );
    }


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


    /**
     * .
     */
    public void testVertexOrderDeterminism(  ) {
        DirectedGraph g    = createMultiTriangleWithMultiLoop(  );
        Iterator      iter = g.vertexSet(  ).iterator(  );
        assertEquals( m_v1, iter.next(  ) );
        assertEquals( m_v2, iter.next(  ) );
        assertEquals( m_v3, iter.next(  ) );
    }


    private DirectedGraph createMultiTriangleWithMultiLoop(  ) {
        DirectedGraph g = new DirectedMultigraph(  );
        initMultiTriangleWithMultiLoop( g );

        return g;
    }


    private void initMultiTriangleWithMultiLoop( DirectedGraph g ) {
        g.addVertex( m_v1 );
        g.addVertex( m_v2 );
        g.addVertex( m_v3 );

        g.addEdge( m_v1, m_v1 );
        g.addEdge( m_v1, m_v2 );
        g.addEdge( m_v2, m_v3 );
        g.addEdge( m_v3, m_v1 );
    }

    private static class LinkedListFactory implements EdgeListFactory {
        /**
         * .
         *
         * @param vertex
         *
         * @return
         */
        public List createEdgeList( Object vertex ) {
            return new LinkedList(  );
        }
    }
}
