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
/* ------------------------------
 * VertexCoverApproximationAlgorithmTest.java
 * ------------------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org._3pq.jgrapht.graph.Pseudograph;
import org._3pq.jgrapht.experimental.alg.VertexCoverAlgorithm;
import org._3pq.jgrapht.experimental.alg.VertexCoverApproximationAlgorithm;
import org._3pq.jgrapht.experimental.alg.VertexCoverGreedyAlgorithm;

import junit.framework.TestCase;

public class VertexCoverAlgorithmTest extends TestCase {
	
	private Pseudograph graph;
	private final int TEST_GRAPH_SIZE = 200;
	
	
	public void testVertexCoverGreedy() {
		create();
		VertexCoverAlgorithm greedy = new VertexCoverGreedyAlgorithm(graph);
		assertTrue(isSolution(greedy.vertexCover()));
	}
	
	public void testVertexCoverApproximation() {
		create();
		VertexCoverAlgorithm approx = new VertexCoverApproximationAlgorithm(graph);
		assertTrue(isSolution(approx.vertexCover()));
	}
	
	/**
	 * Create a random graph of TEST_GRAPH_SIZE nodes.
	 */
	private void create() {
		graph = new Pseudograph();
		for (int i = 0; i < TEST_GRAPH_SIZE; i++) {
			graph.addVertex(new Integer(i));
		}
		Vector vertices = new Vector(graph.vertexSet());
		// join every vertex with a random number of other vertices
		for (int sourceVertexIndex = 0; sourceVertexIndex < TEST_GRAPH_SIZE; sourceVertexIndex++) {
			int numEdgesToCreate = (int) Math.random()*TEST_GRAPH_SIZE/2 + 1;
			for (int j = 0; j < numEdgesToCreate; j++) {
				// find a random vertex to join to
				int destVertexIndex = (int) Math.floor(Math.random()*TEST_GRAPH_SIZE);
				graph.addEdge(vertices.get(sourceVertexIndex), vertices.get(destVertexIndex));
			}
		}
	}

	/**
	 * Checks whether possibleSolution really covers every edge of the graph.
	 * Uses the definition of Vertex Cover - removes every edge that is
	 * incident on a vertex in possibleSolution, so if there are no edges left
	 * they are all covered by vertices in possibleSolution.
	 */
	private boolean isSolution(Collection possibleSolution) {
		Set edgesToCover = new HashSet(graph.edgeSet());
		for (Iterator iVertices = possibleSolution.iterator();
			 iVertices.hasNext();
			 ) {
			edgesToCover.removeAll(new ArrayList(graph.edgesOf(iVertices.next())));
		}
		if (edgesToCover.size() == 0) {
			return true;
		}
		else {
			return false;
		}
	}
}
