/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Lead:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2004, by Barak Naveh and Contributors.
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
 * AbstractGraphIteratorTest.java
 * ------------------------------
 * (C) Copyright 2003, by Liviu Rau and Contributors.
 *
 * Original Author:  Liviu Rau
 * Contributor(s):   Barak Naveh
 *
 * $Id$
 *
 * Changes
 * -------
 * 30-Jul-2003 : Initial revision (LR);
 * 06-Aug-2003 : Test traversal listener & extract a shared superclass (BN);
 *
 */
package org._3pq.jgrapht.traverse;

import org._3pq.jgrapht.DirectedGraph;
import org._3pq.jgrapht.EnhancedTestCase;
import org._3pq.jgrapht.event.ConnectedComponentTraversalEvent;
import org._3pq.jgrapht.event.EdgeTraversalEvent;
import org._3pq.jgrapht.event.TraversalListener;
import org._3pq.jgrapht.event.VertexTraversalEvent;
import org._3pq.jgrapht.graph.DefaultDirectedWeightedGraph;

/**
 * A basis for testing {@link org._3pq.jgrapht.traverse.BreadthFirstIterator}
 * and {@link org._3pq.jgrapht.traverse.DepthFirstIterator} classes.
 *
 * @author Liviu Rau
 *
 * @since Jul 30, 2003
 */
public abstract class AbstractGraphIteratorTest extends EnhancedTestCase {
    StringBuffer m_result;

    /**
     * .
     */
    public void testDirectedGraph(  ) {
        m_result = new StringBuffer(  );

        DirectedGraph         graph = createDirectedGraph(  );

        AbstractGraphIterator iterator = createIterator( graph, "1" );
        iterator.addTraversalListener( new MyTraversalListener(  ) );

        while( iterator.hasNext(  ) ) {
            m_result.append( (String) iterator.next(  ) );

            if( iterator.hasNext(  ) ) {
                m_result.append( ',' );
            }
        }

        assertEquals( getExpectedStr2(  ), m_result.toString(  ) );
    }


    abstract String getExpectedStr1(  );


    abstract String getExpectedStr2(  );


    DirectedGraph createDirectedGraph(  ) {
        DirectedGraph graph = new DefaultDirectedWeightedGraph(  );

        //
        String v1 = "1";
        String v2 = "2";
        String v3 = "3";
        String v4 = "4";
        String v5 = "5";
        String v6 = "6";
        String v7 = "7";
        String v8 = "8";
        String v9 = "9";

        graph.addVertex( v1 );
        graph.addVertex( v2 );
        graph.addVertex( "3" );
        graph.addVertex( "4" );
        graph.addVertex( "5" );
        graph.addVertex( "6" );
        graph.addVertex( "7" );
        graph.addVertex( "8" );
        graph.addVertex( "9" );

        graph.addVertex( "orphan" );

        // NOTE:  set weights on some of the edges to test traversals like
        // ClosestFirstIterator where it matters.  For other traversals, it
        // will be ignored.  Rely on the default edge weight being 1.
        graph.addEdge( v1, v2 );
        graph.addEdge( v1, v3 ).setWeight( 100 );
        graph.addEdge( v2, v4 ).setWeight( 1000 );
        graph.addEdge( v3, v5 );
        graph.addEdge( v3, v6 ).setWeight( 100 );
        graph.addEdge( v5, v6 );
        graph.addEdge( v5, v7 ).setWeight( 200 );
        graph.addEdge( v6, v1 );
        graph.addEdge( v7, v8 ).setWeight( 100 );
        graph.addEdge( v7, v9 );
        graph.addEdge( v8, v2 );
        graph.addEdge( v9, v4 );

        return graph;
    }


    abstract AbstractGraphIterator createIterator( DirectedGraph g,
        Object startVertex );

    /**
     * Internal traversal listener.
     *
     * @author Barak Naveh
     */
    private class MyTraversalListener implements TraversalListener {
        private int m_componentNumber      = 0;
        private int m_numComponentVertices = 0;

        /**
         * @see TraversalListener#connectedComponentFinished(ConnectedComponentTraversalEvent)
         */
        public void connectedComponentFinished( 
            ConnectedComponentTraversalEvent e ) {
            switch( m_componentNumber ) {
                case 1:
                    assertEquals( getExpectedStr1(  ), m_result.toString(  ) );
                    assertEquals( 9, m_numComponentVertices );

                    break;

                case 2:
                    assertEquals( getExpectedStr2(  ), m_result.toString(  ) );
                    assertEquals( 1, m_numComponentVertices );

                    break;

                default:
                    assertFalse(  );

                    break;
            }

            m_numComponentVertices = 0;
        }


        /**
         * @see TraversalListener#connectedComponentStarted(ConnectedComponentTraversalEvent)
         */
        public void connectedComponentStarted( 
            ConnectedComponentTraversalEvent e ) {
            m_componentNumber++;
        }


        /**
         * @see TraversalListener#edgeTraversed(EdgeTraversalEvent)
         */
        public void edgeTraversed( EdgeTraversalEvent e ) {
            // to be tested...
        }


        /**
         * @see TraversalListener#vertexTraversed(VertexTraversalEvent)
         */
        public void vertexTraversed( VertexTraversalEvent e ) {
            m_numComponentVertices++;
        }
    }
}
