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
 * VertexCoverAlgorithm.java
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

import java.util.Collection;

/**
 * A Vertex Cover Algorithm. For more information on Vertex Cover, see
 * <a href="http://mathworld.wolfram.com/VertexCover.html">
 * http://mathworld.wolfram.com/VertexCover.html</a>
 *
 * @author Linda Buisman
 */
public interface VertexCoverAlgorithm {

	/**
	 * Finds a <code>Collection</code> of vertices such that for every 
	 * {@link org._3pq.jgrapht.Edge} in the {@link org._3pq.jgrapht.UnirectedGraph},
	 * at least one of its two vertices is in the<code>Collection</code>}.
	 * 
	 * @return a <code>Collection</code> representing the Vertex Cover 
	 * of the <code>UnirectedGraph</code>.
	 */
	public Collection vertexCover();
}
