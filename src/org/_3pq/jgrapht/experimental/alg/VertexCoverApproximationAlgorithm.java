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
 * VertexCoverApproximationAlgorithm.java
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

import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.graph.Pseudograph;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implements a p-time 2-approximation algorithm for Vertex Cover on 
 * {@link org._3pq.jgrapht.graph.Pseudograph}s.
 * <p>This algorithm is due to Jenny Walter, CMPU-240: Lecture notes for Language Theory 
 * and Computation, Fall 2002, Vassar College, 
 * <a href="http://www.cs.vassar.edu/~walter/cs241index/lectures/PDF/approx.pdf">
 * http://www.cs.vassar.edu/~walter/cs241index/lectures/PDF/approx.pdf</a>.
 * 
 * @author Linda Buisman
 */
public class VertexCoverApproximationAlgorithm
	implements VertexCoverAlgorithm {
		
	private	Pseudograph graph;
	
	/**
	 * Creates an instance of VertexCoverApproximationAlgorithm for 
	 * <code>DirectedGraph</code>s.
	 * @param graph the graph to cover
	 */	
	public VertexCoverApproximationAlgorithm(Pseudograph graph) {
		this.graph = graph;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Collection vertexCover() {
		// C <-- Ø
		Set currentCover = new HashSet();
		// E' <-- E // set of edges of G
		Set edgesToCover = new HashSet(graph.edgeSet());
		// while E' != empty set
		while (!edgesToCover.isEmpty()) {
			// let (u,v) be an arbitrary edge of E'
			Edge e = (Edge) edgesToCover.iterator().next();
			// C <-- C U {u,v}
			Object u = e.getSource();
			Object v = e.getTarget();
			currentCover.add(u);
			currentCover.add(v);
			// remove from E' every edge incident on either u or v
			List incidentU = graph.edgesOf(u);
			edgesToCover.removeAll(incidentU);
			List incidentV = graph.edgesOf(v);
			edgesToCover.removeAll(incidentV);
		} // end while
		// return C
		return currentCover;
	}		
}
