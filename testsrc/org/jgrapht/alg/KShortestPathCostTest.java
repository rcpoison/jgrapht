/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Creator:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2010, by Barak Naveh and Contributors.
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
 * (C) Copyright 2007-2010, by France Telecom
 *
 * Original Author:  Guillaume Boulmier and Contributors.
 *
 * $Id$
 *
 * Changes
 * -------
 * 05-Jun-2007 : Initial revision (GB);
 * 06-Dec-2010 : Bugfixes (GB);
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
            + "[(vS : v1), (v1 : v2), (v2 : v3)], " + "[(vS : v3)]]",
            pathElements.toString());

        assertEquals(5, pathElements.size(), 0);
        GraphPath pathElement = (GraphPath) pathElements.get(0);
        assertEquals(2, pathElement.getWeight(), 0);

        assertEquals(
            Arrays.asList(new Object[] { graph.eS1, graph.e13 }),
            pathElement.getEdgeList());
    }

    public void testPicture1Graph()
    {
        Picture1Graph picture1Graph = new Picture1Graph();

        int maxSize = 10;

        KShortestPaths pathFinder =
            new KShortestPaths(picture1Graph, "vS",
                maxSize);

        //      assertEquals(2, pathFinder.getPaths("v5").size());

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
            Arrays.asList(new Object[] { "vS", "v1", "v5" }),
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
            Arrays.asList(new Object[] { "vS", "v2", "v5" }),
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

    public void testShortestPathsInIncreasingOrder()
    {
        BiconnectedGraph biconnectedGraph = new BiconnectedGraph();
        verifyShortestPathsInIncreasingOrderOfWeight(biconnectedGraph);

        KShortestPathCompleteGraph4 kSPCompleteGraph4 =
            new KShortestPathCompleteGraph4();
        verifyShortestPathsInIncreasingOrderOfWeight(kSPCompleteGraph4);

        KShortestPathCompleteGraph5 kSPCompleteGraph5 =
            new KShortestPathCompleteGraph5();
        verifyShortestPathsInIncreasingOrderOfWeight(kSPCompleteGraph5);

        KShortestPathCompleteGraph6 kSPCompleteGraph6 =
            new KShortestPathCompleteGraph6();
        verifyShortestPathsInIncreasingOrderOfWeight(kSPCompleteGraph6);

        KSPExampleGraph kSPExampleGraph = new KSPExampleGraph();
        verifyShortestPathsInIncreasingOrderOfWeight(kSPExampleGraph);

        NotBiconnectedGraph notBiconnectedGraph = new NotBiconnectedGraph();
        verifyShortestPathsInIncreasingOrderOfWeight(notBiconnectedGraph);

        Picture1Graph picture1Graph = new Picture1Graph();
        verifyShortestPathsInIncreasingOrderOfWeight(picture1Graph);
    }

    public void testShortestPathsWeightsWithMaxSizeIncreases()
    {
        BiconnectedGraph biconnectedGraph = new BiconnectedGraph();
        verifyShortestPathsWeightsWithMaxSizeIncreases(biconnectedGraph);

        KShortestPathCompleteGraph4 kSPCompleteGraph4 =
            new KShortestPathCompleteGraph4();
        verifyShortestPathsWeightsWithMaxSizeIncreases(kSPCompleteGraph4);

        KShortestPathCompleteGraph5 kSPCompleteGraph5 =
            new KShortestPathCompleteGraph5();
        verifyShortestPathsWeightsWithMaxSizeIncreases(kSPCompleteGraph5);

        KShortestPathCompleteGraph6 kSPCompleteGraph6 =
            new KShortestPathCompleteGraph6();
        verifyShortestPathsWeightsWithMaxSizeIncreases(kSPCompleteGraph6);

        KSPExampleGraph kSPExampleGraph = new KSPExampleGraph();
        verifyShortestPathsWeightsWithMaxSizeIncreases(kSPExampleGraph);

        NotBiconnectedGraph notBiconnectedGraph = new NotBiconnectedGraph();
        verifyShortestPathsWeightsWithMaxSizeIncreases(notBiconnectedGraph);

        Picture1Graph picture1Graph = new Picture1Graph();
        verifyShortestPathsWeightsWithMaxSizeIncreases(picture1Graph);
    }

    private void verifyShortestPathsInIncreasingOrderOfWeight(Graph graph)
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
                    if (pathElements == null) {
                        // no path exists between the start vertex and the end
                        // vertex
                        continue;
                    }
                    GraphPath pathElement = (GraphPath) pathElements.get(0);
                    double lastWeight = pathElement.getWeight();
                    for (int i = 0; i < pathElements.size(); i++) {
                        pathElement = (GraphPath) pathElements.get(i);
                        double weight = pathElement.getWeight();
                        assertTrue(lastWeight <= weight);
                        lastWeight = weight;
                    }
                    assertTrue(pathElements.size() <= maxSize);
                }
            }
        }
    }

    private void verifyShortestPathsWeightsWithMaxSizeIncreases(Graph graph)
    {
        int maxSizeLimit = 10;

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
                            sourceVertex, 1);
                    List<GraphPath> prevPathElementsResults =
                        pathFinder.getPaths(targetVertex);

                    if (prevPathElementsResults == null) {
                        // no path exists between the start vertex and the
                        // end vertex
                        continue;
                    }

                    for (int maxSize = 2; maxSize < maxSizeLimit; maxSize++) {
                        pathFinder =
                            new KShortestPaths(graph, sourceVertex,
                                maxSize);
                        List<GraphPath> pathElementsResults =
                            pathFinder.getPaths(targetVertex);

                        verifyWeightsConsistency(
                            prevPathElementsResults,
                            pathElementsResults);
                    }
                }
            }
        }
    }

    /**
     * Verify weights consistency between the results when the max-size argument
     * increases.
     *
     * @param prevPathElementsResults results obtained with a max-size argument
     * equal to <code>k</code>
     * @param pathElementsResults results obtained with a max-size argument
     * equal to <code>k+1</code>
     */
    private void verifyWeightsConsistency(
        List<GraphPath> prevPathElementsResults,
        List<GraphPath> pathElementsResults)
    {
        for (int i = 0; i < prevPathElementsResults.size(); i++) {
            GraphPath pathElementResult =
                (GraphPath) pathElementsResults.get(i);
            GraphPath prevPathElementResult =
                (GraphPath) prevPathElementsResults.get(i);
            assertTrue(
                pathElementResult.getWeight()
                == prevPathElementResult.getWeight());
        }
    }
}

// End KShortestPathCostTest.java
