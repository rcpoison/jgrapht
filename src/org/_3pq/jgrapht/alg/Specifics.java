/*
 * Created on Jul 29, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org._3pq.jgrapht.alg;

import java.util.List;


/**
 * Provides unified interface for operations that are different in directed
 * graphs and in undirected graphs.
 *
 * @author Barak Naveh
 *
 * @since Jul 28, 2003
 */
abstract class Specifics {
    /**
     * Returns the edges outgoing from the specified vertex in case of
     * directed graph, and the edge touching the specified vertex in case
     * of undirected graph.
     *
     * @param vertex the vertex whose outgoing edges are to be returned.
     *
     * @return
     */
    public abstract List edgesOf( Object vertex );
}