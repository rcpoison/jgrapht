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
 * LinearGraphGenerator generates a linear graph of any degree.  For a directed
 * graph, the edges are oriented from START_VERTEX to END_VERTEX.
 *
 * @author John V. Sichi
 *
 * @since Sept 16, 2003
 */
public class LinearGraphGenerator implements GraphGenerator {
    /** Role for the first vertex generated. */
    public static final String START_VERTEX = "Start Vertex";

    /** Role for the last vertex generated. */
    public static final String END_VERTEX = "End Vertex";
    private int                m_degree;

    /**
     * Construct a new LinearGraphGenerator.
     *
     * @param degree number of vertices to be generated
     */
    public LinearGraphGenerator( int degree ) {
        m_degree = degree;
    }

    /**
     * @see GraphGenerator#generateGraph
     */
    public void generateGraph( Graph target, VertexFactory vertexFactory,
        Map resultMap ) {
        Object lastVertex = null;

        for( int i = 0; i < m_degree; ++i ) {
            Object newVertex = vertexFactory.createVertex(  );
            target.addVertex( newVertex );

            if( lastVertex != null ) {
                target.addEdge( lastVertex, newVertex );
            }
            else {
                if( resultMap != null ) {
                    resultMap.put( START_VERTEX, newVertex );
                }
            }

            lastVertex = newVertex;
        }

        if( ( resultMap != null ) && ( lastVertex != null ) ) {
            resultMap.put( END_VERTEX, lastVertex );
        }
    }
}
