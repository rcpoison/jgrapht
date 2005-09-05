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
/* ------------------------------
 * CycleDetectorTest.java
 * ------------------------------
 * (C) Copyright 2003, by John V. Sichi and Contributors.
 *
 * Original Author:  John V. Sichi
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 16-Sept-2004 : Initial revision (JVS);
 *
 */
package org._3pq.jgrapht.alg;

import java.util.*;

import junit.framework.*;

import org._3pq.jgrapht.*;
import org._3pq.jgrapht.graph.*;


/**
 * .
 *
 * @author John V. Sichi
 */
public class CycleDetectorTest extends TestCase
{

    //~ Static fields/initializers --------------------------------------------

    private static final String V1 = "v1";
    private static final String V2 = "v2";
    private static final String V3 = "v3";
    private static final String V4 = "v4";
    private static final String V5 = "v5";
    private static final String V6 = "v6";

    //~ Methods ---------------------------------------------------------------

    /**
     * .
     *
     * @param g
     */
    public void createGraph(Graph g)
    {
        g.addVertex(V1);
        g.addVertex(V2);
        g.addVertex(V3);
        g.addVertex(V4);
        g.addVertex(V5);
        g.addVertex(V6);

        g.addEdge(V1, V2);
        g.addEdge(V2, V3);
        g.addEdge(V3, V4);
        g.addEdge(V4, V1);
        g.addEdge(V4, V5);
        g.addEdge(V5, V6);
        g.addEdge(V1, V6);
    }

    /**
     * .
     */
    public void testDirectedWithCycle()
    {
        DirectedGraph g = new DefaultDirectedGraph();
        createGraph(g);

        Set cyclicSet = new HashSet();
        cyclicSet.add(V1);
        cyclicSet.add(V2);
        cyclicSet.add(V3);
        cyclicSet.add(V4);

        Set acyclicSet = new HashSet();
        acyclicSet.add(V5);
        acyclicSet.add(V6);

        runTest(g, cyclicSet, acyclicSet);
    }

    /**
     * .
     */
    public void testDirectedWithoutCycle()
    {
        DirectedGraph g = new DefaultDirectedGraph();
        createGraph(g);
        g.removeVertex(V2);

        Set cyclicSet = Collections.EMPTY_SET;
        Set acyclicSet = g.vertexSet();

        runTest(g, cyclicSet, acyclicSet);
    }

    private void runTest(DirectedGraph g, Set cyclicSet, Set acyclicSet)
    {
        CycleDetector detector = new CycleDetector(g);

        Set emptySet = Collections.EMPTY_SET;

        assertEquals(!cyclicSet.isEmpty(), detector.detectCycles());

        assertEquals(cyclicSet, detector.findCycles());

        Iterator iter = cyclicSet.iterator();

        while (iter.hasNext()) {
            Object v = iter.next();
            assertEquals(true, detector.detectCyclesContainingVertex(v));
            assertEquals(cyclicSet, detector.findCyclesContainingVertex(v));
        }

        iter = acyclicSet.iterator();

        while (iter.hasNext()) {
            Object v = iter.next();
            assertEquals(false, detector.detectCyclesContainingVertex(v));
            assertEquals(emptySet, detector.findCyclesContainingVertex(v));
        }
    }
}
