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
/* -----------------------
 * GraphGeneratorTest.java
 * -----------------------
 * (C) Copyright 2003-2007, by John V. Sichi and Contributors.
 *
 * Original Author:  John V. Sichi
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 17-Sep-2003 : Initial revision (JVS);
 * 07-May-2006 : Changed from List<Edge> to Set<Edge> (JVS);
 *
 */
package org.jgrapht.generate;

import java.util.*;

import junit.framework.*;

import org.jgrapht.*;
import org.jgrapht.alg.*;
import org.jgrapht.graph.*;


/**
 * .
 *
 * @author John V. Sichi
 * @since Sep 17, 2003
 */
public class GraphGeneratorTest
    extends TestCase
{
    //~ Static fields/initializers ---------------------------------------------

    private static final int SIZE = 10;

    //~ Instance fields --------------------------------------------------------

    private VertexFactory<Object> vertexFactory =
        new VertexFactory<Object>() {
            private int i;

            public Object createVertex()
            {
                return new Integer(++i);
            }
        };

    //~ Methods ----------------------------------------------------------------

    /**
     * .
     */
    public void testEmptyGraphGenerator()
    {
        GraphGenerator<Object, DefaultEdge, Object> gen =
            new EmptyGraphGenerator<Object, DefaultEdge>(SIZE);
        DirectedGraph<Object, DefaultEdge> g =
            new DefaultDirectedGraph<Object, DefaultEdge>(DefaultEdge.class);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        gen.generateGraph(g, vertexFactory, resultMap);
        assertEquals(SIZE, g.vertexSet().size());
        assertEquals(0, g.edgeSet().size());
        assertTrue(resultMap.isEmpty());
    }

    /**
     * .
     */
    public void testLinearGraphGenerator()
    {
        GraphGenerator<Object, DefaultEdge, Object> gen =
            new LinearGraphGenerator<Object, DefaultEdge>(SIZE);
        DirectedGraph<Object, DefaultEdge> g =
            new DefaultDirectedGraph<Object, DefaultEdge>(DefaultEdge.class);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        gen.generateGraph(g, vertexFactory, resultMap);
        assertEquals(SIZE, g.vertexSet().size());
        assertEquals(SIZE - 1, g.edgeSet().size());

        Object startVertex = resultMap.get(LinearGraphGenerator.START_VERTEX);
        Object endVertex = resultMap.get(LinearGraphGenerator.END_VERTEX);
        Iterator vertexIter = g.vertexSet().iterator();

        while (vertexIter.hasNext()) {
            Object vertex = vertexIter.next();

            if (vertex == startVertex) {
                assertEquals(0, g.inDegreeOf(vertex));
                assertEquals(1, g.outDegreeOf(vertex));

                continue;
            }

            if (vertex == endVertex) {
                assertEquals(1, g.inDegreeOf(vertex));
                assertEquals(0, g.outDegreeOf(vertex));

                continue;
            }

            assertEquals(1, g.inDegreeOf(vertex));
            assertEquals(1, g.outDegreeOf(vertex));
        }
    }

    /**
     * .
     */
    public void testRingGraphGenerator()
    {
        GraphGenerator<Object, DefaultEdge, Object> gen =
            new RingGraphGenerator<Object, DefaultEdge>(SIZE);
        DirectedGraph<Object, DefaultEdge> g =
            new DefaultDirectedGraph<Object, DefaultEdge>(DefaultEdge.class);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        gen.generateGraph(g, vertexFactory, resultMap);
        assertEquals(SIZE, g.vertexSet().size());
        assertEquals(SIZE, g.edgeSet().size());

        Object startVertex = g.vertexSet().iterator().next();
        assertEquals(1, g.outDegreeOf(startVertex));

        Object nextVertex = startVertex;
        Set<Object> seen = new HashSet<Object>();

        for (int i = 0; i < SIZE; ++i) {
            DefaultEdge nextEdge =
                g.outgoingEdgesOf(nextVertex).iterator().next();
            nextVertex = g.getEdgeTarget(nextEdge);
            assertEquals(1, g.inDegreeOf(nextVertex));
            assertEquals(1, g.outDegreeOf(nextVertex));
            assertTrue(!seen.contains(nextVertex));
            seen.add(nextVertex);
        }

        // do you ever get the feeling you're going in circles?
        assertTrue(nextVertex == startVertex);
        assertTrue(resultMap.isEmpty());
    }

    /**
     * .
     */
    public void testCompleteGraphGenerator()
    {
        Graph<Object, DefaultEdge> completeGraph =
            new SimpleGraph<Object, DefaultEdge>(DefaultEdge.class);
        CompleteGraphGenerator<Object, DefaultEdge> completeGenerator =
            new CompleteGraphGenerator<Object, DefaultEdge>(10);
        completeGenerator.generateGraph(
            completeGraph,
            new ClassBasedVertexFactory<Object>(Object.class),
            null);

        // complete graph with 10 vertices has 10*(10-1)/2 = 45 edges
        assertEquals(45, completeGraph.edgeSet().size());
    }

    /**
     * .
     */
    public void testScaleFreeGraphGenerator()
    {
        DirectedGraph<Object, DefaultEdge> graph =
            new DefaultDirectedGraph<Object, DefaultEdge>(DefaultEdge.class);
        ScaleFreeGraphGenerator<Object, DefaultEdge> generator =
            new ScaleFreeGraphGenerator<Object, DefaultEdge>(500);
        generator.generateGraph(graph, vertexFactory, null);
        ConnectivityInspector<Object, DefaultEdge> inspector =
            new ConnectivityInspector<Object, DefaultEdge>(graph);
        assertTrue(
            "generated graph is not connected",
            inspector.isGraphConnected());

        try {
            generator = new ScaleFreeGraphGenerator<Object, DefaultEdge>(-50);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        }

        try {
            generator =
                new ScaleFreeGraphGenerator<Object, DefaultEdge>(-50, 31337);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        }

        generator = new ScaleFreeGraphGenerator<Object, DefaultEdge>(0);
        DirectedGraph<Object, DefaultEdge> empty =
            new DefaultDirectedGraph<Object, DefaultEdge>(DefaultEdge.class);
        generator.generateGraph(empty, vertexFactory, null);
        assertTrue("non-empty graph generated", empty.vertexSet().size() == 0);
    }

    // TODO:  testWheelGraphGenerator
}

// End GraphGeneratorTest.java
