/*
 * Created on Jul 29, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org._3pq.jgrapht;

import org._3pq.jgrapht.graph.UnmodifiableGraph;


class UnmodifiableUndirectedGraph extends UnmodifiableGraph
    implements UndirectedGraph {
    /**
     * @see UnmodifiableGraph#UnmodifiableGraph(Graph)
     */
    public UnmodifiableUndirectedGraph( Graph g ) {
        super( g );
    }
}