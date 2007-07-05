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
/* -------------------------
 * KShortestPathKValuesTest.java
 * -------------------------
 * (C) Copyright 2007-2007, by France Telecom
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
public class KShortestPathKValuesTest
    extends TestCase
{
    //~ Methods ----------------------------------------------------------------

    /**
     * @param n
     *
     * @return n!.
     */
    public static int factorial(int n)
    {
        int factorial = 1;
        for (int i = 1; i <= n; i++) {
            factorial *= i;
        }
        return factorial;
    }

    /**
     * @param k
     * @param n
     *
     * @return A(n,k).
     */
    public static int permutation(int n, int k)
    {
        if (k <= n) {
            return factorial(n) / factorial(n - k);
        } else {
            return 0;
        }
    }

    public void testMaxSizeValue()
    {
        KShortestPathCompleteGraph6 graph = new KShortestPathCompleteGraph6();

        for (
            int maxSize = 1;
            maxSize <= calculateNbElementaryPathsForCompleteGraph(6);
            maxSize++)
        {
            KShortestPaths finder = new KShortestPaths(graph, "vS", maxSize);

            assertEquals(finder.getPathElements("v1").size(), maxSize);
            assertEquals(finder.getPathElements("v2").size(), maxSize);
            assertEquals(finder.getPathElements("v3").size(), maxSize);
            assertEquals(finder.getPathElements("v4").size(), maxSize);
            assertEquals(finder.getPathElements("v5").size(), maxSize);
        }
    }

    public void testNbReturnedPaths()
    {
        KShortestPathCompleteGraph4 kSPCompleteGraph4 =
            new KShortestPathCompleteGraph4();
        verifyNbPathsForAllVertices(kSPCompleteGraph4);

        KShortestPathCompleteGraph5 kSPCompleteGraph5 =
            new KShortestPathCompleteGraph5();
        verifyNbPathsForAllVertices(kSPCompleteGraph5);

        KShortestPathCompleteGraph6 kSPCompleteGraph6 =
            new KShortestPathCompleteGraph6();
        verifyNbPathsForAllVertices(kSPCompleteGraph6);
    }

    private int calculateNbElementaryPathsForCompleteGraph(int n)
    {
        int nbPaths = 0;
        for (int k = 1; k <= (n - 1); k++) {
            nbPaths = nbPaths + permutation(n - 2, k - 1);
        }
        return nbPaths;
    }

    private void verifyNbPathsForAllVertices(Graph graph)
    {
        int nbpaths =
            calculateNbElementaryPathsForCompleteGraph(
                graph.vertexSet().size());
        int maxSize = Integer.MAX_VALUE;

        for (
            Iterator sourceIterator = graph.vertexSet().iterator();
            sourceIterator.hasNext();)
        {
            Object sourceVertex = sourceIterator.next();

            KShortestPaths finder =
                new KShortestPaths(graph, sourceVertex,
                    maxSize);
            for (
                Iterator targetIterator = graph.vertexSet().iterator();
                targetIterator.hasNext();)
            {
                Object targetVertex = targetIterator.next();
                if (targetVertex != sourceVertex) {
                    assertEquals(
                        finder.getPathElements(targetVertex).size(),
                        nbpaths);
                }
            }
        }
    }
}

// End KShortestPathKValuesTest.java
