/*
 * Created on Jul 29, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org._3pq.jgrapht.alg;

import java.util.List;

import org._3pq.jgrapht.DirectedGraph;


class DirectedSpecifics extends Specifics {
    private DirectedGraph m_graph;

    /**
     * Creates a new DirectedSpecifics object.
     *
     * @param g
     */
    public DirectedSpecifics( DirectedGraph g ) {
        m_graph = g;
    }

    /**
     * @see BreadthFirstIterator.Specifics#edgesOf(Object)
     */
    public List edgesOf( Object vertex ) {
        return m_graph.outgoingEdgesOf( vertex );
    }
}