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
/* -------------------
 * GraphGenerator.java
 * -------------------
 * (C) Copyright 2003, by John V. Sichi and Contributors.
 *
 * Original Author:  John V. Sichi
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 16-Sep-2003 : Initial revision (JVS);
 *
 */
package org._3pq.jgrapht.alg.generator;

import java.util.HashMap;
import java.util.Map;

import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.VertexFactory;

/**
 * CycleGraphGenerator generates a <a
 * href="http://mathworld.wolfram.com/CycleGraph.html">cycle graph</a> of any
 * degree. A cycle graph is a graph containing a single cycle through all the
 * vertices. For a directed graph, the generated edges are oriented
 * consistently around the cycle.
 *
 * @author John V. Sichi
 *
 * @since Sep 16, 2003
 */
public class CycleGraphGenerator implements GraphGenerator {
    private int m_degree;

    /**
     * Construct a new CycleGraphGenerator.
     *
     * @param degree number of vertices to be generated
     */
    public CycleGraphGenerator( int degree ) {
        if (degree < 0) {
            throw new IllegalArgumentException("must be non-negative");
        }
        m_degree = degree;
    }

    /**
     * @see GraphGenerator#generateGraph
     */
    public void generateGraph( Graph target, VertexFactory vertexFactory,
        Map resultMap ) {
        if( m_degree < 1 ) {
            return;
        }

        LinearGraphGenerator linearGenerator =
            new LinearGraphGenerator( m_degree );
        Map                  privateMap = new HashMap(  );
        linearGenerator.generateGraph( target, vertexFactory, privateMap );

        Object startVertex =
            privateMap.get( LinearGraphGenerator.START_VERTEX );
        Object endVertex = privateMap.get( LinearGraphGenerator.END_VERTEX );
        target.addEdge( endVertex, startVertex );
    }
}
