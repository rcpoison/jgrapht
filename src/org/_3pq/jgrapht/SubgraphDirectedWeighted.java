/*
 * Created on Jul 29, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org._3pq.jgrapht;

import java.util.Set;


class SubgraphDirectedWeighted extends SubgraphDirected
    implements DirectedWeightedGraph {
    /**
     * @see Subgraph#Subgraph(Graph, Set, Set)
     */
    public SubgraphDirectedWeighted( DirectedWeightedGraph base,
        Set vertexSubset, Set edgeSubset ) {
        super( base, vertexSubset, edgeSubset );
    }
}