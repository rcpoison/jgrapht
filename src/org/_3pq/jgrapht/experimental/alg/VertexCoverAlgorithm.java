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
 * VertexCoverAlgorithm.java
 * -------------------------
 * (C) Copyright 2003, by Linda Buisman and Contributors.
 *
 * Original Author:  Linda Buisman
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 06-Nov-2003 : Initial revision (LB);
 *
 */
package org._3pq.jgrapht.experimental.alg;

import java.util.Set;

/**
 * An algorithm to find a Vertex Cover for a graph. A vertex cover is a set of
 * vertices that touches all the edges in the graph. The graph's vertex set is
 * a trivial cover, but a minimal vertex set is usually desired, or at least
 * an approximation. Finding a true minimal vertex cover is an NP-Complete
 * problem. For more on the Vertex Cover problem, see <a
 * href="http://mathworld.wolfram.com/VertexCover.html">
 * http://mathworld.wolfram.com/VertexCover.html</a>
 *
 * @author Linda Buisman
 *
 * @since Nov 6, 2003
 */
public interface VertexCoverAlgorithm {
    /**
     * Finds a set of vertices, C, such that for every edge in the graph, at
     * least one of its two vertices is in C.
     *
     * @return a set representing a vertex cover for a graph.
     */
    public Set findCover(  );
}
