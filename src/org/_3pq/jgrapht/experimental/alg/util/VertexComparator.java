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
 * VertexCmparator.java
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
package org._3pq.jgrapht.experimental.alg.util;

import org._3pq.jgrapht.UndirectedGraph;

/**
 * Compares two vertices based on their degree. Used by greedy algorithms that need to
 * sort vertices by their degree. Two vertices are considered equal if their
 * degrees are equal.
 * 
 * @author Linda Buisman
 */
public class VertexComparator implements java.util.Comparator {

	// true - sort vertices in ascending degree order (smaller degrees first)
	// false - sort vertices in descending degree order (larger degrees first)
	private boolean ascending;

	// used to find out the degree of a vertex in this graph
	private UndirectedGraph graph;

	/**
	 * Creates a VertexComaprator for comparing the degrees of vertices
	 * in a specific graph.
	 * 
	 * @param g graph with respect to which the degree is calculated
	 * @param asc true - will compare in ascending order of degrees (lowest first), 
	 * 			  false - will compare in descending order of degrees (highest first)
	 */
	public VertexComparator(UndirectedGraph g, boolean asc) {
		graph = g;
		ascending = asc;
	}

	/**
	 * Compare the degrees of <code>v1</code> and <code>v2</code>, taking into
	 * account whether ascending or descending order is used.
	 * 
	 * @return -1 if <code>v1</code> comes before <code>v2</code>, 
	 * 		   +1 if <code>v1</code> comes after <code>v2</code>
	 */
	public int compare(Object v1, Object v2) {
		int degree1 = graph.degreeOf(v1);
		int degree2 = graph.degreeOf(v2);
		if ((degree1 < degree2 && ascending)
			|| (degree1 > degree2 && !ascending)) {
			return -1;
		} else if (
			(degree1 > degree2 && ascending)
				|| (degree1 < degree2 && !ascending)) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * Two vertices are considered equal if their degrees are equal.
	 * 
	 * @return true if the degrees of <code>v1</code> and <code>v2</code> are
	 * equal for the given graph.
	 */
	public boolean equals(Object v1, Object v2) {
		return (graph.degreeOf(v1) == graph.degreeOf(v2));
	}
}