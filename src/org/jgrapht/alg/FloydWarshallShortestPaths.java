/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Creator:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2009, by Barak Naveh and Contributors.
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
 * FloydWarshallShortestPaths.java
 * -------------------------
 * (C) Copyright 2009-2009, by Tom Larkworthy and Contributors
 *
 * Original Author:  Tom Larkworthy
 * Contributor(s):   Soren Davidsen
 *
 * $Id$
 *
 * Changes
 * -------
 * 29-Jun-2009 : Initial revision (TL);
 * 03-Dec-2009 : Optimized and enhanced version (SD);
 *
 */
package org.jgrapht.alg;

import java.util.*;

import org.jgrapht.*;
import org.jgrapht.util.*;
import org.jgrapht.graph.*;


/**
 * The <a href="http://en.wikipedia.org/wiki/Floyd-Warshall_algorithm">
 * Floyd-Warshall algorithm</a> finds all shortest paths (all n^2 of them) in
 * O(n^3) time. This also works out the graph diameter during the process.
 *
 * @author Tom Larkworthy
 * @author Soren Davidsen <soren@tanesha.net>
 */
public class FloydWarshallShortestPaths<V, E>
{
    private Graph<V, E> graph;
    private List<V> vertices;
    private int countShortestPaths = 0;
    private double diameter = 0.0;
    private double[][] d = null;
    private int[][] backtrace = null;
    private Map<VertexPair<V>, GraphPath<V,E>> paths = null;

    public FloydWarshallShortestPaths(Graph<V, E> graph) {
        this.graph = graph;
        this.vertices = new ArrayList<V>(graph.vertexSet());
    }

    protected Graph<V, E> getGraph() {
        return graph;
    }

    public int shortestPathsCount() {
        return countShortestPaths;
    }

    /**
     * Calculates all shortest paths.
     */
    private void lazyCalculate() {

        int n = vertices.size();

        // init the backtrace matrix
        backtrace = new int[n][n];
        for (int i = 0; i < n; i++)
            Arrays.fill(backtrace[i], -1);

        // initialize matrix, 0
        d = new double[n][n];
        for (int i = 0; i < n; i++)
            Arrays.fill(d[i], Double.POSITIVE_INFINITY);

        // initialize matrix, 1
        for (int i = 0; i < n; i++) {
            d[i][i] = 0.0;
        }

        // initialize matrix, 2
        Set<E> edges = graph.edgeSet();
        for (E edge : edges) {
            V v1 = graph.getEdgeSource(edge);
            V v2 = graph.getEdgeTarget(edge);

            int v_1 = vertices.indexOf(v1);
            int v_2 = vertices.indexOf(v2);

            d[v_1][v_2] = graph.getEdgeWeight(edge);
            if (!(graph instanceof DirectedGraph)) {
                d[v_2][v_1] = graph.getEdgeWeight(edge);
            }
        }

        // run fw alg
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    double ik_kj = d[i][k] + d[k][j];
                    if (ik_kj < d[i][j]) {
                        d[i][j] = ik_kj;
                        backtrace[i][j] = k;
                        diameter = diameter > d[i][j] ? diameter : d[i][j];
                    }
                }
            }
        }
    }

    /**
     * Get the length of a shortest path.
     * @param a
     * @param b
     * @return
     */
    public double shortestDistance(V a, V b) {

        // lazy
        if (d == null)
            lazyCalculate();

        return d[vertices.indexOf(a)][vertices.indexOf(b)];
    }

    public double getDiameter() {
        return diameter;
    }

    private void shortestPathRecur(List<E> edges, int v_a, int v_b) {
        int k = backtrace[v_a][v_b];
        if (k == -1) {
            E edge = graph.getEdge(vertices.get(v_a), vertices.get(v_b));
            if (edge != null)
                edges.add(edge);
        }
        else {
            shortestPathRecur(edges, v_a, k);
            shortestPathRecur(edges, k, v_b);
        }
    }

    /**
     * Get the shortest path between two vertices.
     *
     * Note: The paths are calculated using a recursive algorithm. It *will* give problems on paths longer than
     * the heap allows.
     * 
     * @param a From vertice
     * @param b To vertice
     * @return the path
     */
    public GraphPath<V, E> shortestPath(V a, V b) {

        if (d == null)
            lazyCalculatePaths();

        int v_a = vertices.indexOf(a);
        int v_b = vertices.indexOf(b);

        List<E> edges = new ArrayList<E>();
        shortestPathRecur(edges, v_a, v_b);

        // no path, return null
        if (edges.size() < 1)
            return null;

        GraphPathImpl<V, E> path = new GraphPathImpl<V, E>(graph, a, b, edges, edges.size());

        return path;
    }

    /**
     * Calculate the shortest paths (not done per default)
     * @return the number of shortest paths.
     */
    public int lazyCalculatePaths() {

        // already we have calculated it once.
        if (paths != null) {
            return countShortestPaths;
        }

        // we don't have shortest paths.. lazyCalculate it.
        if (d == null)
            lazyCalculate();

        Map<VertexPair<V>, GraphPath<V, E>> sps = new HashMap<VertexPair<V>, GraphPath<V, E>>();
        int n = vertices.size();

        countShortestPaths = 0;
        for (int i = 0; i < n; i++) {

            for (int j = 0; j < n; j++) {

                // don't count this.
                if (i == j)
                    continue;

                V v_i = vertices.get(i);
                V v_j = vertices.get(j);

                GraphPath<V, E> path = shortestPath(v_i, v_j);

                // we got a path
                if (path != null) {
                    sps.put(new VertexPair<V>(v_i, v_j), path);
                    countShortestPaths++;
                }
            }
        }

        this.paths = sps;
        return countShortestPaths;
    }

    /**
     * Get shortest paths from a vertex to all other vertices in the graph.
     * @param v the originating vertex
     * @return List of paths
     */
    public List<GraphPath<V, E>> getShortestPaths(V v) {
        if (v == null)
            return null;

        List<GraphPath<V, E>> found = new ArrayList<GraphPath<V, E>>();
        for (VertexPair<V> pair : paths.keySet()) {
            if (pair.hasVertex(v)) {
                found.add(paths.get(pair));
            }
        }

        return found;
    }
}

// End FloydWarshallShortestPaths.java
