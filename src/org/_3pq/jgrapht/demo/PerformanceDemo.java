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
/* --------------------
 * PerformanceDemo.java
 * --------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 10-Aug-2003 : Initial revision (BN);
 *
 */
package org._3pq.jgrapht.demo;

import java.io.IOException;

import java.util.Iterator;

import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.graph.Pseudograph;
import org._3pq.jgrapht.traverse.BreadthFirstIterator;
import org._3pq.jgrapht.traverse.DepthFirstIterator;

/**
 * A simple demo to test memory and CPU consumption on a graph with 3 million
 * elements.
 * 
 * <p>
 * NOTE: To run this demo you may need to increase the JVM max mem size. In
 * Sun's JVM it is done using the "-Xmx" switch. Specify "-Xmx300M" to set it
 * to 300MB.
 * </p>
 * 
 * <p>
 * WARNING: Don't run this demo as-is on machines with less than 512MB memory.
 * Your machine will start paging severely. You need to first modify it to
 * have fewer graph elements. This is easily done by changing the loop counter
 * below.
 * </p>
 *
 * @author Barak Naveh
 *
 * @since Aug 10, 2003
 */
public final class PerformanceDemo {
    /**
     * The starting point for the demo.
     *
     * @param args ignored.
     */
    public static void main( String[] args ) {
        System.out.println( 
            "allocating graph with 3M elements (may take a few tens of seconds)..." );

        long   time = System.currentTimeMillis(  );
        Graph  g    = new Pseudograph(  );
        Object prev;
        Object curr;

        curr = prev = new Object(  );
        g.addVertex( prev );

        for( int i = 0; i < 500000; i++ ) {
            curr = new Object(  );
            g.addVertex( curr );
            g.addEdge( prev, curr );
            g.addEdge( prev, curr );
            g.addEdge( prev, curr );
            g.addEdge( prev, curr );
            g.addEdge( prev, curr );
            prev = curr;
        }

        reportElapsedFor( "graph allocation", time );

        time = System.currentTimeMillis(  );

        for( Iterator i = new BreadthFirstIterator( g ); i.hasNext(  ); ) {
            i.next(  );
        }

        reportElapsedFor( "breadth traversal", time );

        time = System.currentTimeMillis(  );

        for( Iterator i = new DepthFirstIterator( g ); i.hasNext(  ); ) {
            i.next(  );
        }

        reportElapsedFor( "depth traversal", time );

        System.out.println(  );
        System.out.println( 
            "Paused: graph is still in memory (to check mem consumption)." );
        System.out.print( "press any key to finish..." );

        try {
            System.in.read(  );
        }
         catch( IOException e ) {}

        System.out.println( "done." );
    }


    private static void reportElapsedFor( String msg, long startTime ) {
        double time = ( System.currentTimeMillis(  ) - startTime ) / 1000;
        System.out.println( msg + " (" + time + " secs)" );
    }
}
