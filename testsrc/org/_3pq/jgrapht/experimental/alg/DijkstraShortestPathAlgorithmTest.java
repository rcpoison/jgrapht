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
package org._3pq.jgrapht.experimental.alg;

import junit.framework.TestCase;

import org._3pq.jgrapht.*;
import org._3pq.jgrapht.graph.WeightedPseudograph;

/**
 * .
 *
 * @author Barak Naveh
 */
public class DijkstraShortestPathAlgorithmTest extends TestCase {
    public static final String m_v1 = new String( "v1" );
    public static final String m_v2 = new String( "v2" );
    public static final String m_v3 = new String( "v3" );
    public static final String m_v4 = new String( "v4" );

    //
    Edge m_e1;
    Edge m_e2;
    Edge m_e3;
    Edge m_e4;
    Edge m_u;

    /**
     * .
     *
     * @return
     */
    public WeightedGraph create() {
        WeightedGraph g = new WeightedPseudograph();

        g.addVertex(m_v1);
        g.addVertex(m_v2);
        g.addVertex(m_v3);
        g.addVertex(m_v4);
        m_e1 = GraphHelper.addEdge(g, m_v1, m_v2, 1);
        m_e2 = GraphHelper.addEdge(g, m_v2, m_v3, 2);
        m_e3 = GraphHelper.addEdge(g, m_v3, m_v1, 4);
        m_e4 = GraphHelper.addEdge(g, m_v3, m_v4, 10);
        m_u = GraphHelper.addEdge(g, m_v3, m_v3, 5);
        return g;
    }

    /**
     * .
     */
    public void testShortestPath() {
        WeightedGraph         g        = create();
        ShortestPathAlgorithm alg      = new DijkstraShortestPathAlgorithm(g);
        WeightedGraph         pathTree = alg.shortestPathTree(m_v1);

        assertEquals(4, pathTree.vertexSet().size());
        assertEquals(3, pathTree.edgeSet().size());
        assertTrue(pathTree.containsEdge(m_e1));
        assertTrue(pathTree.containsEdge(m_e2));
        assertTrue(pathTree.containsEdge(m_e4));
    }
}
