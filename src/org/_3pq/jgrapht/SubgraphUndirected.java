/*
 * Created on Jul 29, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org._3pq.jgrapht;

import java.util.Set;

import org._3pq.jgrapht.graph.Subgraph;


class SubgraphUndirected extends Subgraph
    implements UndirectedGraph {
    /**
     * @see Subgraph#Subgraph(Graph, Set, Set)
     */
    public SubgraphUndirected( UndirectedGraph base, Set vertexSubset,
        Set edgeSubset ) {
        super( (ListenableGraph) base, vertexSubset, edgeSubset );
    }
}