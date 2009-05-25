/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Creator:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2008, by Barak Naveh and Contributors.
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
/* -------------------------
 * KShortestPathCostTest.java
 * -------------------------
 * (C) Copyright 2007-2008, by France Telecom
 *
 * Original Author:  Guillaume Boulmier and Contributors.
 *
 * $Id$
 *
 * Changes
 * -------
 * 05-Jun-2007 : Initial revision (GB);
 *
 */
package org.jgrapht.alg;

import java.util.*;

import junit.framework.*;

import org.jgrapht.*;


/**
 * @author Guillaume Boulmier
 * @since July 5, 2007
 */
@SuppressWarnings("unchecked")
public class KShortestPathCostTest
    extends TestCase
{
    //~ Methods ----------------------------------------------------------------

    public void testKShortestPathCompleteGraph4()
    {
        int nbPaths = 5;

        KShortestPathCompleteGraph4 graph = new KShortestPathCompleteGraph4();

        KShortestPaths pathFinder = new KShortestPaths(graph, "vS", nbPaths);
        List pathElements = pathFinder.getPaths("v3");

        assertEquals(
            "[[(vS : v1), (v1 : v3)], [(vS : v2), (v2 : v3)],"
            + " [(vS : v2), (v1 : v2), (v1 : v3)], "
            + "[(vS : v1), (v1 : v2), (v2 : v3)], "
            + "[(vS : v3)]]",
            pathElements.toString());

        assertEquals(5, pathElements.size(), 0);
        GraphPath pathElement = (GraphPath) pathElements.get(0);
        assertEquals(2, pathElement.getWeight(), 0);

        assertEquals(
            Arrays.asList(new Object[] { graph.eS1, graph.e13 }),
            pathElement.getEdgeList());
    }

    public void testKShortestPathCosts(Graph graph)
    {
        int maxSize = 20;

        for (
            Iterator sourceIterator = graph.vertexSet().iterator();
            sourceIterator.hasNext();)
        {
            Object sourceVertex = sourceIterator.next();

            for (
                Iterator targetIterator = graph.vertexSet().iterator();
                targetIterator.hasNext();)
            {
                Object targetVertex = targetIterator.next();

                if (targetVertex != sourceVertex) {
                    KShortestPaths pathFinder =
                        new KShortestPaths(graph,
                            sourceVertex, maxSize);

                    List pathElements = pathFinder.getPaths(targetVertex);
                    GraphPath pathElement = (GraphPath) pathElements.get(0);
                    double lastCost = pathElement.getWeight();
                    for (int i = 0; i < pathElements.size(); i++) {
                        pathElement = (GraphPath) pathElements.get(i);
                        double cost = pathElement.getWeight();
                        assertTrue(lastCost <= cost);
                        lastCost = cost;
                    }
                    assertTrue(pathElements.size() <= maxSize);
                }
            }
        }
    }

    public void testPicture1Graph()
    {
        Picture1Graph picture1Graph = new Picture1Graph();

        int maxSize = 10;

        KShortestPaths pathFinder =
            new KShortestPaths(picture1Graph, "vS",
                maxSize);

        assertEquals(2, pathFinder.getPaths("v5").size());

        List pathElements = pathFinder.getPaths("v5");
        GraphPath pathElement = (GraphPath) pathElements.get(0);
        assertEquals(
            Arrays.asList(
                new Object[] {
                    picture1Graph.eS1,
                    picture1Graph.e15
                }),
            pathElement.getEdgeList());

        List vertices = Graphs.getPathVertexList(pathElement);
        assertEquals(
            Arrays.asList(
                new Object[] {
                    "vS",
                    "v1",
                    "v5"
                }),
            vertices);

        pathElement = (GraphPath) pathElements.get(1);
        assertEquals(
            Arrays.asList(
                new Object[] {
                    picture1Graph.eS2,
                    picture1Graph.e25
                }),
            pathElement.getEdgeList());

        vertices = Graphs.getPathVertexList(pathElement);
        assertEquals(
            Arrays.asList(
                new Object[] {
                    "vS",
                    "v2",
                    "v5"
                }),
            vertices);

        pathElements = pathFinder.getPaths("v7");
        pathElement = (GraphPath) pathElements.get(0);
        double lastCost = pathElement.getWeight();
        for (int i = 0; i < pathElements.size(); i++) {
            pathElement = (GraphPath) pathElements.get(i);
            double cost = pathElement.getWeight();

            assertTrue(lastCost <= cost);
            lastCost = cost;
        }
    }
}

// End KShortestPathCostTest.java
