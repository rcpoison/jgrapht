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
/* --------------------------
 * StrongConnectivityInspector.java
 * --------------------------
 * (C) Copyright 2005, by Chris Soltenborn and Contributors.
 *
 * Original Author:  Chris Soltenborn
 * Contributor(s):   John V. Sichi
 *
 * $Id$
 *
 * Changes
 * -------
 * 05-Feb-2005 : Initial revision (CS);
 * 20-Apr-2005 : Reformatted for JGraphT (JVS);
 *
 */
package org._3pq.jgrapht.alg;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org._3pq.jgrapht.DirectedGraph;
import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.GraphHelper;
import org._3pq.jgrapht.edge.DirectedEdge;
import org._3pq.jgrapht.graph.DefaultDirectedGraph;
import org._3pq.jgrapht.traverse.DepthFirstIterator;

/**
 * Complements the {@link ConnectivityInspector} class with the capability to
 * compute the strongly connected components of a directed graph.  The
 * algorithm is implemented after "Corman et al: Introduction to agorithms",
 * Chapter 25.2. It has a running time of O(V log( V ) + E).  NOTE: The
 * running time could be improved to O(V + E) in the following way: If a
 * vertex is visited by AnalyzingDepthFirstIterator for the last time, it is
 * added to a stack. This stack can be used to perform the first call of
 * DepthFirstSearch without sorting it first (this is where the O(V log V)
 * comes from).
 * 
 * <p>
 * Unlike ConnectivityInspector, this class does not implement incremental
 * inspection; every call to stronglyConnectedSets executes the full
 * algorithm.
 * </p>
 *
 * @author chris
 *
 * @since Feb 2, 2005
 */
public class StrongConnectivityInspector {
    private final DirectedGraph graph;

    /**
     * The constructor for the StrongConnectivityInspector class.
     *
     * @param g The graph to inspect.
     */
    public StrongConnectivityInspector( DirectedGraph g ) {
        this.graph = g;
    }

    /**
     * Computes a List of Sets, where each set contains vertices which together
     * form a strongly connected component within the given graph.
     *
     * @return List of Sets containing the strongly connected components
     */
    public List stronglyConnectedSets(  ) {
        LinkedList result = new LinkedList(  );

        // calculate discover and finish times
        AnalyzingDepthFirstIterator iter =
            new AnalyzingDepthFirstIterator( graph );

        while( iter.hasNext(  ) ) {
            iter.next(  );
        }

        Map verticesData = iter.getVerticesData(  );

        // create inverted graph
        DirectedGraph invertedGraph = new DefaultDirectedGraph(  );
        GraphHelper.addGraphReversed( invertedGraph, this.graph );

        // sort vertices in increasing finish time order
        VertexData[] orderedVertices =
            new VertexData[ verticesData.values(  ).size(  ) ];
        verticesData.values(  ).toArray( orderedVertices );
        Arrays.sort( orderedVertices, new FinishingTimeComparator(  ) );

        // create DepthFirstOrder forest on inverted graph,
        // save trees as strongly connected components
        HashSet processedVertices = new HashSet(  );

        for( int i = 0; i < orderedVertices.length; i++ ) {
            VertexData data = orderedVertices[ i ];

            // already contained in one of the trees?
            if( !processedVertices.contains( data.getVertex(  ) ) ) {
                Set                stronglyConnectedComponent = new HashSet(  );
                DepthFirstIterator myIter =
                    new DepthFirstIterator( invertedGraph, data.getVertex(  ) );

                while( myIter.hasNext(  ) ) {
                    Object vertex = myIter.next(  );

                    if( !processedVertices.contains( vertex ) ) {
                        processedVertices.add( vertex );
                        stronglyConnectedComponent.add( vertex );
                    }
                }

                // save tree
                result.add( stronglyConnectedComponent );
            }
        }

        return result;
    }

    /**
     * Overwrites some method of the subclass {@link DepthFirstIterator} to
     * compute the discovery and finishing time of all vertices. The result is
     * saved in a map which contains the vertices as keys and the computed
     * {@link VertexData} objects as values.  For an example of usage, see
     * {@link AnalyzingDepthFirstIterator}.
     *
     * @author chris
     */
    private class AnalyzingDepthFirstIterator extends DepthFirstIterator {
        private final Map vertices = new HashMap(  );
        private int       time = 0;

        /**
         * {@inheritDoc}
         */
        AnalyzingDepthFirstIterator( DirectedGraph g ) {
            super( g );
        }

        /**
         * {@inheritDoc}
         */
        protected void encounterVertex( Object vertex, Edge edge ) {
            super.encounterVertex( vertex, edge );

            VertexData data = new VertexData( vertex );
            data.visit( time );
            time++;
            vertices.put( vertex, data );
        }


        /**
         * {@inheritDoc}
         */
        protected void encounterVertexAgain( Object vertex, Edge edge ) {
            super.encounterVertexAgain( vertex, edge );

            VertexData data = (VertexData) vertices.get( vertex );
            data.visit( time );
            time++;
        }


        /**
         * After the iterator has been used, this method returns a HashMap
         * containing the vertices as keys and VertexData objects as values.
         *
         * @return the HashMap containing vertices and data.
         */
        Map getVerticesData(  ) {
            return vertices;
        }
    }


    /**
     * A {@link Comparator} for {@link VertexData} objects.
     *
     * @author chris
     */
    private class FinishingTimeComparator implements Comparator {
        /**
         * Returns -1, 0 or 1 if the finishing time of vertexData1 is less,
         * equal or greater than the finishing time of vertexData2.
         *
         * @param vertexData1 The first VertexData object to compare
         * @param vertexData2 The second VertexData object to compare
         *
         * @return -1, 0 or 1 (see above)
         */
        public int compare( Object vertexData1, Object vertexData2 ) {
            VertexData vd1 = (VertexData) vertexData1;
            VertexData vd2 = (VertexData) vertexData2;

            if( vd1.getFinishingTime(  ) < vd2.getFinishingTime(  ) ) {
                return -1;
            }
            else if( vd1.getFinishingTime(  ) > vd2.getFinishingTime(  ) ) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }


    /**
     * Used to store data about a vertex, namely the time when a vertex has
     * been visited first (discovery time) and the time when it was visited
     * last (finishing time).
     *
     * @author chris
     */
    private class VertexData {
        private final Object vertex;
        private int          discovered = Integer.MIN_VALUE;
        private int          finished   = Integer.MIN_VALUE;

        /**
         * The constructor of the VertexData class.
         *
         * @param vertex the vertex to associate with this VertexData object.
         */
        VertexData( Object vertex ) {
            super(  );
            this.vertex = vertex;
        }

        /**
         * Gets the discovery time of the vertex associated with this
         * VertexData object.
         *
         * @return The discovery time
         */
        int getDiscoveyTime(  ) {
            return discovered;
        }


        /**
         * Gets the finishing time of the vertex associated with this
         * VertexData object.
         *
         * @return The finishing time
         */
        int getFinishingTime(  ) {
            return finished;
        }


        /**
         * Gets the vertex associated with this VertexData object.
         *
         * @return The vertex
         */
        Object getVertex(  ) {
            return this.vertex;
        }


        /**
         * Called every time a vertex is visited by
         * AnalyzingDepthFirstIterator. Makes sure that discovery and
         * finishing time are set to proper values.
         *
         * @param time The current time of the {@link
         *        AnalyzingDepthFirstIterator}
         */
        void visit( int time ) {
            if( this.discovered == Integer.MIN_VALUE ) {
                this.discovered     = time;
                this.finished       = time;
            }
            else {
                this.finished = time;
            }
        }
    }
}
