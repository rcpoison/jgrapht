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
/* ---------------------
 * VertexCoversTest.java
 * ---------------------
 * (C) Copyright 2003, by Linda Buisman and Contributors.
 *
 * Original Author:  Linda Buisman
 * Contributor(s):   Barak Naveh
 *
 * $Id$
 *
 * Changes
 * -------
 * 06-Nov-2003 : Initial revision (LB);
 * 10-Nov-2003 : Adapted to VertexCovers (BN);
 *
 */
package org._3pq.jgrapht.alg;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import junit.framework.TestCase;

import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.GraphHelper;
import org._3pq.jgrapht.graph.Pseudograph;

/**
 * Tests the vertex cover algorithms.
 *
 * @author Linda Buisman
 *
 * @since Nov 6, 2003
 */
public class VertexCoversTest extends TestCase {
    private static final int TEST_GRAPH_SIZE = 200;
    private static final int TEST_REPEATS = 20;

    /**
     * .
     */
    public void testFind2ApproximationCover(  ) {
        VertexCovers vc = new VertexCovers(  );

        for( int i = 0; i < TEST_REPEATS; i++ ) {
            Graph g = createRandomGraph(  );
            assertTrue( isCover( vc.find2ApproximationCover( g ), g ) );
        }
    }


    /**
     * .
     */
    public void testFindGreedyCover(  ) {
        VertexCovers vc = new VertexCovers(  );

        for( int i = 0; i < TEST_REPEATS; i++ ) {
            Graph g = createRandomGraph(  );
            Set   c = vc.findGreedyCover( GraphHelper.undirectedGraph( g ) );
            assertTrue( isCover( c, g ) );
        }
    }


    /**
     * Checks if the specified vertex set covers every edge of the graph. Uses
     * the definition of Vertex Cover - removes every edge that is incident on
     * a vertex in vertexSet. If no edges are left, vertexSet is a vertex
     * cover for the specified graph.
     *
     * @param vertexSet the vertices to be tested for covering the graph.
     * @param g the graph to be covered.
     *
     * @return
     */
    private boolean isCover( Set vertexSet, Graph g ) {
        Set uncoveredEdges = new HashSet( g.edgeSet(  ) );

        for( Iterator i = vertexSet.iterator(  ); i.hasNext(  ); ) {
            uncoveredEdges.removeAll( g.edgesOf( i.next(  ) ) );
        }

        return uncoveredEdges.size(  ) == 0;
    }


    /**
     * Create a random graph of TEST_GRAPH_SIZE nodes.
     *
     * @return
     */
    private Graph createRandomGraph(  ) {
        // TODO: move random graph generator to be under GraphGenerator framework. 
        Pseudograph g = new Pseudograph(  );

        for( int i = 0; i < TEST_GRAPH_SIZE; i++ ) {
            g.addVertex( new Integer( i ) );
        }

        Vector vertices = new Vector( g.vertexSet(  ) );

        // join every vertex with a random number of other vertices
        for( int source = 0; source < TEST_GRAPH_SIZE; source++ ) {
            int numEdgesToCreate =
                (int) Math.random(  ) * TEST_GRAPH_SIZE / 2 + 1;

            for( int j = 0; j < numEdgesToCreate; j++ ) {
                // find a random vertex to join to
                int target =
                    (int) Math.floor( Math.random(  ) * TEST_GRAPH_SIZE );
                g.addEdge( vertices.get( source ), vertices.get( target ) );
            }
        }

        return g;
    }
}
