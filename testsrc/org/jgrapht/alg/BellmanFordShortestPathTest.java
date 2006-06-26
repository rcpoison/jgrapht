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
/* ------------------------------
 * BellmanFordShortestPathTest.java
 * ------------------------------
 * (C) Copyright 2006, by John V. Sichi and Contributors.
 *
 * Original Author:  John V. Sichi
 * Contributor(s):   -
 *
 * $Id: DijkstraShortestPathTest.java,v 1.4 2005/05/30 05:37:29 perfecthash Exp
 * $
 *
 * Changes
 * -------
 * 14-Jan-2006 : Initial revision (JVS);
 *
 */
package org.jgrapht.alg;

import java.util.*;

import org.jgrapht.*;
import org.jgrapht.graph.*;

/**
 * .
 *
 * @author John V. Sichi
 */
public class BellmanFordShortestPathTest extends ShortestPathTestCase
{
    /**
     * .
     */
    public void testConstructor()
    {
        BellmanFordShortestPath path;
        Graph g = create();

        path = new BellmanFordShortestPath(g, V3);

        // find best path with no constraint on number of hops
        assertEquals(
            Arrays.asList(new DefaultEdge [] {
                m_e13, m_e12, m_e24, m_e45
            }),
            path.getPathEdgeList(V5));
        assertEquals(15.0, path.getCost(V5), 0);

        // find best path within 2 hops (less than optimal)
        path = new BellmanFordShortestPath(g, V3, 2);
        assertEquals(
            Arrays.asList(new DefaultEdge [] {
                m_e34, m_e45
            }),
            path.getPathEdgeList(V5));
        assertEquals(25.0, path.getCost(V5), 0);

        // find best path within 1 hop (doesn't exist!)
        path = new BellmanFordShortestPath(g, V3, 1);
        assertNull(path.getPathEdgeList(V5));
    }

    protected List findPathBetween(Graph g, String src, String dest)
    {
        return BellmanFordShortestPath.findPathBetween(g, src, dest);
    }

    public void testWithNegativeEdges()
    {
        Graph g = createWithBias(true);

        List path;
        
        path = findPathBetween(g, V1, V4);
        assertEquals(Arrays.asList(new DefaultEdge [] { m_e13, m_e34 }), path);
        
        path = findPathBetween(g, V1, V5);
        assertEquals(Arrays.asList(new DefaultEdge [] { m_e15 }), path);
    }
}

// End BellmanFordShortestPathTest.java
