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
package org._3pq.jgrapht.experimental;

import java.io.*;

import java.util.*;

import org._3pq.jgrapht.*;
import org._3pq.jgrapht.graph.*;

/**
 * .
 * @author  Michael Behrisch
 * @version 1.0
 */
public class GraphReader {
    private final BufferedReader _in;

    /**
     * Creates a new GraphReader object.
     *
     * @param file  
     *
     * @throws IOException  
     */
    public GraphReader( String file ) throws IOException {
        _in = new BufferedReader( new FileReader( file ) );
    }

    /**
     * .
     *
     * @param args  
     *
     * @throws Exception  
     */
    public static void main( String[] args ) throws Exception {
        Graph g = new SimpleGraph(  );
        new GraphReader( args[ 0 ] ).readGraph( g );

        Set uncovered = new HashSet( g.edgeSet(  ) );
        System.out.println( "graph read" );

        for( Iterator it = g.vertexSet(  ).iterator(  ); it.hasNext(  ); ) {
            Object v = it.next(  );
            Set    c = new HashSet( GraphHelper.neighborListOf( g, v ) );
            c.add( v );

            Graph sub = new Subgraph( g, c, null );

            if( GraphTests.isComplete( sub ) ) {
                System.out.println( c );
                uncovered.removeAll( sub.edgeSet(  ) );
            }
        }

        System.out.println( uncovered.size(  ) );
        System.out.println( uncovered );
    }


    /**
     * .
     *
     * @param g  
     *
     * @throws IOException  
     */
    public void readGraph( Graph g ) throws IOException {
        while( ready(  ) ) {
            String[] cols = skipComments(  );

            if( cols.length > 0 ) {
                if( cols[ 0 ].equals( "e" ) ) {
                    int x = Integer.parseInt( cols[ 1 ] ) - 1;
                    int y = Integer.parseInt( cols[ 2 ] ) - 1;

                    if( x != y ) {
                        GraphHelper.addEdgeWithVertices( g, new Integer( x ),
                            new Integer( y ) );
                    }
                }
            }
        }
    }


    /**
     * .
     *
     * @return  
     *
     * @throws IOException  
     */
    public boolean ready(  ) throws IOException {
        return _in.ready(  );
    }


    private String[] skipComments(  ) throws IOException {
        String[] cols = split( _in.readLine(  ) );

        while( cols.length == 0 || cols[ 0 ].equals( "c" ) ) {
            cols = split( _in.readLine(  ) );
        }

        return cols;
    }


    private String[] split( String src ) {
        final List            l   = new ArrayList(  );
        final StringTokenizer tok = new StringTokenizer( src );

        while( tok.hasMoreTokens(  ) ) {
            l.add( tok.nextToken(  ) );
        }

        return (String[]) l.toArray( new String[ l.size(  ) ] );
    }
}
