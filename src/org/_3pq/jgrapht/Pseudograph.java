/*
 * Created on Jul 29, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org._3pq.jgrapht;

import org._3pq.jgrapht.graph.DefaultGraph;


class Pseudograph extends DefaultGraph
    implements UndirectedGraph {
    /**
     * @see DefaultGraph
     */
    public Pseudograph( EdgeFactory ef ) {
        super( ef, true, true );
    }
}