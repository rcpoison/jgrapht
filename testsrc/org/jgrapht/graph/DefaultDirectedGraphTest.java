/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Lead:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2006, by Barak Naveh and Contributors.
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
 * along with this library; if not, write to the Free Software Foundation,
 * Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */
/* -----------------------------
 * DefaultDirectedGraphTest.java
 * -----------------------------
 * (C) Copyright 2003-2006, by Barak Naveh and Contributors.
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
package org.jgrapht.graph;

import java.util.*;

import org.jgrapht.*;


/**
 * A unit test for directed multigraph.
 *
 * @author Barak Naveh
 * @since Aug 9, 2003
 */
public class DefaultDirectedGraphTest extends EnhancedTestCase
{

    //~ Instance fields -------------------------------------------------------

    private String m_v1 = "v1";
    private String m_v2 = "v2";
    private String m_v3 = "v3";

    //~ Methods ---------------------------------------------------------------

    /**
     * .
     */
    public void testEdgeSetFactory()
    {
        DirectedMultigraph<String, DefaultEdge> g =
            new DirectedMultigraph<String, DefaultEdge>(
                DefaultEdge.class);
        g.setEdgeSetFactory(new LinkedHashSetFactory<String, DefaultEdge>());
        initMultiTriangleWithMultiLoop(g);
    }

    /**
     * .
     */
    public void testEdgeOrderDeterminism()
    {
        DirectedGraph<String, DefaultEdge> g =
            new DirectedMultigraph<String, DefaultEdge>(
                DefaultEdge.class);
        g.addVertex(m_v1);
        g.addVertex(m_v2);
        g.addVertex(m_v3);

        DefaultEdge e1 = g.addEdge(m_v1, m_v2);
        DefaultEdge e2 = g.addEdge(m_v2, m_v3);
        DefaultEdge e3 = g.addEdge(m_v3, m_v1);

        Iterator<DefaultEdge> iter = g.edgeSet().iterator();
        assertEquals(e1, iter.next());
        assertEquals(e2, iter.next());
        assertEquals(e3, iter.next());

        // some bonus tests
        assertTrue(Graphs.testIncidence(g, e1, m_v1));
        assertTrue(Graphs.testIncidence(g, e1, m_v2));
        assertFalse(Graphs.testIncidence(g, e1, m_v3));
        assertEquals(m_v2, Graphs.getOppositeVertex(g, e1, m_v1));
        assertEquals(m_v1, Graphs.getOppositeVertex(g, e1, m_v2));

        assertEquals(
            "([v1, v2, v3], [(v1,v2), (v2,v3), (v3,v1)])",
            g.toString());
    }

    /**
     * .
     */
    public void testEdgesOf()
    {
        DirectedGraph<String, DefaultEdge> g = createMultiTriangleWithMultiLoop();

        assertEquals(3, g.edgesOf(m_v1).size());
        assertEquals(2, g.edgesOf(m_v2).size());
    }

    /**
     * .
     */
    public void testGetAllEdges()
    {
        DirectedGraph<String, DefaultEdge> g = createMultiTriangleWithMultiLoop();

        Set<DefaultEdge> loops = g.getAllEdges(m_v1, m_v1);
        assertEquals(1, loops.size());
    }

    /**
     * .
     */
    public void testInDegreeOf()
    {
        DirectedGraph<String, DefaultEdge> g = createMultiTriangleWithMultiLoop();

        assertEquals(2, g.inDegreeOf(m_v1));
        assertEquals(1, g.inDegreeOf(m_v2));
    }

    /**
     * .
     */
    public void testOutDegreeOf()
    {
        DirectedGraph<String, DefaultEdge> g = createMultiTriangleWithMultiLoop();

        assertEquals(2, g.outDegreeOf(m_v1));
        assertEquals(1, g.outDegreeOf(m_v2));
    }

    /**
     * .
     */
    public void testVertexOrderDeterminism()
    {
        DirectedGraph<String, DefaultEdge> g = createMultiTriangleWithMultiLoop();
        Iterator<String> iter = g.vertexSet().iterator();
        assertEquals(m_v1, iter.next());
        assertEquals(m_v2, iter.next());
        assertEquals(m_v3, iter.next());
    }

    private DirectedGraph<String, DefaultEdge> createMultiTriangleWithMultiLoop()
    {
        DirectedGraph<String, DefaultEdge> g =
            new DirectedMultigraph<String, DefaultEdge>(
                DefaultEdge.class);
        initMultiTriangleWithMultiLoop(g);

        return g;
    }

    private void initMultiTriangleWithMultiLoop(DirectedGraph<String, DefaultEdge> g)
    {
        g.addVertex(m_v1);
        g.addVertex(m_v2);
        g.addVertex(m_v3);

        g.addEdge(m_v1, m_v1);
        g.addEdge(m_v1, m_v2);
        g.addEdge(m_v2, m_v3);
        g.addEdge(m_v3, m_v1);
    }

    //~ Inner Classes ---------------------------------------------------------

    private static class LinkedHashSetFactory<V,E> implements EdgeSetFactory<V,E>
    {
        /**
         * .
         *
         * @param vertex
         *
         * @return an empty list.
         */
        public Set<E> createEdgeSet(V vertex)
        {
            return new LinkedHashSet<E>();
        }
    }
}
