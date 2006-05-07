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
/* --------------------------
 * AsUndirectedGraphTest.java
 * --------------------------
 * (C) Copyright 2003, by John V. Sichi and Contributors.
 *
 * Original Author:  John V. Sichi
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 14-Aug-2003 : Initial revision (JVS);
 *
 */
package org.jgrapht.graph;

import java.util.*;

import org.jgrapht.*;


/**
 * A unit test for the AsDirectedGraph view.
 *
 * @author John V. Sichi
 */
public class AsUndirectedGraphTest extends EnhancedTestCase
{

    //~ Instance fields -------------------------------------------------------

    private DirectedGraph<String, DirEdge<String>> m_directed;
    private Edge<String> m_loop;
    private String m_v1 = "v1";
    private String m_v2 = "v2";
    private String m_v3 = "v3";
    private String m_v4 = "v4";
    private UndirectedGraph<String, Edge<String>> m_undirected;

    //~ Constructors ----------------------------------------------------------

    /**
     * @see junit.framework.TestCase#TestCase(java.lang.String)
     */
    public AsUndirectedGraphTest(String name)
    {
        super(name);
    }

    //~ Methods ---------------------------------------------------------------

    /**
     * .
     */
    public void testAddEdge()
    {
        try {
            m_undirected.addEdge(m_v3, m_v4);
            assertFalse();
        } catch (UnsupportedOperationException e) {
            assertTrue();
        }
    }

    /**
     * .
     */
    public void testAddVertex()
    {
        String v5 = "v5";

        m_undirected.addVertex(v5);
        assertEquals(true, m_undirected.containsVertex(v5));
        assertEquals(true, m_directed.containsVertex(v5));
    }

    /**
     * .
     */
    public void testDegreeOf()
    {
        assertEquals(1, m_undirected.degreeOf(m_v1));
        assertEquals(3, m_undirected.degreeOf(m_v2));
        assertEquals(1, m_undirected.degreeOf(m_v3));
        assertEquals(3, m_undirected.degreeOf(m_v4));
    }

    /**
     * .
     */
    public void testGetAllEdges()
    {
        Set<Edge<String>> edges = m_undirected.getAllEdges(m_v3, m_v2);
        assertEquals(1, edges.size());
        assertEquals(m_directed.getEdge(m_v2, m_v3),
            edges.iterator().next());

        edges = m_undirected.getAllEdges(m_v4, m_v4);
        assertEquals(1, edges.size());
        assertEquals(m_loop, edges.iterator().next());
    }

    /**
     * .
     */
    public void testGetEdge()
    {
        assertEquals(
            m_directed.getEdge(m_v1, m_v2),
            m_undirected.getEdge(m_v1, m_v2));
        assertEquals(
            m_directed.getEdge(m_v1, m_v2),
            m_undirected.getEdge(m_v2, m_v1));

        assertEquals(
            m_directed.getEdge(m_v4, m_v4),
            m_undirected.getEdge(m_v4, m_v4));
    }

    /**
     * .
     */
    public void testRemoveEdge()
    {
        m_undirected.removeEdge(m_loop);
        assertEquals(false, m_undirected.containsEdge(m_loop));
        assertEquals(false, m_directed.containsEdge(m_loop));
    }

    /**
     * .
     */
    public void testRemoveVertex()
    {
        m_undirected.removeVertex(m_v4);
        assertEquals(false, m_undirected.containsVertex(m_v4));
        assertEquals(false, m_directed.containsVertex(m_v4));
    }

    /**
     * .
     */
    protected void setUp()
    {
        m_directed = new DefaultDirectedGraph<String, DirEdge<String>>();
        m_undirected = new AsUndirectedGraph<String, Edge<String>>(m_directed);

        m_directed.addVertex(m_v1);
        m_directed.addVertex(m_v2);
        m_directed.addVertex(m_v3);
        m_directed.addVertex(m_v4);
        m_directed.addEdge(m_v1, m_v2);
        m_directed.addEdge(m_v2, m_v3);
        m_directed.addEdge(m_v2, m_v4);
        m_loop = m_directed.addEdge(m_v4, m_v4);
    }
}
