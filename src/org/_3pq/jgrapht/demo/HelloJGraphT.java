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
/* -----------------
 * HelloJGraphT.java
 * -----------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 27-Jul-2003 : Initial revision (BN);
 *
 */
package org._3pq.jgrapht.demo;

import java.net.MalformedURLException;
import java.net.URL;

import org._3pq.jgrapht.DirectedGraph;
import org._3pq.jgrapht.UndirectedGraph;
import org._3pq.jgrapht.graph.DefaultDirectedGraph;
import org._3pq.jgrapht.graph.SimpleGraph;

/**
 * A simple introduction to using JGraphT.
 *
 * @author Barak Naveh
 *
 * @since Jul 27, 2003
 */
public final class HelloJGraphT {
    private HelloJGraphT(  ) {} // ensure non-instantiability.

    /**
     * The starting point for the demo.
     *
     * @param args ignored.
     */
    public static void main( String[] args ) {
        UndirectedGraph stringGraph = createStringGraph(  );

        // note undirected edges are printed as: {<v1>,<v2>}
        System.out.println( stringGraph.toString(  ) );

        // create a graph based on URL objects
        DirectedGraph hrefGraph = createHrefGraph(  );

        // note directed edges are printed as: (<v1>,<v2>)
        System.out.println( hrefGraph.toString(  ) );
    }


    /**
     * Creates a toy directed graph based on URL objects that represents link
     * structure.
     *
     * @return a graph based on URL objects.
     */
    private static DirectedGraph createHrefGraph(  ) {
        DirectedGraph g = new DefaultDirectedGraph(  );

        try {
            URL amazon = new URL( "http://www.amazon.com" );
            URL yahoo = new URL( "http://www.yahoo.com" );
            URL ebay  = new URL( "http://www.ebay.com" );

            // add the vertices
            g.addVertex( amazon );
            g.addVertex( yahoo );
            g.addVertex( ebay );

            // add edges to create linking structure
            g.addEdge( yahoo, amazon );
            g.addEdge( yahoo, ebay );
        }
         catch( MalformedURLException e ) {
            e.printStackTrace(  );
        }

        return g;
    }


    /**
     * Craete a toy graph based on String objects.
     *
     * @return a graph based on String objects.
     */
    private static UndirectedGraph createStringGraph(  ) {
        UndirectedGraph g = new SimpleGraph(  );

        String          v1 = "v1";
        String          v2 = "v2";
        String          v3 = "v3";
        String          v4 = "v4";

        // add the vertices
        g.addVertex( v1 );
        g.addVertex( v2 );
        g.addVertex( v3 );
        g.addVertex( v4 );

        // add edges to create a circuit
        g.addEdge( v1, v2 );
        g.addEdge( v2, v3 );
        g.addEdge( v3, v4 );
        g.addEdge( v4, v1 );

        return g;
    }
}
