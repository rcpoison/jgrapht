/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Lead:  Barak Naveh (barak_naveh@users.sourceforge.net)
 *
 * (C) Copyright 2003, by Barak Naveh and Contributors.
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
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */
/* ----------
 * Graph.java
 * ----------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 24-Jul-2003 : Initial revision (BN);
 *
 */
package org._3pq.jgrapht;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * The root interface in the graph hierarchy.  A mathematical graph-theory
 * graph object <tt>G(V,E)</tt> contains a set <tt>V</tt> of vertices and a
 * set <tt>E</tt> of edges. Each edge e=(v1,v2) in E connects vertex v1 to
 * vertex v2. for more information about graphs and their related definitions
 * see <a href="http://mathworld.wolfram.com/Graph.html">
 * http://mathworld.wolfram.com/Graph.html</a>.
 * 
 * <p>
 * This library is generally follows the terminology found at: <a
 * href="http://mathworld.wolfram.com/topics/GraphTheory.html">
 * http://mathworld.wolfram.com/topics/GraphTheory.html</a>. Implementation of
 * this interface can provide simple-graphs, multigraphs, pseudographs etc.
 * The package <code>org._3pq.jgrapht.graph</code> provides a gallery of
 * abstract and concrete graph implementations.
 * </p>
 * 
 * <p>
 * This library works best when vertices represent arbitrary objects and edges
 * represent the relationships between them. While vertex instances are
 * expected to be included in more than one graph, edges are generally assumed
 * to be exclusive to one graph. The method <code>addEdge(Edge)</code>
 * provides a back-door for that assumption and a programmer must exercise
 * care if decided to use it.
 * </p>
 *
 * @author Barak Naveh
 * @version 1.0, 14/07/2003
 *
 * @since Jul 14, 2003
 */
public interface Graph {
    /**
     * Returns a list of all edges connecting source vertex to target vertex if
     * such vertices exist in this graph. If any of the vertices does not
     * exist or is <code>null</code>, returns <code>null</code>. If both
     * vertices exist but no edges found, returns an empty list.
     * 
     * <p>
     * In undirected graphs, some of the returned edges may have their source
     * and target vertices in the opposite order. In simple graphs the
     * returned list is either singleton list or empty list.
     * </p>
     *
     * @param sourceVertex source vertex of the edge.
     * @param targetVertex target vertex of the edge.
     *
     * @return a list of all edges connecting source vertex to target vertex.
     */
    public List getAllEdges( Object sourceVertex, Object targetVertex );


    /**
     * Returns an edge connecting source vertex to target vertex if such
     * vertices and such edge exist in this graph. Otherwise returns
     * <code>null</code>. If any of the specified vertices is
     * <code>null</code>  returns <code>null</code>
     * 
     * <p>
     * In undirected graphs, the returned edge may have its source and target
     * vertices in the opposite order.
     * </p>
     *
     * @param sourceVertex source vertex of the edge.
     * @param targetVertex target vertex of the edge.
     *
     * @return an edge connecting source vertex to target vertex.
     */
    public Edge getEdge( Object sourceVertex, Object targetVertex );


    /**
     * Returns the edge factory using which this graph creates new edges. The
     * edge factory is defined when the graph is constructed and must not be
     * modified.
     *
     * @return the edge factory using which this graph creates new edges.
     */
    public EdgeFactory getEdgeFactory(  );


    /**
     * Adds all of the specified edges to this graph - please read on carefully
     * and USE WITH CARE!. The behavior of this operation is undefined if the
     * specified vertex collection is modified while the operation is in
     * progress. This method will generally invoke the {@link #addEdge(Edge)}
     * method, which carries a warning. Please see that warning and make sure
     * you understand it well before you use. If in doubt, don't use this
     * method.
     *
     * @param edges the edges to be added to this graph.
     *
     * @return <tt>true</tt> if this graph changed as a result of the call
     *
     * @throws NullPointerException if the specified edges contains one or more
     *         null edges, or if the specified vertex collection is
     *         <tt>null</tt>.
     *
     * @see #addVertex(Object)
     */
    public boolean addAllEdges( Collection edges );


    /**
     * Adds all of the specified vertices to this graph. The behavior of this
     * operation is undefined if the specified vertex collection is modified
     * while the operation is in progress.
     *
     * @param vertices the vertices to be added to this graph.
     *
     * @return <tt>true</tt> if this graph changed as a result of the call
     *
     * @throws NullPointerException if the specified vertices contains one or
     *         more null vertices, or if the specified vertex collection is
     *         <tt>null</tt>.
     *
     * @see #addVertex(Object)
     */
    public boolean addAllVertices( Collection vertices );


    /**
     * Creates a new edge in this graph, going from the source vertex to the
     * target vertex, and returns the created edge. Some graphs do not allow
     * edge-multiplicity. In such cases, if the graph already contains an edge
     * from the specified source to the specified target, than this method
     * does not change the graph and returns <code>null</code>.
     * 
     * <p>
     * The source and target vertices must be already in this graph. If they
     * are not found in graph IllegalArgumentException is thrown.
     * </p>
     * 
     * <p>
     * This method creates the new edge <code>e</code> using the
     * <code>EdgeFactory</code>. For the new edge to be added <code>e</code>
     * must <i>not</i> be equal to any other edge the graph (even if the graph
     * allows edge-multiplicity). More formally, the graph must not contain
     * any edge <code>e2</code> such that <code>e2.equals(e)</code>. If such
     * <code>e2</code> is found then the newly created edge <code>e</code> is
     * abandoned, the  method leaves this graph unchanged returns
     * <code>null</code>.
     * </p>
     *
     * @param sourceVertex source vertex of the edge.
     * @param targetVertex target vertex of the edge.
     *
     * @return The new edge if created, or <code>null</code> if such edge
     *         already exists or for some reason was not created.
     *
     * @throws IllegalArgumentException if source or target vertices are not
     *         found in the graph.
     * @throws NullPointerException if any of the specified vertices is
     *         <code>null</code>.
     *
     * @see #getEdgeFactory()
     * @see EdgeFactory
     */
    public Edge addEdge( Object sourceVertex, Object targetVertex );


    /**
     * Adds the specified edge to this graph - please read on carefully and USE
     * WITH CARE!. More formally, adds the specified edge, <code>e</code>, to
     * this graph if this graph contains no edge <code>e2</code> such that
     * <code>e2.equals(e)</code>. If this graph already contains such edge,
     * the call leaves this graph unchanged and returns <tt>false</tt>. If the
     * edge was added to the graph, returns <code>true</code>.
     * 
     * <p>
     * Some graphs do not allow edge-multiplicity. In such cases, if the graph
     * already contains an edge going from the source vertex of the specified
     * edge to the target vertex of the specified edge,  this method does not
     * change the graph and returns <code>false</code>.
     * </p>
     * 
     * <p>
     * The source and target vertices of the specified edge must already be in
     * this graph. If this is not the case, IllegalArgumentException is
     * thrown. The edge must also be assignment compatible with the class of
     * the edges produced by the edge factory of this graph. If this is not
     * the case ClassCastException is thrown.
     * </p>
     * 
     * <p>
     * Using this method is generally NOT RECOMMENDED - if in doubt don't use.
     * It allows trickery such as adding the same edge instance into two
     * graphs. Changing a property of the edge, such as weight, in one graph
     * will be reflected in the other graph. Such behavior is generally
     * <i>not</i> desired for edges (although it <i>is</i> desired for
     * vertices).
     * </p>
     *
     * @param e edge to be added to this graph.
     *
     * @return <tt>true</tt> if this graph did not already contain the
     *         specified edge.
     *
     * @throws IllegalArgumentException if source or target vertices of
     *         specified edge are not found in this graph.
     * @throws ClassCastException if the specified edge is not assignment
     *         compatible with the class of edges produced by the edge factory
     *         of this graph.
     * @throws NullPointerException if the specified edge is <code>null</code>.
     *
     * @see #addEdge(Object, Object)
     * @see #getEdgeFactory()
     * @see EdgeFactory
     */
    public boolean addEdge( Edge e );


    /**
     * Adds the specified vertex to this graph if not already present. More
     * formally, adds the specified vertex, <code>v</code>, to this graph if
     * this graph contains no vertex <code>u</code> such that
     * <code>u.equals(v)</code>. If this graph already contains such vertex,
     * the call leaves this graph unchanged and returns <tt>false</tt>. In
     * combination with the restriction on constructors, this ensures that
     * graphs never contain duplicate vertices.
     *
     * @param v vertex to be added to this graph.
     *
     * @return <tt>true</tt> if this graph did not already contain the
     *         specified vertex.
     *
     * @throws NullPointerException if the specified vertex is
     *         <code>null</code>.
     */
    public boolean addVertex( Object v );


    /**
     * Returns <tt>true</tt> if and only if this graph contains an edge going
     * from the source vertex to the target vertex. In undirected graphs the
     * same result is obtained when source and target are inverted. If any of
     * the specified vertices does not exist in the graph, or if is
     * <code>null</code>, returns <code>false</code>.
     *
     * @param sourceVertex source vertex of the edge.
     * @param targetVertex target vertex of the edge.
     *
     * @return <tt>true</tt> if this graph contains the specified edge.
     */
    public boolean containsEdge( Object sourceVertex, Object targetVertex );


    /**
     * Returns <tt>true</tt> if this graph contains the specified edge.  More
     * formally, returns <tt>true</tt> if and only if this graph contains an
     * edge <code>e2</code> such that <code>e.equals(e2)</code>. If the
     * specified edge is <code>null</code> returns <code>false</code>.
     *
     * @param e edge whose presence in this graph is to be tested.
     *
     * @return <tt>true</tt> if this graph contains the specified edge.
     */
    public boolean containsEdge( Edge e );


    /**
     * Returns <tt>true</tt> if this graph contains the specified vertex.  More
     * formally, returns <tt>true</tt> if and only if this graph contains a
     * vertex <code>u</code> such that <code>u.equals(v)</code>. If the
     * specified vertex is <code>null</code> returns <code>false</code>.
     *
     * @param v vertex whose presence in this graph is to be tested.
     *
     * @return <tt>true</tt> if this graph contains the specified vertex.
     */
    public boolean containsVertex( Object v );


    /**
     * Returns a set of the edges contained in this graph. The set is backed by
     * the graph, so changes to the graph are reflected in the set. If the
     * graph is modified while an iteration over the set is in progress, the
     * results of the iteration are undefined.
     *
     * @return a set of the edges contained in this graph.
     */
    public Set edgeSet(  );


    /**
     * Returns a list of all edges touching the specified vertex. If not edges
     * are  touching the specified vertex returns an empty list.
     *
     * @param vertex the vertex for which a list of touching edges to be
     *        returned.
     *
     * @return a list of all edges touching the specified vertex.
     */
    public List edgesOf( Object vertex );


    /**
     * Compares the specified object with this graph object for equality.
     * 
     * <p>
     * In general, two graphs are equal if they are of the same class and their
     * vertex set and edge set are equal respectively. Thus, it is NOT CHEAP
     * to have "value comparison" in place of the default <tt>Object</tt>'s
     * "reference comparison". Any class that overrides the
     * <tt>Object.equals</tt> method must also override the
     * <tt>Object.hashCode</tt> method in order to satisfy the general
     * contract for the <tt>Object.hashCode</tt>method. In particular,
     * <tt>o1.equals(o2)</tt> must imply
     * <tt>o1.hashCode()==o2.hashCode()</tt>. This results in a
     * <tt>hashCode()</tt> method that is also NON-CHEAP, which, in turn,
     * makes it inefficient to aggregate graphs into <tt>Set</tt>s,
     * <tt>List</tt>s, <tt>HashMap</tt>s, etc.
     * </p>
     * 
     * <p>
     * The simplest and perhaps the better course of action is to rely on
     * <tt>Object.equals</tt> and to write a local code for graph comparison
     * if such is needed.  Programmers who wish, in spite of the above, to
     * override the <tt>Object.equals</tt> should exercise care and be aware
     * of the performance consequences.
     * </p>
     *
     * @see Object#equals(java.lang.Object)
     */
    public boolean equals( Object o );


    /**
     * @see #equals(Object)
     */
    public int hashCode(  );


    /**
     * Removes all the edges in this graph that are also contained in the
     * specified edge collection.  After this call returns, this graph will
     * contain no edges in common with the specified edges.
     *
     * @param edges edges to be removed from this graph.
     *
     * @return <tt>true</tt> if this graph changed as a result of the call
     *
     * @throws NullPointerException if the specified edge collection is
     *         <tt>null</tt>.
     *
     * @see #removeEdge(Edge)
     * @see #containsEdge(Edge)
     */
    public boolean removeAllEdges( Collection edges );


    /**
     * Removes all the edges going from the specified source vertex to the
     * specified target vertex, and returns a list of all removed edges.
     * Returns <code>null</code> if any of the specified vertices does exist
     * in the graph. If both vertices exist but no edge found, returns an
     * empty list.
     *
     * @param sourceVertex source vertex of the edge.
     * @param targetVertex target vertex of the edge.
     *
     * @return The removed edge, or <code>null</code> if no edge removed.
     */
    public List removeAllEdges( Object sourceVertex, Object targetVertex );


    /**
     * Removes all the vertices in this graph that are also contained in the
     * specified vertex collection.  After this call returns, this graph will
     * contain no vertices in common with the specified vertices.
     *
     * @param vertices vertices to be removed from this graph.
     *
     * @return <tt>true</tt> if this graph changed as a result of the call
     *
     * @throws NullPointerException if the specified vertex collection is
     *         <tt>null</tt>.
     *
     * @see #removeVertex(Object)
     * @see #containsVertex(Object)
     */
    public boolean removeAllVertices( Collection vertices );


    /**
     * Removes an edge going from source vertex to target vertex, if such
     * vertices and such edge exist in this graph. Returns the edge if removed
     * or <code>null</code> otherwise.
     *
     * @param sourceVertex source vertex of the edge.
     * @param targetVertex target vertex of the edge.
     *
     * @return The removed edge, or <code>null</code> if no edge removed.
     */
    public Edge removeEdge( Object sourceVertex, Object targetVertex );


    /**
     * Removes the specified edge from the graph. Removes the specified edge
     * from this graph if it is present. More formally, removes an edge
     * <code>e2</code> such that <code>e2.equals(e)</code>, if the graph
     * contains such edge. Returns <tt>true</tt> if the graph contained the
     * specified edge. (The graph will not contain the specified edge once the
     * call returns).
     * 
     * <p>
     * If the specified edge is <code>null</code> returns <code>false</code>.
     * </p>
     *
     * @param e edge to be removed from this graph, if present.
     *
     * @return <code>true</code> if and only if the graph contained the
     *         specified edge.
     */
    public boolean removeEdge( Edge e );


    /**
     * Removes the specified vertex from this graph if it is present.  More
     * formally, removes a vertex <code>u</code> such that
     * <code>u.equals(v)</code>, if the graph contains such vertex. Returns
     * <tt>true</tt> if the graph contained the specified vertex. (The graph
     * will not contain the specified vertex once the call returns).
     * 
     * <p>
     * If the specified vertex is <code>null</code> returns <code>false</code>.
     * </p>
     *
     * @param v vertex to be removed from this graph, if present.
     *
     * @return true if the graph contained the specified vertex.
     */
    public boolean removeVertex( Object v );


    /**
     * Returns a set of the vertices contained in this graph. The set is backed
     * by the graph, so changes to the graph are reflected in the set. If the
     * graph is modified while an iteration over the set is in progress, the
     * results of the iteration are undefined.
     *
     * @return a set view of the vertices contained in this graph.
     */
    public Set vertexSet(  );
}
