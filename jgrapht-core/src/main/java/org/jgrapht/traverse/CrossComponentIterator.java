/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Creator:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2008, by Barak Naveh and Contributors.
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
/* ---------------------------
 * CrossComponentIterator.java
 * ---------------------------
 * (C) Copyright 2003-2008, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   John V. Sichi
 *                   Christian Hammer
 *
 * $Id$
 *
 * Changes
 * -------
 * 31-Jul-2003 : Initial revision (BN);
 * 11-Aug-2003 : Adaptation to new event model (BN);
 * 31-Jan-2004 : Extracted cross-component traversal functionality (BN);
 * 04-May-2004 : Made generic (CH)
 * 07-May-2006 : Changed from List<Edge> to Set<Edge> (JVS);
 *
 */
package org.jgrapht.traverse;

import java.util.*;

import org.jgrapht.*;
import org.jgrapht.event.*;


/**
 * Provides a cross-connected-component traversal functionality for iterator
 * subclasses.
 *
 * @param <V> vertex type
 * @param <E> edge type
 * @param <D> type of data associated to seen vertices
 *
 * @author Barak Naveh
 * @since Jan 31, 2004
 */
public abstract class CrossComponentIterator<V, E, D>
    extends AbstractGraphIterator<V, E>
{
    //~ Static fields/initializers ---------------------------------------------

    private static final int CCS_BEFORE_COMPONENT = 1;
    private static final int CCS_WITHIN_COMPONENT = 2;
    private static final int CCS_AFTER_COMPONENT = 3;

    //~ Enums ------------------------------------------------------------------

    /**
     * Standard vertex visit state enumeration.
     */
    protected static enum VisitColor
    {
        /**
         * Vertex has not been returned via iterator yet.
         */
        WHITE,

        /**
         * Vertex has been returned via iterator, but we're not done with all of
         * its out-edges yet.
         */
        GRAY,

        /**
         * Vertex has been returned via iterator, and we're done with all of its
         * out-edges.
         */
        BLACK
    }

    //~ Instance fields --------------------------------------------------------

    //
    private final ConnectedComponentTraversalEvent ccFinishedEvent =
        new ConnectedComponentTraversalEvent(
            this,
            ConnectedComponentTraversalEvent.CONNECTED_COMPONENT_FINISHED);
    private final ConnectedComponentTraversalEvent ccStartedEvent =
        new ConnectedComponentTraversalEvent(
            this,
            ConnectedComponentTraversalEvent.CONNECTED_COMPONENT_STARTED);

    // TODO: support ConcurrentModificationException if graph modified
    // during iteration.
    private FlyweightEdgeEvent<V, E> reusableEdgeEvent;
    private FlyweightVertexEvent<V> reusableVertexEvent;
    private Iterator<V> vertexIterator = null;

    /**
     * Stores the vertices that have been seen during iteration and (optionally)
     * some additional traversal info regarding each vertex.
     */
    private Map<V, D> seen = new HashMap<V, D>();
    private V startVertex;
    private Specifics<V, E> specifics;

    private final Graph<V, E> graph;

    /**
     * The connected component state
     */
    private int state = CCS_BEFORE_COMPONENT;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new iterator for the specified graph. Iteration will start at
     * the specified start vertex. If the specified start vertex is <code>
     * null</code>, Iteration will start at an arbitrary graph vertex.
     *
     * @param g the graph to be iterated.
     * @param startVertex the vertex iteration to be started.
     *
     * @throws IllegalArgumentException if <code>g==null</code> or does not
     * contain <code>startVertex</code>
     */
    public CrossComponentIterator(Graph<V, E> g, V startVertex)
    {
        super();

        if (g == null) {
            throw new IllegalArgumentException("graph must not be null");
        }
        graph = g;

        specifics = createGraphSpecifics(g);
        vertexIterator = g.vertexSet().iterator();
        setCrossComponentTraversal(startVertex == null);

        reusableEdgeEvent = new FlyweightEdgeEvent<V, E>(this, null);
        reusableVertexEvent = new FlyweightVertexEvent<V>(this, null);

        if (startVertex == null) {
            // pick a start vertex if graph not empty
            if (vertexIterator.hasNext()) {
                this.startVertex = vertexIterator.next();
            } else {
                this.startVertex = null;
            }
        } else if (g.containsVertex(startVertex)) {
            this.startVertex = startVertex;
        } else {
            throw new IllegalArgumentException(
                "graph must contain the start vertex");
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * @return the graph being traversed
     */
    public Graph<V, E> getGraph()
    {
        return graph;
    }

    /**
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext()
    {
        if (startVertex != null) {
            encounterStartVertex();
        }

        if (isConnectedComponentExhausted()) {
            if (state == CCS_WITHIN_COMPONENT) {
                state = CCS_AFTER_COMPONENT;
                if (nListeners != 0) {
                    fireConnectedComponentFinished(ccFinishedEvent);
                }
            }

            if (isCrossComponentTraversal()) {
                while (vertexIterator.hasNext()) {
                    V v = vertexIterator.next();

                    if (!isSeenVertex(v)) {
                        encounterVertex(v, null);
                        state = CCS_BEFORE_COMPONENT;

                        return true;
                    }
                }

                return false;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * @see java.util.Iterator#next()
     */
    public V next()
    {
        if (startVertex != null) {
            encounterStartVertex();
        }

        if (hasNext()) {
            if (state == CCS_BEFORE_COMPONENT) {
                state = CCS_WITHIN_COMPONENT;
                if (nListeners != 0) {
                    fireConnectedComponentStarted(ccStartedEvent);
                }
            }

            V nextVertex = provideNextVertex();
            if (nListeners != 0) {
                fireVertexTraversed(createVertexTraversalEvent(nextVertex));
            }

            addUnseenChildrenOf(nextVertex);

            return nextVertex;
        } else {
            throw new NoSuchElementException();
        }
    }

    /**
     * Returns <tt>true</tt> if there are no more uniterated vertices in the
     * currently iterated connected component; <tt>false</tt> otherwise.
     *
     * @return <tt>true</tt> if there are no more uniterated vertices in the
     * currently iterated connected component; <tt>false</tt> otherwise.
     */
    protected abstract boolean isConnectedComponentExhausted();

    /**
     * Update data structures the first time we see a vertex.
     *
     * @param vertex the vertex encountered
     * @param edge the edge via which the vertex was encountered, or null if the
     * vertex is a starting point
     */
    protected abstract void encounterVertex(V vertex, E edge);

    /**
     * Returns the vertex to be returned in the following call to the iterator
     * <code>next</code> method.
     *
     * @return the next vertex to be returned by this iterator.
     */
    protected abstract V provideNextVertex();

    /**
     * Access the data stored for a seen vertex.
     *
     * @param vertex a vertex which has already been seen.
     *
     * @return data associated with the seen vertex or <code>null</code> if no
     * data was associated with the vertex. A <code>null</code> return can also
     * indicate that the vertex was explicitly associated with <code>
     * null</code>.
     */
    protected D getSeenData(V vertex)
    {
        return seen.get(vertex);
    }

    /**
     * Determines whether a vertex has been seen yet by this traversal.
     *
     * @param vertex vertex in question
     *
     * @return <tt>true</tt> if vertex has already been seen
     */
    protected boolean isSeenVertex(Object vertex)
    {
        return seen.containsKey(vertex);
    }

    /**
     * Called whenever we re-encounter a vertex. The default implementation does
     * nothing.
     *
     * @param vertex the vertex re-encountered
     * @param edge the edge via which the vertex was re-encountered
     */
    protected abstract void encounterVertexAgain(V vertex, E edge);

    /**
     * Stores iterator-dependent data for a vertex that has been seen.
     *
     * @param vertex a vertex which has been seen.
     * @param data data to be associated with the seen vertex.
     *
     * @return previous value associated with specified vertex or <code>
     * null</code> if no data was associated with the vertex. A <code>
     * null</code> return can also indicate that the vertex was explicitly
     * associated with <code>null</code>.
     */
    protected D putSeenData(V vertex, D data)
    {
        return seen.put(vertex, data);
    }

    /**
     * Called when a vertex has been finished (meaning is dependent on traversal
     * represented by subclass).
     *
     * @param vertex vertex which has been finished
     */
    protected void finishVertex(V vertex)
    {
        if (nListeners != 0) {
            fireVertexFinished(createVertexTraversalEvent(vertex));
        }
    }

    // -------------------------------------------------------------------------
    /**
     * @param <V>
     * @param <E>
     * @param g
     *
     * @return TODO Document me
     */
    static <V, E> Specifics<V, E> createGraphSpecifics(Graph<V, E> g)
    {
        if (g instanceof DirectedGraph<?, ?>) {
            return new DirectedSpecifics<V, E>((DirectedGraph<V, E>) g);
        } else {
            return new UndirectedSpecifics<V, E>(g);
        }
    }

    private void addUnseenChildrenOf(V vertex)
    {
        for (E edge : specifics.edgesOf(vertex)) {
            if (nListeners != 0) {
                fireEdgeTraversed(createEdgeTraversalEvent(edge));
            }

            if(edge instanceof Deactivateable)
            {
            	Deactivateable deactivateableEdge = (Deactivateable)edge;
            	
            	if(!deactivateableEdge.isActive())
            		continue;
            }
            
            V oppositeV = Graphs.getOppositeVertex(graph, edge, vertex);

            if (isSeenVertex(oppositeV)) {
                encounterVertexAgain(oppositeV, edge);
            } else {
                encounterVertex(oppositeV, edge);
            }
        }
    }

    private EdgeTraversalEvent<V, E> createEdgeTraversalEvent(E edge)
    {
        if (isReuseEvents()) {
            reusableEdgeEvent.setEdge(edge);

            return reusableEdgeEvent;
        } else {
            return new EdgeTraversalEvent<V, E>(this, edge);
        }
    }

    private VertexTraversalEvent<V> createVertexTraversalEvent(V vertex)
    {
        if (isReuseEvents()) {
            reusableVertexEvent.setVertex(vertex);

            return reusableVertexEvent;
        } else {
            return new VertexTraversalEvent<V>(this, vertex);
        }
    }

    private void encounterStartVertex()
    {
        encounterVertex(startVertex, null);
        startVertex = null;
    }

    //~ Inner Interfaces -------------------------------------------------------

    static interface SimpleContainer<T>
    {
        /**
         * Tests if this container is empty.
         *
         * @return <code>true</code> if empty, otherwise <code>false</code>.
         */
        public boolean isEmpty();

        /**
         * Adds the specified object to this container.
         *
         * @param o the object to be added.
         */
        public void add(T o);

        /**
         * Remove an object from this container and return it.
         *
         * @return the object removed from this container.
         */
        public T remove();
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * Provides unified interface for operations that are different in directed
     * graphs and in undirected graphs.
     */
    abstract static class Specifics<VV, EE>
    {
        /**
         * Returns the edges outgoing from the specified vertex in case of
         * directed graph, and the edge touching the specified vertex in case of
         * undirected graph.
         *
         * @param vertex the vertex whose outgoing edges are to be returned.
         *
         * @return the edges outgoing from the specified vertex in case of
         * directed graph, and the edge touching the specified vertex in case of
         * undirected graph.
         */
        public abstract Set<? extends EE> edgesOf(VV vertex);
    }

    /**
     * A reusable edge event.
     *
     * @author Barak Naveh
     * @since Aug 11, 2003
     */
    static class FlyweightEdgeEvent<VV, localE>
        extends EdgeTraversalEvent<VV, localE>
    {
        private static final long serialVersionUID = 4051327833765000755L;

        /**
         * @see EdgeTraversalEvent#EdgeTraversalEvent(Object, Edge)
         */
        public FlyweightEdgeEvent(Object eventSource, localE edge)
        {
            super(eventSource, edge);
        }

        /**
         * Sets the edge of this event.
         *
         * @param edge the edge to be set.
         */
        protected void setEdge(localE edge)
        {
            this.edge = edge;
        }
    }

    /**
     * A reusable vertex event.
     *
     * @author Barak Naveh
     * @since Aug 11, 2003
     */
    static class FlyweightVertexEvent<VV>
        extends VertexTraversalEvent<VV>
    {
        private static final long serialVersionUID = 3834024753848399924L;

        /**
         * @see VertexTraversalEvent#VertexTraversalEvent(Object, Object)
         */
        public FlyweightVertexEvent(Object eventSource, VV vertex)
        {
            super(eventSource, vertex);
        }

        /**
         * Sets the vertex of this event.
         *
         * @param vertex the vertex to be set.
         */
        protected void setVertex(VV vertex)
        {
            this.vertex = vertex;
        }
    }

    /**
     * An implementation of {@link Specifics} for a directed graph.
     */
    private static class DirectedSpecifics<VV, EE>
        extends Specifics<VV, EE>
    {
        private DirectedGraph<VV, EE> graph;

        /**
         * Creates a new DirectedSpecifics object.
         *
         * @param g the graph for which this specifics object to be created.
         */
        public DirectedSpecifics(DirectedGraph<VV, EE> g)
        {
            graph = g;
        }

        /**
         * @see CrossComponentIterator.Specifics#edgesOf(Object)
         */
        public Set<? extends EE> edgesOf(VV vertex)
        {
            return graph.outgoingEdgesOf(vertex);
        }
    }

    /**
     * An implementation of {@link Specifics} in which edge direction (if any)
     * is ignored.
     */
    private static class UndirectedSpecifics<VV, EE>
        extends Specifics<VV, EE>
    {
        private Graph<VV, EE> graph;

        /**
         * Creates a new UndirectedSpecifics object.
         *
         * @param g the graph for which this specifics object to be created.
         */
        public UndirectedSpecifics(Graph<VV, EE> g)
        {
            graph = g;
        }

        /**
         * @see CrossComponentIterator.Specifics#edgesOf(Object)
         */
        public Set<EE> edgesOf(VV vertex)
        {
            return graph.edgesOf(vertex);
        }
    }
}

// End CrossComponentIterator.java
