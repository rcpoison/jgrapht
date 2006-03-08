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
package org._3pq.jgrapht.experimental.alg;

import java.util.*;

import org._3pq.jgrapht.*;
import org._3pq.jgrapht.experimental.heap.*;
import org._3pq.jgrapht.graph.*;


/**
 * A general purpose implementation of Dijkstra's method.for the solution of
 * various problems in directed and undirected weighted graphs.  It is possible
 * to solve a lot of problems using Dijkstra's method and just specifying the
 * function which calculates a new "distance" from a "distance"to a previous
 * vertex and an edge weight, and whether the minimum or maximum are to be
 * achieved. For instance the shortest path problem is solved by using addition
 * as function and minimizing, the maximum capacity path (path where the
 * minimal weight (aka capacity) of the edges is maximized) is found by using
 * min as function and maximizing. A minimum spanning tree can be found by
 * using identity on the second parameter (edge weight) as function and
 * minimizing, and so on ...
 *
 * @author Michael Behrisch
 */
public abstract class DijkstraAlgorithm<V, E extends Edge<V>> extends WeightedGraphAlgorithm<V, E>
{

    //~ Instance fields -------------------------------------------------------

    /**
     * the heap for the vertices (stored here to make it reusable)
     */
    private final Heap _heap;

    /**
     * mapping graph vertices to heap vertices (stored here to make it
     * reusable). At the moment this map is rebuild on each call of the
     * algorithm. Future implementations may use Listeners to keep this map up
     * to date.
     */
    private final Map<Object, HeapVertex> _heapVertices = new HashMap<Object, HeapVertex>();

    /**
     * in which direction to compare. 1 (or any other positive value) means the
     * smaller the better, -1 (or other negative value) the opposite. This is
     * useful only for subclasses.
     */
    private final int _compare;

    //~ Constructors ----------------------------------------------------------

    /**
     * Creates an instance of DijkstraAlgorithm for the graph given using the
     * default BinaryHeap. This is equivalent to the call
     * DijkstraAlgorithm(wgraph, BinaryHeap.getFactory(), false)
     *
     * @param wgraph The WeightedGraph where a shortest path spanning tree will
     *               be determined.
     * @param maximum
     */
    public DijkstraAlgorithm(WeightedGraph<V,E> wgraph, boolean maximum)
    {
        this(wgraph, BinaryHeap.getFactory(), maximum);
    }

    /**
     * Creates an instance of DijkstraAlgorithm for the graph given using the
     * given Heap and optimization direction.
     *
     * @param wgraph The WeightedGraph where a shortest path spanning tree will
     *               be determined.
     * @param factory The factory to be used for creating heaps..
     * @param maximum
     */
    protected DijkstraAlgorithm(
        WeightedGraph<V,E> wgraph,
        HeapFactory factory,
        boolean maximum)
    {
        super(wgraph);
        _heap = factory.createHeap(maximum);
        _compare = maximum ? -1 : 1;
    }

    //~ Methods ---------------------------------------------------------------

    /**
     * Determines the optimum path with respect to the priority function from a
     * given vertex to all other vertices that are in the same connected set as
     * the given vertex in the weighted graph using Dijkstra's algorithm.
     *
     * @param from The Vertex from where we want to obtain the shortest path to
     *             all other vertices.
     *
     * @return A WeightedGraph comprising of the optimum path spanning tree.
     */
    public final WeightedGraph<V,E> optimumPathTree(V from)
    {
        WeightedGraph<V,E> optimumPathTree;

        if (_directed) {
        	//FIXME hb 051124: I would like to pass Edge<V> instead of DirectedEdge and remove the cast
            optimumPathTree = (WeightedGraph<V,E>)new SimpleDirectedWeightedGraph<V,DirEdge<V>>();
        } else {
            optimumPathTree = new SimpleWeightedGraph<V,E>();
        }

        _heap.clear();
        _heapVertices.clear();

        for (Iterator<V> it = _wgraph.vertexSet().iterator(); it.hasNext();) {
            Object vertex = it.next();
            HeapVertex heapV;

            if (vertex instanceof HeapVertex) {
                heapV = (HeapVertex) vertex;
            } else {
                heapV = new HeapVertex(vertex);
                _heapVertices.put(vertex, heapV);
            }

            if (vertex == from) {
                heapV.setPriority(
                    (_compare > 0) ? 0 : Double.POSITIVE_INFINITY);
            } else {
                heapV.setPriority(_compare * Double.POSITIVE_INFINITY);
            }

            _heap.add(heapV);
        }

        while (!_heap.isEmpty()) {
            HeapVertex hv = heapVertex(_heap.extractTop());
            V v = (V)hv.getVertex();				//FIXME hb 051124: Remove cast
            E treeEdge = (E) hv.getAdditional();	//FIXME hb 051124: Remove cast

            if (treeEdge != null) {
                GraphHelper.addEdgeWithVertices(optimumPathTree, treeEdge);
            }

            Iterator<? extends Edge<V>> edges;

            if (_directed) {
                edges =
                    ((DirectedGraph<V,DirEdge<V>>) _wgraph).outgoingEdgesOf(v).iterator();
            } else {
                edges = _wgraph.edgesOf(v).iterator();
            }

            while (edges.hasNext()) {
                Edge<V> e = edges.next();
                HeapVertex u = heapVertex(e.oppositeVertex(v));
                double newPrio =
                    priorityFunction(hv.getPriority(), e.getWeight());

                if ((_compare * (u.getPriority() - newPrio)) > 0) {
                    u.setPriority(newPrio);
                    u.setAdditional(e);
                    _heap.update(u);
                }
            }
        }

        return optimumPathTree;
    }

    /**
     * .
     *
     * @param vertexPrio
     * @param edgeWeight
     *
     * @return
     */
    protected abstract double priorityFunction(
        double vertexPrio,
        double edgeWeight);


    /**
     * @param v
     * @return
     */
    private final HeapVertex heapVertex(Object v)
    {
        if (v instanceof HeapVertex) {
            return (HeapVertex) v;
        }

        return _heapVertices.get(v);
    }
}
