/*
 * Created on Jul 29, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org._3pq.jgrapht;

import org._3pq.jgrapht.graph.DefaultListenableGraph;


class ListenableDirectedGraph extends DefaultListenableGraph
    implements DirectedGraph {
    /**
     * @see DefaultListenableGraph#DefaultListenableGraph(Graph)
     */
    public ListenableDirectedGraph( DirectedGraph base ) {
        super( base );
    }
}