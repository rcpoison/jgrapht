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
/* -------------------
 * GraphDelegator.java
 * -------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   Christian Hammer
 *
 * $Id$
 *
 * Changes
 * -------
 * 24-Jul-2003 : Initial revision (BN);
 * 11-Mar-2004 : Made generic (CH);
 *
 */
package org._3pq.jgrapht.graph;

import java.io.*;

import java.util.*;

import org._3pq.jgrapht.*;


/**
 * A graph backed by the the graph specified at the constructor, which
 * delegates all its methods to the backing graph. Operations on this graph
 * "pass through" to the to the backing graph. Any modification made to this
 * graph or the backing graph is reflected by the other.
 *
 * <p>This graph does <i>not</i> pass the hashCode and equals operations
 * through to the backing graph, but relies on <tt>Object</tt>'s <tt>
 * equals</tt> and <tt>hashCode</tt> methods.</p>
 *
 * <p>This class is mostly used as a base for extending subclasses.</p>
 *
 * @author Barak Naveh
 * @since Jul 20, 2003
 */
public class GraphDelegator<V, E extends Edge<V>> extends AbstractGraph<V, E>
    implements Graph<V, E>, Serializable
{

    //~ Static fields/initializers --------------------------------------------

    private static final long serialVersionUID = 3257005445226181425L;

    //~ Instance fields -------------------------------------------------------

    /**
     * The graph to which operations are delegated.
     */
    private Graph<V, E> m_delegate;

    //~ Constructors ----------------------------------------------------------

    /**
     * Constructor for GraphDelegator.
     *
     * @param g the backing graph (the delegate).
     *
     * @throws IllegalArgumentException iff <code>g==null</code>
     */
    public GraphDelegator(Graph<V, E> g)
    {
        super();

        if (g == null) {
            throw new IllegalArgumentException("g must not be null.");
        }

        m_delegate = g;
    }

    //~ Methods ---------------------------------------------------------------

    /**
     * @see Graph#getAllEdges(Object, Object)
     */
    public List<E> getAllEdges(V sourceVertex, V targetVertex)
    {
        return m_delegate.getAllEdges(sourceVertex, targetVertex);
    }

    /**
     * @see Graph#getEdge(Object, Object)
     */
    public E getEdge(V sourceVertex, V targetVertex)
    {
        return m_delegate.getEdge(sourceVertex, targetVertex);
    }

    /**
     * @see Graph#getEdgeFactory()
     */
    public EdgeFactory<V, E> getEdgeFactory()
    {
        return m_delegate.getEdgeFactory();
    }

    /**
     * @see Graph#addEdge(Edge)
     */
    public boolean addEdge(E e)
    {
        return m_delegate.addEdge(e);
    }

    /**
     * @see Graph#addEdge(Object, Object)
     */
    public E addEdge(V sourceVertex, V targetVertex)
    {
        return m_delegate.addEdge(sourceVertex, targetVertex);
    }

    /**
     * @see Graph#addVertex(Object)
     */
    public boolean addVertex(V v)
    {
        return m_delegate.addVertex(v);
    }

    /**
     * @see Graph#containsEdge(Edge)
     */
    public boolean containsEdge(Edge e)
    {
        return m_delegate.containsEdge(e);
    }

    /**
     * @see Graph#containsVertex(Object)
     */
    public boolean containsVertex(V v)
    {
        return m_delegate.containsVertex(v);
    }

    /**
     * @see UndirectedGraph#degreeOf(Object)
     */
    public int degreeOf(V vertex)
    {
        return ((UndirectedGraph<V,E>) m_delegate).degreeOf(vertex);
    }

    /**
     * @see Graph#edgeSet()
     */
    public Set<E> edgeSet()
    {
        return m_delegate.edgeSet();
    }

    /**
     * @see Graph#edgesOf(Object)
     */
    public List<E> edgesOf(V vertex)
    {
        return m_delegate.edgesOf(vertex);
    }

    /**
     * @see DirectedGraph#inDegreeOf(Object)
     */
    public int inDegreeOf(V vertex)
    {
        return ((DirectedGraph<V,? extends E>) m_delegate).inDegreeOf(vertex);
    }

    /**
     * @see DirectedGraph#incomingEdgesOf(Object)
     */
    public List<E> incomingEdgesOf(V vertex)
    {
        return ((DirectedGraph<V,E>) m_delegate).incomingEdgesOf(vertex);
    }

    /**
     * @see DirectedGraph#outDegreeOf(Object)
     */
    public int outDegreeOf(V vertex)
    {
        return ((DirectedGraph<V,? extends E>) m_delegate).outDegreeOf(vertex);
    }

    /**
     * @see DirectedGraph#outgoingEdgesOf(Object)
     */
    public List<E> outgoingEdgesOf(V vertex)
    {
        return ((DirectedGraph<V,E>) m_delegate).outgoingEdgesOf(vertex);
    }

    /**
     * @see Graph#removeEdge(Edge)
     */
    public boolean removeEdge(E e)
    {
        return m_delegate.removeEdge(e);
    }

    /**
     * @see Graph#removeEdge(Object, Object)
     */
    public E removeEdge(V sourceVertex, V targetVertex)
    {
        return m_delegate.removeEdge(sourceVertex, targetVertex);
    }

    /**
     * @see Graph#removeVertex(Object)
     */
    public boolean removeVertex(V v)
    {
        return m_delegate.removeVertex(v);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return m_delegate.toString();
    }

    /**
     * @see Graph#vertexSet()
     */
    public Set<V> vertexSet()
    {
        return m_delegate.vertexSet();
    }
}
