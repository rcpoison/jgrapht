/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Creator:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
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
 * NeighborIndexTest.java
 * ------------------------------
 * (C) Copyright 2003-2006, by Barak Naveh and Contributors.
 *
 * Original Author:  Charles Fry
 *
 * $Id$
 *
 * Changes
 * -------
 * 12-Dec-2005 : Initial revision (CF);
 *
 */
package org.jgrapht.alg;

import java.util.*;

import junit.framework.*;

import org.jgrapht.graph.*;


/**
 * .
 *
 * @author Charles Fry
 */
public class NeighborIndexTest
    extends TestCase
{
    //~ Static fields/initializers ---------------------------------------------

    private static final String V1 = "v1";
    private static final String V2 = "v2";
    private static final String V3 = "v3";

    //~ Methods ----------------------------------------------------------------

    public void testNeighborSet()
    {
        ListenableUndirectedGraph<String, DefaultEdge> g =
            new ListenableUndirectedGraph<String, DefaultEdge>(
                DefaultEdge.class);
        g.addVertex(V1);
        g.addVertex(V2);

        g.addEdge(V1, V2);

        NeighborIndex<String, DefaultEdge> index =
            new NeighborIndex<String, DefaultEdge>(g);
        g.addGraphListener(index);

        Set neighbors = index.neighborsOf(V1);

        assertEquals(1, neighbors.size());
        assertEquals(true, neighbors.contains(V2));

        g.addVertex(V3);
        g.addEdge(V3, V1);

        assertEquals(2, neighbors.size());
        assertEquals(true, neighbors.contains(V3));

        g.removeEdge(V3, V1);

        assertEquals(1, neighbors.size());
        assertEquals(false, neighbors.contains(V3));

        g.removeVertex(V2);

        assertEquals(0, neighbors.size());
    }

    public void testDirectedNeighborSet()
    {
        ListenableDirectedGraph<String, DefaultEdge> g =
            new ListenableDirectedGraph<String, DefaultEdge>(
                DefaultEdge.class);
        g.addVertex(V1);
        g.addVertex(V2);

        g.addEdge(V1, V2);

        DirectedNeighborIndex<String, DefaultEdge> index =
            new DirectedNeighborIndex<String, DefaultEdge>(g);
        g.addGraphListener(index);

        Set p = index.predecessorsOf(V1);
        Set s = index.successorsOf(V1);

        assertEquals(0, p.size());
        assertEquals(1, s.size());
        assertEquals(true, s.contains(V2));

        g.addVertex(V3);
        g.addEdge(V3, V1);

        assertEquals(1, p.size());
        assertEquals(1, s.size());
        assertEquals(true, p.contains(V3));

        g.removeEdge(V3, V1);

        assertEquals(0, p.size());

        g.removeVertex(V2);

        assertEquals(0, s.size());
    }
}

// End NeighborIndexTest.java
