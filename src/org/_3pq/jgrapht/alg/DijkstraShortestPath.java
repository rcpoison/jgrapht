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
/* --------------------------
 * DijkstraShortestPath.java
 * --------------------------
 * (C) Copyright 2003, by John V. Sichi and Contributors.
 *
 * Original Author:  John V. Sichi
 *
 * $Id$
 *
 * Changes
 * -------
 * 02-Sept-2003 : Initial revision (JVS);
 *
 */
package org._3pq.jgrapht.alg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.traverse.ClosestFirstIterator;

/**
 * An implementation of <a
 * href="http://mathworld.wolfram.com/DijkstrasAlgorithm.html"> Dijkstra's
 * shortest path algorithm</a> using <code>ClosestFirstIterator</code>.
 *
 * @author John V. Sichi
 *
 * @since Sept 2, 2003
 */
public final class DijkstraShortestPath {
    private DijkstraShortestPath(  ) {} // ensure non-instantiability.

    /**
     * Find the shortest path between two vertices, represented as a List of
     * Edges in order from start to end vertex.
     *
     * @param searchGraph the graph to be searched
     * @param startVertex the vertex at which the path should start
     * @param endVertex the vertex at which the path should end
     *
     * @return List of Edges, or null if no path exists
     */
    public static List findPathBetween( Graph searchGraph, Object startVertex,
        Object endVertex ) {
        ClosestFirstIterator iter =
            new ClosestFirstIterator( searchGraph, startVertex );

        while( iter.hasNext(  ) ) {
            Object vertex = iter.next(  );

            if( vertex.equals( endVertex ) ) {
                return createPath( iter, endVertex );
            }
        }

        return null;
    }


    /**
     * Helper for findPathBetween.
     *
     * @param iter a ClosestFirstIterator which has already visited endVertex
     * @param endVertex the vertex being sought
     *
     * @return non-null result for findPathBetween
     */
    private static List createPath( ClosestFirstIterator iter, Object endVertex ) {
        List path = new ArrayList(  );

        for( ;; ) {
            Edge edge = iter.getSpanningTreeEdge( endVertex );

            if( edge == null ) {
                break;
            }

            path.add( edge );
            endVertex = edge.oppositeVertex( endVertex );
        }

        Collections.reverse( path );

        return path;
    }
}
