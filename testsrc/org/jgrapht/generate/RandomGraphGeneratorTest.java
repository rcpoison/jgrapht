/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Creator:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2007, by Barak Naveh and Contributors.
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
/* -----------------
 * RandomGraphGeneratorTest.java
 * -----------------
 * (C) Copyright 2005-2007, by Assaf Lehr and Contributors.
 *
 * Original Author:  Assaf Lehr
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 */
package org.jgrapht.generate;

import junit.framework.*;

import org.jgrapht.*;
import org.jgrapht.experimental.isomorphism.*;
import org.jgrapht.graph.*;


/**
 * @author Assaf
 * @since Aug 6, 2005
 */
public class RandomGraphGeneratorTest
    extends TestCase
{
    //~ Methods ----------------------------------------------------------------

    public void testGenerateGraph2()
    {
        Graph [] graphArray = testGenerateDirectedGraph();

        assertTrue(EdgeTopologyCompare.compare(graphArray[0], graphArray[1]));
        // cannot assert false , cause it may be true once in a while (random)
        // but it generally should work.
        // assertFalse(EdgeTopologyCompare.compare(graphArray[1],graphArray[2]));
    }

    /**
     * Creates 3 graphs with the same numOfVertex and numOfEdges. The first two
     * are generated using the same RandomGraphGenerator; the third is generated
     * using a new instance. The graphs are <code>directedGragh1,
     * directedGragh2, directedGragh3</code>
     */
    private static Graph [] testGenerateDirectedGraph()
    {
        final int numOfVertex = 11;
        final int numOfEdges = 110; // simple undirected max = N(v)x(N(v)-1)
        RandomGraphGenerator<Integer, DefaultEdge> randomGen =
            new RandomGraphGenerator<Integer, DefaultEdge>(
                numOfVertex,
                numOfEdges);

        Graph<Integer, DefaultEdge> directedGragh1 =
            new SimpleDirectedGraph<Integer, DefaultEdge>(
                DefaultEdge.class);

        randomGen.generateGraph(
            directedGragh1,
            new IntegerVertexFactory(),
            null);

        // use the same randomGen
        Graph<Integer, DefaultEdge> directedGragh2 =
            new SimpleDirectedGraph<Integer, DefaultEdge>(
                DefaultEdge.class);

        randomGen.generateGraph(
            directedGragh2,
            new IntegerVertexFactory(),
            null);

        // use new randomGen here
        RandomGraphGenerator<Integer, DefaultEdge> newRandomGen =
            new RandomGraphGenerator<Integer, DefaultEdge>(
                numOfVertex,
                numOfEdges);

        Graph<Integer, DefaultEdge> directedGragh3 =
            new SimpleDirectedGraph<Integer, DefaultEdge>(
                DefaultEdge.class);

        newRandomGen.generateGraph(
            directedGragh3,
            new IntegerVertexFactory(),
            null);

        return new Graph[] {
                directedGragh1,
                directedGragh2,
                directedGragh3
            };
    }
}

// End RandomGraphGeneratorTest.java
