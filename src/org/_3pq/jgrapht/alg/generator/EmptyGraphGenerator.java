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
 * GraphGenerator.java
 * ----------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  John V. Sichi
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 16-Sept-2003 : Initial revision (JVS);
 *
 */
package org._3pq.jgrapht.alg.generator;

import java.util.Map;

import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.VertexFactory;

/**
 * EmptyGraphGenerator generates an <a
 * href="http://mathworld.wolfram.com/EmptyGraph.html">empty graph</a> of any
 * degree.
 *
 * @author John V. Sichi
 *
 * @since Sept 16, 2003
 */
public class EmptyGraphGenerator implements GraphGenerator {
    private int m_degree;

    /**
     * Construct a new EmptyGraphGenerator.
     *
     * @param degree number of vertices to be generated
     */
    public EmptyGraphGenerator( int degree ) {
        m_degree = degree;
    }

    /**
     * @see GraphGenerator#generateGraph
     */
    public void generateGraph( Graph target, VertexFactory vertexFactory,
        Map resultMap ) {
        for( int i = 0; i < m_degree; ++i ) {
            target.addVertex( vertexFactory.createVertex(  ) );
        }
    }
}
