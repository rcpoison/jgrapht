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
/* --------------------------------------
 * DijkstraShortestPathAlgorithmTest.java
 * --------------------------------------
 * (C) Copyright 2003, by Michael Behrisch and Contributors.
 *
 * Original Author:  Michael Behrisch
 * Contributor(s):   -
 *
 * $Id: DijkstraShortestPathAlgorithmTest.java,v 1.4 2004/05/01 12:21:54
 * barak_naveh Exp $
 *
 * Changes
 * -------
 * 06-Sep-2003 : Initial revision (MB);
 *
 */
package org._3pq.jgrapht.experimental.alg;

import junit.framework.*;

import org._3pq.jgrapht.*;
import org._3pq.jgrapht.graph.*;


/**
 * .
 *
 * @author Barak Naveh
 */
public class DijkstraShortestPathAlgorithmTest extends TestCase
{

    //~ Static fields/initializers --------------------------------------------

    private static final String V1 = new String("v1");
    private static final String V2 = new String("v2");
    private static final String V3 = new String("v3");
    private static final String V4 = new String("v4");

    //~ Instance fields -------------------------------------------------------

    //
    Edge m_e1;
    Edge m_e2;
    Edge m_e3;
    Edge m_e4;
    Edge m_u;

    //~ Methods ---------------------------------------------------------------

    /**
     * .
     */
    public void testShortestPath()
    {
        WeightedGraph g = create();
        ShortestPathAlgorithm alg = new DijkstraShortestPathAlgorithm(g);
        WeightedGraph pathTree = alg.shortestPathTree(V1);

        assertEquals(4, pathTree.vertexSet().size());
        assertEquals(3, pathTree.edgeSet().size());
        assertTrue(pathTree.containsEdge(m_e1));
        assertTrue(pathTree.containsEdge(m_e2));
        assertTrue(pathTree.containsEdge(m_e4));
    }

    private WeightedGraph create()
    {
        WeightedGraph g = new WeightedPseudograph();

        g.addVertex(V1);
        g.addVertex(V2);
        g.addVertex(V3);
        g.addVertex(V4);
        m_e1 = GraphHelper.addEdge(g, V1, V2, 1);
        m_e2 = GraphHelper.addEdge(g, V2, V3, 2);
        m_e3 = GraphHelper.addEdge(g, V3, V1, 4);
        m_e4 = GraphHelper.addEdge(g, V3, V4, 10);
        m_u = GraphHelper.addEdge(g, V3, V3, 5);

        return g;
    }
}
