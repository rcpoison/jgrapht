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
/* -------------------------
 * KShortestPathsIterator.java
 * -------------------------
 * (C) Copyright 2007-2008, by France Telecom
 *
 * Original Author:  Guillaume Boulmier and Contributors.
 * Contributor(s):   John V. Sichi
 *
 * $Id$
 *
 * Changes
 * -------
 * 05-Jun-2007 : Initial revision (GB);
 * 05-Jul-2007 : Added support for generics (JVS);
 *
 */
package org.jgrapht.alg;

import java.util.*;

import org.jgrapht.*;


/**
 * Helper class for {@link KShortestPaths}.
 *
 * @author Guillaume Boulmier
 * @since July 5, 2007
 */
class KShortestPathsIterator<V, E>
    implements Iterator<Set<V>>
{
    // ~ Instance fields
    // --------------------------------------------------------

    //~ Instance fields --------------------------------------------------------

    /**
     * End vertex.
     */
    private V endVertex;

    /**
     * Graph on which shortest paths are searched.
     */
    private Graph<V, E> graph;

    /**
     * Number of paths stored at each end vertex.
     */
    private int k;

    /**
     * Vertices whose ranking shortest paths have been modified during the
     * previous pass.
     */
    private Set<V> prevImprovedVertices;

    /**
     * Key = vertex, value = <code>RankingPathElementList</code>.
     */
    private Map<V, RankingPathElementList<V, E>> prevSeenDataContainer;

    /**
     * Stores the vertices that have been seen during iteration and (optionally)
     * some additional traversal info regarding each vertex. Key = vertex, value
     * = <code>RankingPathElementList</code> list of calculated paths.
     */
    private Map<V, RankingPathElementList<V, E>> seenDataContainer;

    /**
     * Start vertex.
     */
    private V startVertex;

    private boolean startVertexEncountered;

    //~ Constructors -----------------------------------------------------------

    // ~ Constructors
    // -----------------------------------------------------------

    /**
     * @param graph graph on which shortest paths are searched.
     * @param startVertex start vertex of the calculated paths.
     * @param endVertex end vertex of the calculated paths.
     * @param maxSize number of paths stored at end vertex of the graph.
     */
    public KShortestPathsIterator(
        Graph<V, E> graph,
        V startVertex,
        V endVertex,
        int maxSize)
    {
        assertKShortestPathsIterator(graph, startVertex);

        this.graph = graph;
        this.startVertex = startVertex;
        this.endVertex = endVertex;

        this.k = maxSize;

        this.seenDataContainer = new HashMap<V, RankingPathElementList<V, E>>();
        this.prevSeenDataContainer =
            new HashMap<V, RankingPathElementList<V, E>>();

        this.prevImprovedVertices = new HashSet<V>();
    }

    //~ Methods ----------------------------------------------------------------

    // ~ Methods
    // ----------------------------------------------------------------

    /**
     * @return <code>true</code> if at least one path has been improved during
     * the previous pass, <code>false</code> otherwise.
     */
    public boolean hasNext()
    {
        if (!this.startVertexEncountered) {
            encounterStartVertex();
        }

        return !(this.prevImprovedVertices.isEmpty());
    }

    /**
     * Returns the list of vertices whose path has been improved during the
     * current pass.
     *
     * @see java.util.Iterator#next()
     */
    public Set<V> next()
    {
        if (!this.startVertexEncountered) {
            encounterStartVertex();
        }

        if (hasNext()) {
            Set<V> improvedVertices = new HashSet<V>();

            for (
                Iterator<V> iter = this.prevImprovedVertices.iterator();
                iter.hasNext();)
            {
                V vertex = iter.next();
                if (!vertex.equals(this.endVertex)) {
                    // updates outgoing vertices of the vertex.
                    updateOutgoingVertices(vertex, improvedVertices);
                }
            }

            savePassData(improvedVertices);

            return improvedVertices;
        }
        throw new NoSuchElementException();
    }

    /**
     * Unsupported.
     *
     * @see java.util.Iterator#remove()
     */
    public void remove()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the path elements of the ranking shortest paths with less than
     * <code>nMaxHops</code> edges between the start vertex and the end vertex.
     *
     * @param endVertex end vertex.
     *
     * @return list of <code>RankingPathElement</code>, or <code>null</code> of
     * no path exists between the start vertex and the end vertex.
     */
    RankingPathElementList<V, E> getPathElements(V endVertex)
    {
        return this.seenDataContainer.get(endVertex);
    }

    /**
     * Adds the first path to the specified vertex.
     *
     * @param vertex vertex reached by a path.
     * @param edge edge reaching the vertex.
     */
    private void addFirstPath(V vertex, E edge)
    {
        // the vertex has not been reached yet
        RankingPathElementList<V, E> data = createSeenData(vertex, edge);
        this.seenDataContainer.put(vertex, data);
    }

    private void assertKShortestPathsIterator(Graph<V, E> graph, V startVertex)
    {
        if (graph == null) {
            throw new NullPointerException("graph is null");
        }
        if (startVertex == null) {
            throw new NullPointerException("startVertex is null");
        }
    }

    /**
     * The first time we see a vertex, make up a new entry for it.
     *
     * @param vertex a vertex which has just been encountered.
     * @param edge the edge via which the vertex was encountered.
     *
     * @return the new entry.
     */
    private RankingPathElementList<V, E> createSeenData(V vertex, E edge)
    {
        V oppositeVertex = Graphs.getOppositeVertex(this.graph, edge, vertex);

        RankingPathElementList<V, E> oppositeData =
            this.prevSeenDataContainer.get(oppositeVertex);

        RankingPathElementList<V, E> data =
            new RankingPathElementList<V, E>(
                this.graph,
                this.k,
                oppositeData,
                edge);

        return data;
    }

    /**
     * Returns outgoing edges of the vertex.
     */
    private Set<E> edgesOf(V vertex)
    {
        if (this.graph instanceof DirectedGraph) {
            return ((DirectedGraph<V, E>) this.graph).outgoingEdgesOf(vertex);
        } else {
            return this.graph.edgesOf(vertex);
        }
    }

    /**
     * Initializes the list of paths at the start vertex ans adds an empty path.
     */
    private void encounterStartVertex()
    {
        RankingPathElementList<V, E> data =
            new RankingPathElementList<V, E>(
                this.graph,
                this.k,
                new RankingPathElement<V, E>(
                    this.startVertex));

        this.seenDataContainer.put(this.startVertex, data);
        this.prevSeenDataContainer.put(this.startVertex, data);

        // initially the only vertex whose value is considered to have changed
        // is the start vertex
        this.prevImprovedVertices.add(this.startVertex);

        this.startVertexEncountered = true;
    }

    private void savePassData(Set<V> improvedVertices)
    {
        for (Iterator<V> iter = improvedVertices.iterator(); iter.hasNext();) {
            V vertex = iter.next();

            RankingPathElementList<V, E> clonedData =
                new RankingPathElementList<V, E>(
                    this.seenDataContainer.get(vertex));
            this.prevSeenDataContainer.put(vertex, clonedData);
        }

        this.prevImprovedVertices = improvedVertices;
    }

    /**
     * Try to add a new paths for the vertex. These new paths reached the
     * specified vertex and ended with the specified edge.
     *
     * @param vertex a vertex which has just been encountered.
     * @param edge the edge via which the vertex was encountered.
     */
    private boolean tryToAddNewPaths(V vertex, E edge)
    {
        RankingPathElementList<V, E> data = this.seenDataContainer.get(vertex);

        V oppositeVertex = Graphs.getOppositeVertex(this.graph, edge, vertex);
        RankingPathElementList<V, E> oppositeData =
            this.prevSeenDataContainer.get(oppositeVertex);

        return data.addPathElements(oppositeData, edge);
    }

    /**
     * Updates outgoing vertices of the vertex. For each outgoing vertex, the
     * new paths are obtained by concatenating the specified edge to the
     * calculated paths of the specified vertex. If the weight of a new path is
     * greater than the weight of any path stored so far at the outgoing vertex
     * then the pah is not added, otherwise it is added to the list of paths in
     * increasing order of weight.
     *
     * @param vertex
     * @param improvedVertices
     */
    private void updateOutgoingVertices(V vertex, Set<V> improvedVertices)
    {
        // try to add new paths for the target vertices of the outgoing edges
        // of the vertex.
        for (Iterator<E> iter = edgesOf(vertex).iterator(); iter.hasNext();) {
            E edge = iter.next();
            V vertexReachedByEdge =
                Graphs.getOppositeVertex(this.graph, edge,
                    vertex);

            // check if the path does not loop over the start vertex.
            if (vertexReachedByEdge != this.startVertex) {
                if (this.seenDataContainer.containsKey(vertexReachedByEdge)) {
                    boolean relaxed =
                        tryToAddNewPaths(vertexReachedByEdge,
                            edge);
                    if (relaxed) {
                        improvedVertices.add(vertexReachedByEdge);
                    }
                } else {
                    addFirstPath(vertexReachedByEdge, edge);
                    improvedVertices.add(vertexReachedByEdge);
                }
            }
        }
    }
}

// End KShortestPathsIterator.java
