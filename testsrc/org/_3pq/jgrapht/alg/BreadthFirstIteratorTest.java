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
/* -----------------------------
 * BreadthFirstIteratorTest.java
 * -----------------------------
 * (C) Copyright 2003, by Liviu Rau and Contributors.
 *
 * Original Author:  Liviu Rau
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 30-Jul-2003 : Initial revision (LR);
 *
 */
package org._3pq.jgrapht.alg;

import junit.framework.TestCase;

import org._3pq.jgrapht.DirectedGraph;
import org._3pq.jgrapht.GraphFactory;
import org._3pq.jgrapht.alg.BreadthFirstIterator;

/**
 * Tests for the {@link BreadthFirstIterator} class.
 *
 * @author Liviu Rau
 *
 * @since Jul 30, 2003
 */
public class BreadthFirstIteratorTest extends TestCase {
    /**
     * .
     */
    public void testDirectedGraph(  ) {
        GraphFactory  graphFactory = GraphFactory.getFactory(  );
        DirectedGraph graph = graphFactory.createDirectedGraph(  );

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
        
        graph.addVertex("orphan");

        graph.addEdge( v1, v2 );
        graph.addEdge( v1, v3 );
        graph.addEdge( v2, v4 );
        graph.addEdge( v3, v5 );
        graph.addEdge( v3, v6 );
        graph.addEdge( v5, v6 );
        graph.addEdge( v5, v7 );
        graph.addEdge( v6, v1 );
        graph.addEdge( v7, v8 );
        graph.addEdge( v7, v9 );
        graph.addEdge( v8, v2 );
        graph.addEdge( v9, v4 );

        BreadthFirstIterator iterator = new BreadthFirstIterator( graph, v1 );
        StringBuffer         result = new StringBuffer(  );

        while( iterator.hasNext(  ) ) {
            result.append( (String) iterator.next(  ) );
            
            if (iterator.hasNext(  )) {
                result.append(',');
            }
        }

        String s = result.toString(  );
        System.out.println( s );
        assertEquals( "1,2,3,4,5,6,7,8,9,orphan", s );
    }
}
