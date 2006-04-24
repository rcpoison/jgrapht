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
/* -------------
 * Subgraph.java
 * -------------
 * (C) Copyright 2003-2004, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   Christian Hammer
 *
 * $Id$
 *
 * Changes
 * -------
 * 24-Jul-2003 : Initial revision (BN);
 * 26-Jul-2003 : Accurate constructors to avoid casting problems (BN);
 * 10-Aug-2003 : Adaptation to new event model (BN);
 * 23-Oct-2003 : Allowed non-listenable graph as base (BN);
 * 07-Feb-2004 : Enabled serialization (BN);
 * 11-Mar-2004 : Made generic (CH);
 * 15-Mar-2004 : Integrity is now checked using Maps (CH);
 * 20-Mar-2004 : Cancelled verification of element identity to base graph (BN);
 * 21-Sep-2004 : Added induced subgraph
 *
 */
package org.jgrapht.graph;

import java.io.*;

import java.util.*;

import org.jgrapht.*;
import org.jgrapht.event.*;


/**
 * A subgraph is a graph that has a subset of vertices and a subset of edges
 * with respect to some base graph. More formally, a subgraph G(V,E) that is
 * based on a base graph Gb(Vb,Eb) satisfies the following <b><i>subgraph
 * property</i></b>: V is a subset of Vb and E is a subset of Eb. Other than
 * this property, a subgraph is a graph with any respect and fully complies
 * with the <code>Graph</code> interface.
 *
 * <p>If the base graph is a {@link org.jgrapht.ListenableGraph}, the
 * subgraph listens on the base graph and guarantees the subgraph property. If
 * an edge or a vertex is removed from the base graph, it is automatically
 * removed from the subgraph. Subgraph listeners are informed on such removal
 * only if it results in a cascaded removal from the subgraph. If the subgraph
 * has been created as an induced subgraph it also keeps track of edges being
 * added to its vertices. If  vertices are added to the base graph, the
 * subgraph remains unaffected.</p>
 *
 * <p>If the base graph is <i>not</i> a ListenableGraph, then the subgraph
 * property cannot be guaranteed. If edges or vertices are removed from the
 * base graph, they are <i>not</i> removed from the subgraph.</p>
 *
 * <p>Modifications to Subgraph are allowed as long as the subgraph property is
 * maintained. Addition of vertices or edges are allowed as long as they also
 * exist in the base graph. Removal of vertices or edges is always allowed. The
 * base graph is <i>never</i> affected by any modification made to the
 * subgraph.</p>
 *
 * <p>A subgraph may provide a "live-window" on a base graph, so that changes
 * made to its vertices or edges are immediately reflected in the base graph,
 * and vice versa. For that to happen, vertices and edges added to the subgraph
 * must be <i>identical</i> (that is, reference-equal and not only value-equal)
 * to their respective ones in the base graph. Previous versions of this class
 * enforced such identity, at a severe performance cost. Currently it is no
 * longer enforced. If you want to achieve a "live-window"functionality, your
 * safest tactics would be to NOT override the <code>equals()</code> methods of
 * your vertices and edges. If you use a class that has already overridden the
 * <code>equals()</code> method, such as <code>String</code>, than you can use
 * a wrapper around it, or else use it directly but exercise a great care to
 * avoid having different-but-equal instances in the subgraph and the base
 * graph.</p>
 *
 * <p>This graph implementation guarantees deterministic vertex and edge set
 * ordering (via {@link LinkedHashSet}).</p>
 *
 * @author Barak Naveh
 * @see org.jgrapht.Graph
 * @see java.util.Set
 * @since Jul 18, 2003
 * 
 * <p>
 * TODO hb 27-Nov-05: Subgraph features the code for a directed graph without specifying the interface.
 * This makes types/typecasting problematic. The class does not even test
 * whether a directed graph is stored in m_base when executing direction-related methods.
 * My guess is that all direction-related methods should move to DirectedSubgraph.
 */
public class Subgraph<V, E extends Edge<V>> extends AbstractGraph<V, E>
    implements Serializable
{
    //~ Static fields/initializers --------------------------------------------
    private static final long serialVersionUID = 3208313055169665387L;
    private static final String NO_SUCH_EDGE_IN_BASE =
        "no such edge in base graph";
    private static final String NO_SUCH_VERTEX_IN_BASE =
        "no such vertex in base graph";

    //~ Instance fields -------------------------------------------------------

    //
    Set<E> m_edgeSet = new LinkedHashSet<E>(); // friendly to improve performance
    Set<V> m_vertexSet = new LinkedHashSet<V>(); // friendly to improve
                                              // performance

    //
    private transient Set<E> m_unmodifiableEdgeSet = null;
    private transient Set<V> m_unmodifiableVertexSet = null;
    private Graph<V, E> m_base;
    private boolean m_isInduced = false;
    private boolean m_verifyIntegrity = true;

    //~ Constructors ----------------------------------------------------------

    /**
     * Creates a new Subgraph.
     *
     * @param base the base (backing) graph on which the subgraph will be
     *             based.
     * @param vertexSubset vertices to include in the subgraph. If <code>
     *                     null</code> then all vertices are included.
     * @param edgeSubset edges to in include in the subgraph. If <code>
     *                   null</code> then all the edges whose vertices found in
     *                   the graph are included.
     */
    public Subgraph(Graph<V, E> base, Set<V> vertexSubset, Set<E> edgeSubset)
    {
        super();

        m_base = base;

        if (m_base instanceof ListenableGraph) {
            ((ListenableGraph<V, E>) m_base).addGraphListener(new BaseGraphListener());
        }

        addVerticesUsingFilter(base.vertexSet(), vertexSubset);
        addEdgesUsingFilter(base.edgeSet(), edgeSubset);
    }

    /**
     * Creates a new induced Subgraph. The subgraph will keep track of edges
     * being added to its vertex subset as well as deletion of edges and
     * vertices. If base it not listenable, this is identical to the call
     * Subgraph(base, vertexSubset, null) .
     *
     * @param base the base (backing) graph on which the subgraph will be
     *             based.
     * @param vertexSubset vertices to include in the subgraph. If <code>
     *                     null</code> then all vertices are included.
     */
    public Subgraph(Graph<V, E> base, Set<V> vertexSubset)
    {
        this(base, vertexSubset, null);

        m_isInduced = true;
    }

    //~ Methods ---------------------------------------------------------------

    /**
     * @see org.jgrapht.Graph#getAllEdges(Object, Object)
     */
    public List<E> getAllEdges(V sourceVertex, V targetVertex)
    {
        List<E> edges = null;

        if (containsVertex(sourceVertex) && containsVertex(targetVertex)) {
            edges = new ArrayList<E>();

            List<E> baseEdges = m_base.getAllEdges(sourceVertex, targetVertex);

            for (Iterator<E> iter = baseEdges.iterator(); iter.hasNext();) {
                E e = iter.next();

                if (m_edgeSet.contains(e)) { // add if subgraph also contains
                                             // it
                    edges.add(e);
                }
            }
        }

        return edges;
    }

    /**
     * @see org.jgrapht.Graph#getEdge(Object, Object)
     */
    public E getEdge(V sourceVertex, V targetVertex)
    {
        List<E> edges = getAllEdges(sourceVertex, targetVertex);

        if ((edges == null) || edges.isEmpty()) {
            return null;
        } else {
            return edges.get(0);
        }
    }

    /**
     * @see org.jgrapht.Graph#getEdgeFactory()
     */
    public EdgeFactory<V, E> getEdgeFactory()
    {
        return m_base.getEdgeFactory();
    }

    /**
     * Sets the the check integrity flag.
     *
     * @param verifyIntegrity
     *
     * @see Subgraph
     * @deprecated method will be deleted in future versions. verifyIntegrity
     *             flag has no effect now.
     */
    public void setVerifyIntegrity(boolean verifyIntegrity)
    {
        m_verifyIntegrity = verifyIntegrity;
    }

    /**
     * Returns the value of the verifyIntegrity flag.
     *
     * @return the value of the verifyIntegrity flag.
     *
     * @deprecated method will be deleted in future versions.
     */
    public boolean isVerifyIntegrity()
    {
        return m_verifyIntegrity;
    }

    /**
     * @see org.jgrapht.Graph#addEdge(Object, Object)
     */
    public E addEdge(V sourceVertex, V targetVertex)
    {
        assertVertexExist(sourceVertex);
        assertVertexExist(targetVertex);

        if (!m_base.containsEdge(sourceVertex, targetVertex)) {
            throw new IllegalArgumentException(NO_SUCH_EDGE_IN_BASE);
        }

        List<E> edges = m_base.getAllEdges(sourceVertex, targetVertex);

        for (Iterator<E> iter = edges.iterator(); iter.hasNext();) {
            E e = iter.next();

            if (!containsEdge(e)) {
                m_edgeSet.add(e);

                return e;
            }
        }

        return null;
    }

    /**
     * Adds the specified edge to this subgraph.
     *
     * @param e the edge to be added.
     *
     * @return <code>true</code> if the edge was added, otherwise <code>
     *         false</code>.
     *
     * @throws NullPointerException
     * @throws IllegalArgumentException
     *
     * @see Subgraph
     * @see org.jgrapht.Graph#addEdge(Edge)
     */
    public boolean addEdge(E e)
    {
        if (e == null) {
            throw new NullPointerException();
        }

        if (!m_base.containsEdge(e)) {
            throw new IllegalArgumentException(NO_SUCH_EDGE_IN_BASE);
        }

        assertVertexExist(e.getSource());
        assertVertexExist(e.getTarget());

        if (containsEdge(e)) {
            return false;
        } else {
            m_edgeSet.add(e);

            return true;
        }
    }

    /**
     * Adds the specified vertex to this subgraph.
     *
     * @param v the vertex to be added.
     *
     * @return <code>true</code> if the vertex was added, otherwise <code>
     *         false</code>.
     *
     * @throws NullPointerException
     * @throws IllegalArgumentException
     *
     * @see Subgraph
     * @see org.jgrapht.Graph#addVertex(Object)
     */
    public boolean addVertex(V v)
    {
        if (v == null) {
            throw new NullPointerException();
        }

        if (!m_base.containsVertex(v)) {
            throw new IllegalArgumentException(NO_SUCH_VERTEX_IN_BASE);
        }

        if (containsVertex(v)) {
            return false;
        } else {
            m_vertexSet.add(v);

            return true;
        }
    }

    /**
     * @see org.jgrapht.Graph#containsEdge(Edge)
     */
    public boolean containsEdge(Edge e)
    {
        return m_edgeSet.contains(e);
    }

    /**
     * @see org.jgrapht.Graph#containsVertex(Object)
     */
    public boolean containsVertex(V v)
    {
        return m_vertexSet.contains(v);
    }

    /**
     * @see UndirectedGraph#degreeOf(Object)
     */
    public int degreeOf(V vertex) {
        assertVertexExist(vertex);

        // TODO hb 27-Nov-05: Check/understand this sophistication
        // could the intend be to throw a ClassCastException
        // for non-directed graphs?
        // sophisticated way to check runtime class of base ;-)
        ((UndirectedGraph<V,E>) m_base).degreeOf(vertex);

        int degree = 0;

        for (E e : m_base.edgesOf(vertex)) {
            if (containsEdge(e)) {
                degree++;

                if (e.getSource().equals(e.getTarget())) {
                    degree++;
                }
            }
        }

        return degree;
    }

    /**
     * @see org.jgrapht.Graph#edgeSet()
     */
    public Set<E> edgeSet()
    {
        if (m_unmodifiableEdgeSet == null) {
            m_unmodifiableEdgeSet = Collections.unmodifiableSet(m_edgeSet);
        }

        return m_unmodifiableEdgeSet;
    }

    /**
     * @see org.jgrapht.Graph#edgesOf(Object)
     */
    public List<E> edgesOf(V vertex)
    {
        assertVertexExist(vertex);

        ArrayList<E> edges = new ArrayList<E>();
        List<E> baseEdges = m_base.edgesOf(vertex);

        for (E e : baseEdges) {
            if (containsEdge(e)) {
                edges.add(e);
            }
        }

        return edges;
    }

    /**
     * @see DirectedGraph#inDegreeOf(Object)
     */
    public int inDegreeOf(V vertex)
    {
        assertVertexExist(vertex);

        int degree = 0;

        // XXX hb 27-Nov-05: I have no clue why this cast works without raising a warning
        for (DirEdge e : ((DirectedGraph<V, ? extends E>) m_base).incomingEdgesOf(vertex)) {
            if (containsEdge(e)) {
                degree++;
            }
        }

        return degree;
    }

    /**
     * @see DirectedGraph#incomingEdgesOf(Object)
     */
    public List<E> incomingEdgesOf(V vertex)
    {
        assertVertexExist(vertex);

        ArrayList<E> edges = new ArrayList<E>();
        List<E> baseEdges =
            ((DirectedGraph<V, E>) m_base).incomingEdgesOf(vertex);

        for (E e : baseEdges) {
            if (containsEdge(e)) {
                edges.add(e);
            }
        }

        return edges;
    }

    /**
     * @see DirectedGraph#outDegreeOf(Object)
     */
    public int outDegreeOf(V vertex)
    {
        assertVertexExist(vertex);

        int degree = 0;

        // XXX hb 27-Nov-05: I have no clue why this cast works without raising a warning
        for (E e : ((DirectedGraph<V, E>) m_base).outgoingEdgesOf(vertex)) {
            if (containsEdge(e)) {
                degree++;
            }
        }

        return degree;
    }

    /**
     * @see DirectedGraph#outgoingEdgesOf(Object)
     */
    public List<E> outgoingEdgesOf(V vertex)
    {
        assertVertexExist(vertex);

        ArrayList<E> edges = new ArrayList<E>();
        // XXX hb 27-Nov-05: I have no clue why this cast works without raising a warning
        List<? extends E> baseEdges =
            ((DirectedGraph<V, E>) m_base).outgoingEdgesOf(vertex);

        for (E e : baseEdges) {
            if (containsEdge(e)) {
                edges.add(e);
            }
        }

        return edges;
    }

    /**
     * @see org.jgrapht.Graph#removeEdge(Edge)
     */
    public boolean removeEdge(E e)
    {
        return m_edgeSet.remove(e);
    }

    /**
     * @see org.jgrapht.Graph#removeEdge(Object, Object)
     */
    public E removeEdge(V sourceVertex, V targetVertex)
    {
        E e = getEdge(sourceVertex, targetVertex);

        return m_edgeSet.remove(e) ? e : null;
    }

    /**
     * @see org.jgrapht.Graph#removeVertex(Object)
     */
    public boolean removeVertex(V v)
    {
        // If the base graph does NOT contain v it means we are here in
        // response to removal of v from the base. In such case we don't need
        // to remove all the edges of v as they were already removed.
        if (containsVertex(v) && m_base.containsVertex(v)) {
            removeAllEdges(edgesOf(v));
        }

        return m_vertexSet.remove(v);
    }

    /**
     * @see org.jgrapht.Graph#vertexSet()
     */
    public Set<V> vertexSet()
    {
        if (m_unmodifiableVertexSet == null) {
            m_unmodifiableVertexSet = Collections.unmodifiableSet(m_vertexSet);
        }

        return m_unmodifiableVertexSet;
    }

    private void addEdgesUsingFilter(Set<E> edgeSet, Set<E> filter)
    {
        E e;
        boolean containsVertices;
        boolean edgeIncluded;

        for (Iterator<E> iter = edgeSet.iterator(); iter.hasNext();) {
            e = iter.next();

            containsVertices =
                containsVertex(e.getSource())
                && containsVertex(e.getTarget());

            // note the use of short circuit evaluation
            edgeIncluded = (filter == null) || filter.contains(e);

            if (containsVertices && edgeIncluded) {
                addEdge(e);
            }
        }
    }

    private void addVerticesUsingFilter(Set<V> vertexSet, Set<V> filter)
    {
        V v;

        for (Iterator<V> iter = vertexSet.iterator(); iter.hasNext();) {
            v = iter.next();

            // note the use of short circuit evaluation
            if ((filter == null) || filter.contains(v)) {
                addVertex(v);
            }
        }
    }

    //~ Inner Classes ---------------------------------------------------------

    /**
     * An internal listener on the base graph.
     *
     * @author Barak Naveh
     * @since Jul 20, 2003
     */
    private class BaseGraphListener implements GraphListener<V, E>,
        Serializable
    {
        private static final long serialVersionUID = 4343535244243546391L;

        /**
         * @see GraphListener#edgeAdded(GraphEdgeChangeEvent)
         */
        public void edgeAdded(GraphEdgeChangeEvent<V, E> e)
        {
            if (m_isInduced) {
                addEdge(e.getEdge());
            }
        }

        /**
         * @see GraphListener#edgeRemoved(GraphEdgeChangeEvent)
         */
        public void edgeRemoved(GraphEdgeChangeEvent<V, E> e)
        {
            E edge = e.getEdge();

            removeEdge(edge);
        }

        /**
         * @see VertexSetListener#vertexAdded(GraphVertexChangeEvent)
         */
        public void vertexAdded(GraphVertexChangeEvent<V> e)
        {
            // we don't care
        }

        /**
         * @see VertexSetListener#vertexRemoved(GraphVertexChangeEvent)
         */
        public void vertexRemoved(GraphVertexChangeEvent<V> e)
        {
            V vertex = e.getVertex();

            removeVertex(vertex);
        }
    }
}
