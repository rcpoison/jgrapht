/*
 * Created on Jul 29, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org._3pq.jgrapht;

import java.util.Set;

import org._3pq.jgrapht.graph.Subgraph;


class SubgraphDirected extends Subgraph
    implements DirectedGraph {
    /**
     * @see Subgraph#Subgraph(Graph, Set, Set)
     */
    public SubgraphDirected( DirectedGraph base, Set vertexSubset,
        Set edgeSubset ) {
        super( (ListenableGraph) base, vertexSubset, edgeSubset );
    }
}