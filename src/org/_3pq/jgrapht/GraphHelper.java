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
/* ----------------
 * GraphHelper.java
 * ----------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 10-Jul-2003 : Initial revision (BN);
 *
 */
package org._3pq.jgrapht;

/**
 * A collection of utilities to assist the working with graphs.
 *
 * @author Barak Naveh
 *
 * @since Jul 31, 2003
 */
public final class GraphHelper {
    private GraphHelper(  ) {} // ensure non-instantiability.

    /**
     * Creates a new edge and adds it to the specified graph similarly to the
     * {@link Graph#addEdge(Object, Object)} method.
     *
     * @param g the graph for which the edge to be added.
     * @param sourceVertex source vertex of the edge.
     * @param targetVertex target vertex of the edge.
     * @param weight weight of the edge.
     *
     * @return The newly created edge if added to the graph, otherwise
     *         <code>null</code>.
     *
     * @see Graph#addEdge(Object, Object)
     */
    public static Edge addEdge( Graph g, Object sourceVertex,
        Object targetVertex, double weight ) {
        EdgeFactory ef = g.getEdgeFactory(  );
        Edge        e = ef.createEdge( sourceVertex, targetVertex );
        e.setWeight( weight );

        return g.addEdge( e ) ? e : null;
    }


    /**
     * Adds the specified edge to the specified graph including its vertices.
     * If any of the vertices of the specified edge are not already in the
     * graph they are also added (before the edge is added).
     *
     * @param g the graph for which the specified edge to be added.
     * @param e the edge to be added to the graph (including its vertices).
     *
     * @return <code>true</code> if and only if the specified edge was not
     *         already contained in the graph.
     */
    public static boolean addEdgeWithVertices( Graph g, Edge e ) {
        g.addVertex( e.getSource(  ) );
        g.addVertex( e.getTarget(  ) );

        return g.addEdge( e );
    }
}
