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
/* -------------------------
 * BreadthFirstIterator.java
 * -------------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   Liviu Rau
 *
 * $Id$
 *
 * Changes
 * -------
 * 24-Jul-2003 : Initial revision (BN);
 * 06-Aug-2003 : Extracted common logic to TraverseUtils.XXFirstIterator (BN);
 *
 */
package org._3pq.jgrapht.traverse;

import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.traverse.TraverseUtils.SimpleQueue;
import org._3pq.jgrapht.traverse.TraverseUtils.XXFirstIterator;

/**
 * A breadth-first iterator for a directed and an undirected graph. For this
 * iterator to work correctly the graph must not be modified during iteration.
 * Currently there are no means to ensure that, nor to fail-fast. The result
 * of such modifications are undefined.
 *
 * @author Barak Naveh
 *
 * @since Jul 19, 2003
 */
public class BreadthFirstIterator extends XXFirstIterator {
    /**
     * Creates a new breadth-first iterator for the specified graph.
     *
     * @param g the graph to be iterated.
     * @param crossComponentTraversal whether to traverse the graph across
     *        connected components.
     */
    public BreadthFirstIterator( Graph g, boolean crossComponentTraversal ) {
        this( g, null, crossComponentTraversal );
    }


    /**
     * Creates a new breadth-first iterator for the specified graph. Iteration
     * will start at the specified start vertex. If the specified start vertex
     * is <code>null</code>, Iteration will start at an arbitrary graph
     * vertex.
     *
     * @param g the graph to be iterated.
     * @param startVertex the vertex iteration to be started.
     * @param crossComponentTraversal whether to traverse the graph across
     *        connected components.
     */
    public BreadthFirstIterator( Graph g, Object startVertex,
        boolean crossComponentTraversal ) {
        super( g, startVertex, crossComponentTraversal, new SimpleQueue(  ) );
    }
}
