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
 * (C) Copyright 2003, by Barak Naveh and Contributors.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.VertexFactory;

/**
 * WheelGraphGenerator generates a <a
 * href="http://mathworld.wolfram.com/WheelGraph.html">wheel graph</a> of any
 * degree.
 *
 * @author John V. Sichi
 *
 * @since Sep 16, 2003
 */
public class WheelGraphGenerator implements GraphGenerator {
    /** Role for the hub vertex. */
    public static final String HUB_VERTEX = "Hub Vertex";
    private boolean            m_inwardSpokes;
    private int                m_degree;

    /**
     * Construct a new WheelGraphGenerator.
     *
     * @param degree number of vertices to be generated
     * @param inwardSpokes if true and graph is directed, spokes are oriented
     *        from rim to hub; else from hub to rim
     */
    public WheelGraphGenerator( int degree, boolean inwardSpokes ) {
        if (degree < 0) {
            throw new IllegalArgumentException("must be non-negative");
        }
        m_degree           = degree;
        m_inwardSpokes     = inwardSpokes;
    }

    /**
     * @see GraphGenerator#generateGraph
     */
    public void generateGraph( Graph target, final VertexFactory vertexFactory,
        Map resultMap ) {
        if( m_degree < 1 ) {
            return;
        }

        // A little trickery to intercept the rim generation.  This is
        // necessary since target may be initially non-empty, meaning we can't
        // rely on its vertex set after the rim is generated.
        final Collection rim              = new ArrayList(  );
        VertexFactory    rimVertexFactory =
            new VertexFactory(  ) {
                public Object createVertex(  ) {
                    Object vertex      = vertexFactory.createVertex(  );
                    rim.add( vertex );

                    return vertex;
                }
            };

        CycleGraphGenerator cycleGenerator =
            new CycleGraphGenerator( m_degree - 1 );
        cycleGenerator.generateGraph( target, rimVertexFactory, resultMap );

        Object hubVertex = vertexFactory.createVertex(  );
        target.addVertex( hubVertex );

        if( resultMap != null ) {
            resultMap.put( HUB_VERTEX, hubVertex );
        }

        Iterator rimIter = rim.iterator(  );

        while( rimIter.hasNext(  ) ) {
            Object rimVertex = rimIter.next(  );

            if( m_inwardSpokes ) {
                target.addEdge( rimVertex, hubVertex );
            }
            else {
                target.addEdge( hubVertex, rimVertex );
            }
        }
    }
}
