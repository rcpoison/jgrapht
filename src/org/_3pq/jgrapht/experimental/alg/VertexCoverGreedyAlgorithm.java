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
/* -------------------------------
 * VertexCoverGreedyAlgorithm.java
 * -------------------------------
 * (C) Copyright 2003, by Linda Buisman and Contributors.
 *
 * Original Author:  Linda Buisman
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 06-Nov-2003 : Initial revision (LB);
 *
 */
package org._3pq.jgrapht.experimental.alg;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org._3pq.jgrapht.DirectedGraph;
import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.UndirectedGraph;
import org._3pq.jgrapht.alg.util.VertexDegreeComparator;
import org._3pq.jgrapht.graph.AsUndirectedGraph;
import org._3pq.jgrapht.graph.Subgraph;

/**
 * A greedy approximation algorithm for Vertex Cover on a specified graph.
 *
 * @author Linda Buisman
 *
 * @since Nov 6, 2003
 */
public class VertexCoverGreedyAlgorithm implements VertexCoverAlgorithm {
    private UndirectedGraph m_graph;

    /**
     * Creates an instance of VertexCoverGreedyAlgorithm for the specified
     * graph.
     *
     * @param g the graph for which the algorithm to be applied.
     *
     * @throws IllegalArgumentException if the graph is neigher DirectedGraph
     *         nor UndirectedGraph.
     */
    public VertexCoverGreedyAlgorithm( Graph g ) {
        if( g instanceof DirectedGraph ) {
            m_graph = new AsUndirectedGraph( (DirectedGraph) g );
        }
        else if( g instanceof UndirectedGraph ) {
            m_graph = (UndirectedGraph) g;
        }
        else {
            throw new IllegalArgumentException( "Unrecognized graph" );
        }
    }

    /**
     * {@inheritDoc}
     */
    public Set findCover(  ) {
        // C <-- Ø
        Set cover = new HashSet(  );

        // G' <-- G
        Subgraph g = new Subgraph( m_graph, null, null );

        // compare vertices in descending order of degree
        VertexDegreeComparator comp = new VertexDegreeComparator( g );

        // while G' != Ø
        while( g.edgeSet(  ).size(  ) > 0 ) {
            // v <-- vertex with maximum degree in G'
            Object v = Collections.max( g.vertexSet(  ), comp );

            // C <-- C U {v}
            cover.add( v );

            // remove from G' every edge incident on v, and v itself
            g.removeVertex( v );
        }

        return cover;
    }
}
