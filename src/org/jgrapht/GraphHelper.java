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
/* ----------------
 * GraphHelper.java
 * ----------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   Christian Hammer
 *                   Mikael Hansen
 *
 * $Id$
 *
 * Changes
 * -------
 * 10-Jul-2003 : Initial revision (BN);
 * 06-Nov-2003 : Change edge sharing semantics (JVS);
 * 11-Mar-2004 : Made generic (CH);
 * 07-May-2006 : Changed from List<Edge> to Set<Edge> (JVS);
 *
 */
package org.jgrapht;

import java.util.*;

import org.jgrapht.edge.*;
import org.jgrapht.graph.*;


/**
 * A collection of utilities to assist the working with graphs.
 *
 * @author Barak Naveh
 * @since Jul 31, 2003
 */
public final class GraphHelper
{

    //~ Constructors ----------------------------------------------------------

    private GraphHelper()
    {
    } // ensure non-instantiability.

    //~ Methods ---------------------------------------------------------------

    /**
     * Creates a new edge and adds it to the specified graph similarly to the
     * {@link Graph#addEdge(Object, Object)} method.
     *
     * @param g the graph for which the edge to be added.
     * @param sourceVertex source vertex of the edge.
     * @param targetVertex target vertex of the edge.
     * @param weight weight of the edge.
     *
     * @return The newly created edge if added to the graph, otherwise <code>
     *         null</code>.
     *
     * @see Graph#addEdge(Object, Object)
     */
    public static <V, E extends Edge<V>> E addEdge(Graph<V, E> g,
        V sourceVertex,
        V targetVertex,
        double weight)
    {
        EdgeFactory<V, E> ef = g.getEdgeFactory();
        E e = ef.createEdge(sourceVertex, targetVertex);

        // we first create the edge and set the weight to make sure that
        // listeners will see the correct weight upon addEdge.
        e.setWeight(weight);

        return g.addEdge(e) ? e : null;
    }

    /**
     * Adds the specified edge to the specified graph including its vertices.
     * If any of the vertices of the specified edge are not already in the
     * graph they are also added (before the edge is added).
     *
     * @param g the graph for which the specified edge to be added.
     * @param e the edge to be added to the graph (including its vertices).
     *
     * @return <code>true</code> if and only if the specified edge was not
     *         already contained in the graph.
     */
    public static <V, E extends Edge<V>> boolean addEdgeWithVertices(Graph<V, E> g,
        E e)
    {
        g.addVertex(e.getSource());
        g.addVertex(e.getTarget());

        return g.addEdge(e);
    }

    /**
     * Adds the specified source and target vertices to the graph, if not
     * already included, and creates a new edge and adds it to the specified
     * graph similarly to the {@link Graph#addEdge(Object, Object)} method.
     *
     * @param g the graph for which the specified edge to be added.
     * @param sourceVertex source vertex of the edge.
     * @param targetVertex target vertex of the edge.
     *
     * @return The newly created edge if added to the graph, otherwise <code>
     *         null</code>.
     */
    public static <V, E extends Edge<V>> E addEdgeWithVertices(Graph<V, E> g,
        V sourceVertex,
        V targetVertex)
    {
        g.addVertex(sourceVertex);
        g.addVertex(targetVertex);

        return g.addEdge(sourceVertex, targetVertex);
    }

    /**
     * Adds the specified source and target vertices to the graph, if not
     * already included, and creates a new weighted edge and adds it to the
     * specified graph similarly to the {@link Graph#addEdge(Object, Object)}
     * method.
     *
     * @param g the graph for which the specified edge to be added.
     * @param sourceVertex source vertex of the edge.
     * @param targetVertex target vertex of the edge.
     * @param weight weight of the edge.
     *
     * @return The newly created edge if added to the graph, otherwise <code>
     *         null</code>.
     */
    public static <V, E extends Edge<V>> E addEdgeWithVertices(Graph<V, E> g,
        V sourceVertex,
        V targetVertex,
        double weight)
    {
        g.addVertex(sourceVertex);
        g.addVertex(targetVertex);

        return addEdge(g, sourceVertex, targetVertex, weight);
    }

    /**
     * Adds all the vertices and all the edges of the specified source graph to
     * the specified destination graph. First all vertices of the source graph
     * are added to the destination graph. Then every edge of the source graph
     * is added to the destination graph. This method returns <code>true</code>
     * if the destination graph has been modified as a result of this
     * operation, otherwise it returns <code>false</code>.
     *
     * <p>The behavior of this operation is undefined if any of the specified
     * graphs is modified while operation is in progress.</p>
     *
     * @param destination the graph to which vertices and edges are added.
     * @param source the graph used as source for vertices and edges to add.
     *
     * @return <code>true</code> if and only if the destination graph has been
     *         changed as a result of this operation.
     */
    public static <V, E extends Edge<V>> boolean addGraph(Graph<V, E> destination,
        Graph<? extends V, ? extends E> source)
    {
        boolean modified = destination.addAllVertices(source.vertexSet());
        modified |= destination.addAllEdges(source.edgeSet());

        return modified;
    }

    /**
     * Adds all the vertices and all the edges of the specified source digraph
     * to the specified destination digraph, reversing all of the edges.
     *
     * <p>The behavior of this operation is undefined if any of the specified
     * graphs is modified while operation is in progress.</p>
     *
     * @param destination the graph to which vertices and edges are added.
     * @param source the graph used as source for vertices and edges to add.
     //FIXME hb 26-Nov-05: How to achieve type-safty here if dst is a super-type of src?
     */
    @SuppressWarnings("unchecked")    // FIXME hb 28-nov-05: See below
    public static <V, E extends DirEdge<V>> void addGraphReversed(
        DirectedGraph<V,E> destination,
        DirectedGraph<V,E> source)
    {
        destination.addAllVertices(source.vertexSet());

        for (E edge : source.edgeSet()) {
            //FIXME hb 26-Nov-05: Use the edge factory of src, dst, or passed as a parameter to create edges
            E reversedEdge =
                (E)new DirectedEdge<V>(edge.getTarget(), edge.getSource());
            destination.addEdge(reversedEdge);
        }
    }

    /**
     * Returns a list of vertices that are the neighbors of a specified vertex.
     * If the graph is a multigraph vertices may appear more than once in the
     * returned list.
     *
     * @param g the graph to look for neighbors in.
     * @param vertex the vertex to get the neighbors of.
     *
     * @return a list of the vertices that are the neighbors of the specified
     *         vertex.
     */
    public static <V, E extends Edge<V>> List<V> neighborListOf(Graph<V, E> g,
        V vertex)
    {
        List<V> neighbors = new ArrayList<V>();

        for( E e : g.edgesOf(vertex) ) {
            neighbors.add(e.oppositeVertex(vertex));            
        }

        return neighbors;
    }

    /**
     * Returns a list of vertices that are the predecessors of a specified
     * vertex. If the graph is a multigraph, vertices may appear more than once
     * in the returned list.
     *
     * @param g the graph to look for predecessors in.
     * @param vertex the vertex to get the predecessors of.
     *
     * @return a list of the vertices that are the predecessors of the
     *         specified vertex.
     */
    public static <V, E extends DirEdge<V>> List<V> predecessorListOf(DirectedGraph<V, E> g,
        V vertex)
    {
        List<V> predecessors = new ArrayList<V>();
        Set<? extends E> edges = g.incomingEdgesOf(vertex);

        for( E e : edges ) {
            predecessors.add(e.oppositeVertex(vertex));            
        }

        return predecessors;
    }

    /**
     * Returns a list of vertices that are the successors of a specified
     * vertex. If the graph is a multigraph vertices may appear more than once
     * in the returned list.
     *
     * @param g the graph to look for successors in.
     * @param vertex the vertex to get the successors of.
     *
     * @return a list of the vertices that are the successors of the specified
     *         vertex.
     */
    public static <V, E extends DirEdge<V>> List<V> successorListOf(DirectedGraph<V, E> g,
        V vertex)
    {
        List<V> successors = new ArrayList<V>();
        Set<? extends E> edges = g.outgoingEdgesOf(vertex);

        for( E e : edges ) {
            successors.add(e.oppositeVertex(vertex));
        }
        
        return successors;
    }

    /**
     * Returns an undirected view of the specified graph. If the specified
     * graph is directed, returns an undirected view of it. If the specified
     * graph is undirected, just returns it.
     *
     * @param g the graph for which an undirected view to be returned.
     *
     * @return an undirected view of the specified graph, if it is directed, or
     *         or the specified graph itself if it is undirected.
     *
     * @throws IllegalArgumentException if the graph is neither DirectedGraph
     *                                  nor UndirectedGraph.
     *
     * @see AsUndirectedGraph
     */
    @SuppressWarnings("unchecked")
    public static <V, E extends Edge<V>> UndirectedGraph<V, Edge<V>> undirectedGraph(
        Graph<V, E> g)
    {
        if (g instanceof DirectedGraph) {
            return new AsUndirectedGraph<V,Edge<V>>((DirectedGraph)g);
        } else if (g instanceof UndirectedGraph) {
            return (UndirectedGraph<V,Edge<V>>) g;
        } else {
            throw new IllegalArgumentException(
                "Graph must be either DirectedGraph or UndirectedGraph");
        }
    }
}
