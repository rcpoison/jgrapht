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
 * along with this library; if not, write to the Free Software Foundation,
 * Inc.,
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
 * $Id: DefaultDirectedGraphTest.java,v 1.5 2005/06/08 22:24:57 perfecthash Exp
 * $
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
        DirectedMultigraph<String, DirEdge<String>> g = new DirectedMultigraph<String, DirEdge<String>>();
        g.setEdgeSetFactory(new LinkedHashSetFactory<String, DirEdge<String>>());
        initMultiTriangleWithMultiLoop(g);
    }

    /**
     * .
     */
    public void testEdgeOrderDeterminism()
    {
        DirectedGraph<String, DirEdge<String>> g = new DirectedMultigraph<String, DirEdge<String>>();
        g.addVertex(m_v1);
        g.addVertex(m_v2);
        g.addVertex(m_v3);

        Edge e1 = g.addEdge(m_v1, m_v2);
        Edge e2 = g.addEdge(m_v2, m_v3);
        Edge e3 = g.addEdge(m_v3, m_v1);

        Iterator<DirEdge<String>> iter = g.edgeSet().iterator();
        assertEquals(e1, iter.next());
        assertEquals(e2, iter.next());
        assertEquals(e3, iter.next());
    }

    /**
     * .
     */
    public void testEdgesOf()
    {
        DirectedGraph<String, DirEdge<String>> g = createMultiTriangleWithMultiLoop();

        assertEquals(3, g.edgesOf(m_v1).size());
        assertEquals(2, g.edgesOf(m_v2).size());
    }

    /**
     * .
     */
    public void testGetAllEdges()
    {
        DirectedGraph<String, DirEdge<String>> g = createMultiTriangleWithMultiLoop();

        Set<DirEdge<String>> loops = g.getAllEdges(m_v1, m_v1);
        assertEquals(1, loops.size());
    }

    /**
     * .
     */
    public void testInDegreeOf()
    {
        DirectedGraph<String, DirEdge<String>> g = createMultiTriangleWithMultiLoop();

        assertEquals(2, g.inDegreeOf(m_v1));
        assertEquals(1, g.inDegreeOf(m_v2));
    }

    /**
     * .
     */
    public void testOutDegreeOf()
    {
        DirectedGraph<String, DirEdge<String>> g = createMultiTriangleWithMultiLoop();

        assertEquals(2, g.outDegreeOf(m_v1));
        assertEquals(1, g.outDegreeOf(m_v2));
    }

    /**
     * .
     */
    public void testVertexOrderDeterminism()
    {
        DirectedGraph<String, DirEdge<String>> g = createMultiTriangleWithMultiLoop();
        Iterator<String> iter = g.vertexSet().iterator();
        assertEquals(m_v1, iter.next());
        assertEquals(m_v2, iter.next());
        assertEquals(m_v3, iter.next());
    }

    private DirectedGraph<String, DirEdge<String>> createMultiTriangleWithMultiLoop()
    {
        DirectedGraph<String, DirEdge<String>> g = new DirectedMultigraph<String, DirEdge<String>>();
        initMultiTriangleWithMultiLoop(g);

        return g;
    }

    private void initMultiTriangleWithMultiLoop(DirectedGraph<String, DirEdge<String>> g)
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

    private static class LinkedHashSetFactory<V,E extends Edge<V>> implements EdgeSetFactory<V,E>
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
