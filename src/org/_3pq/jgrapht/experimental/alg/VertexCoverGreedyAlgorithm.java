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
 * VertexCoverGreedyAlgorithm.java
 * --------------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Linda Buisman
 *
 * $Id$
 *
 * Changes
 * -------
 * 06-Nov-2003 : Initial revision (LB);
 *
 */
package org._3pq.jgrapht.experimental.alg;

import org._3pq.jgrapht.experimental.alg.util.*;
import org._3pq.jgrapht.graph.Pseudograph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implements a greedy approximation algorithm for Vertex Cover on 
 * {@link org._3pq.jgrapht.graph.Pseudograph}s.
 * 
 * @author Linda Buisman
 */

public class VertexCoverGreedyAlgorithm implements VertexCoverAlgorithm {
	
	private	Pseudograph graph;
	
	/**
	 * Creates an instance of VertexCoverGreedyAlgorithm for 
	 * <code>Pseudograph</code>s.
	 * @param graph the graph to cover
	 */	
	public VertexCoverGreedyAlgorithm(Pseudograph graph) {
		this.graph = graph;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Collection vertexCover() {
		// C <-- Ø
		Set currentCover = new HashSet();
		// G' <-- G
		Pseudograph graphToCover = (Pseudograph) graph.clone();
		// while G' != Ø
		while (graphToCover.edgeSet().size() > 0) {
			// v <-- vertex with maximum degree in G'
			// sort vertices in descending order of degree
			Set originalVertexSet = graphToCover.vertexSet();
			VertexComparator comp = new VertexComparator(graphToCover, false);
			List sortedVertexList = new ArrayList(originalVertexSet);
			Collections.sort(sortedVertexList, comp);
			Object v = sortedVertexList.get(0);
			// C <-- C U {v}
			currentCover.add(v);
			// remove from G' every edge incident on v, and v itself
			graphToCover.removeAllEdges(
				new ArrayList(graphToCover.edgesOf(v)));
			} // end while
		return currentCover;
	}	
}
