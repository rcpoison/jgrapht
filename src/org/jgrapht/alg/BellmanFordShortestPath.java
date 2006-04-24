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
/* -------------------------
 * BellmanFordShortestPath.java
 * -------------------------
 * (C) Copyright 2006, by France Telecom and Contributors.
 *
 * Original Author:  Guillaume Boulmier and Contributors.
 * Contributor(s):   John V. Sichi
 *
 * $Id$
 *
 * Changes
 * -------
 * 05-Jan-2006 : Initial revision (GB);
 * 14-Jan-2006 : Added support for generics (JVS);
 *
 */
package org._3pq.jgrapht.alg;

import java.util.List;

import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.Edge;

/**
 * <a href="http://www.nist.gov/dads/HTML/dijkstraalgo.html">Bellman-Ford
 * algorithm </a>: weights could be negative, paths could be constrained by a
 * maximum number of edges.
 *  
 */
public class BellmanFordShortestPath<V,E extends Edge<V>> {

    /**
     * Graph on which shortest paths are searched.
     */
    protected Graph<V,E> graph;

    /**
     * Start vertex.
     */
    protected V startVertex;

    private BellmanFordIterator<V,E> iter;

    /**
     * Maximum number of edges of the calculated paths.
     */
    private int nMaxHops;

    private int passNumber;

    /**
     * Creates an object to calculate shortest paths between the start vertex
     * and others vertices using the Bellman-Ford algorithm.
     * 
     * @param graph
     * @param startVertex
     */
    public BellmanFordShortestPath(Graph<V,E> graph, V startVertex) {
        this(graph, startVertex, graph.vertexSet().size() - 1);
    }

    /**
     * Creates an object to calculate shortest paths between the start vertex
     * and others vertices using the Bellman-Ford algorithm.
     * 
     * 
     * @param graph
     * @param startVertex
     * @param nMaxHops
     *            maximum number of edges of the calculated paths.
     */
    public BellmanFordShortestPath(
        Graph<V,E> graph, V startVertex, int nMaxHops) {
        this.startVertex = startVertex;
        this.nMaxHops = nMaxHops;
        this.graph = graph;

        this.passNumber = 1;
    }

    /**
     * 
     * @param endVertex
     *            end vertex.
     * @return the cost of the shortest path between the start vertex and the
     *         end vertex.
     */
    public double getCost(V endVertex) {
        lazyCalculate();

        assertGetPath(endVertex);

        return this.iter.getPathElement(endVertex).getCost();
    }

    /**
     * 
     * @param endVertex
     *            end vertex.
     * @return list of <code>Edge</code>, or null
     *             if no path exists between the start vertex and the end
     *             vertex.
     */
    public List<E> getPathEdgeList(V endVertex) {
        assertGetPath(endVertex);

        lazyCalculate();

        if (this.iter.getPathElement(endVertex) == null) {
            return null;
        }

        return createPath(endVertex);
    }

    private void assertGetPath(V endVertex) {

        if (endVertex.equals(this.startVertex)) {
            throw new IllegalArgumentException(
                    "The end vertex is the same as the start vertex!");
        }

        if (!this.graph.containsVertex(endVertex)) {
            throw new IllegalArgumentException(
                    "Graph must contain the end vertex!");
        }

    }

    /**
     * Complexity = O(length of path)
     * 
     * @param endVertex
     *            end vertex.
     * @return list of <code>Edge</code>.
     */
    private List<E> createPath(V endVertex) {
        AbstractPathElement<V,E> pathElement =
            this.iter.getPathElement(endVertex);

        return pathElement.createEdgeListPath();
    }

    private void lazyCalculate() {
        if (this.iter == null) {
            this.iter =
                new BellmanFordIterator<V,E>(this.graph, this.startVertex);
        }

        // at the i-th pass the shortest paths with less (or equal) than i edges
        // are calculated.
        for (; this.passNumber <= this.nMaxHops && this.iter.hasNext(); this.passNumber++) {
            this.iter.next();
        }
    }

    /**
     * Convenience method to find the shortest path via a single static method
     * call.  If you need a more advanced search (e.g. limited by hops, or
     * computation of the path length), use the constructor instead.
     *
     * @param graph the graph to be searched
     * @param startVertex the vertex at which the path should start
     * @param endVertex the vertex at which the path should end
     *
     * @return List of Edges, or null if no path exists
     */
    public static <V, E extends Edge<V>> List<E> findPathBetween(
        Graph<V, E> graph,
        V startVertex,
        V endVertex)
    {
        BellmanFordShortestPath<V,E> alg =
            new BellmanFordShortestPath<V,E>(
                graph,
                startVertex);

        return alg.getPathEdgeList(endVertex);
    }
}
