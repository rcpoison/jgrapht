/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Creator:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2009, by Barak Naveh and Contributors.
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
 * along with this library; if not, write to the Free Software Foundation,
 * Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */
/* -------------------------
 * FloydWarshallShortestPaths.java
 * -------------------------
 * (C) Copyright 2009-2009, by Tom Larkworthy and Contributors
 *
 * Original Author:  Tom Larkworthy
 *
 * $Id$
 *
 * Changes
 * -------
 * 29-Jun-2009 : Initial revision (TL);
 *
 */
package org.jgrapht.alg;

import java.util.HashMap;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.VertexFactory;
import org.jgrapht.generate.RandomGraphGenerator;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.SimpleGraph;

/**
 * The <a href="http://en.wikipedia.org/wiki/Floyd-Warshall_algorithm">
 * Floyd-Warshall algorithm</a> finds all shortest paths (all n^2 of them) 
 * in O(n^3) time.
 * This also works out the graph diameter during the process.
 * @author Tom Larkworthy
 */
public class FloydWarshallShortestPaths<V,E> {
    int nextIndex = 0;
    HashMap<V, Integer> indices;
	
    double [/*source*/][/*target*/] d;
	
    double diameter;

    /**
     * Constructs the shortest path array for the given graph.
     *
     * @param g input graph
     */
    public FloydWarshallShortestPaths(Graph<V,E> g){
        int sz = g.vertexSet().size();
        d = new double[sz][sz];
        indices = new HashMap<V, Integer>();
		
        //Initialise distance to infinity, or the neighbours weight, or 0 if same
        for(V v1:g.vertexSet()){
            for(V v2:g.vertexSet()){
                if(v1 == v2) d[index(v1)][index(v2)]=0;
                else{
                    E e = g.getEdge(v1,v2);
					
                    if(e == null){
                        d[index(v1)][index(v2)] = Double.POSITIVE_INFINITY;
                    }else{
                        d[index(v1)][index(v2)] = g.getEdgeWeight(e);
                    }
                }
            }
        }
		
        //now iterate k times
        for(int k=0; k<sz;k++){
            for(V v1:g.vertexSet()){
                for(V v2:g.vertexSet()){
                    d[index(v1)][index(v2)] = 
                        Math.min(
                            d[index(v1)][index(v2)], 
                            d[index(v1)][k]+d[k][index(v2)]
                            );
                    diameter = Math.max(diameter, d[index(v1)][index(v2)]);
                }
            }
        }
    }

    /**
     * Retrieves the shortest distance between two vertices.
     *
     * @param v1 first vertex
     *
     * @param v2 second vertex
     *
     * @return distance, or positive infinity if no path
     */
    public double shortestDistance(V v1, V v2){
        return d[index(v1)][index(v2)];
    }

    /**
     * @return diameter computed for the graph
     */
    public double getDiameter(){
        return diameter;
    }
	
    private int index(V vertex){
        Integer index = indices.get(vertex);
        if(index == null){
            indices.put(vertex, nextIndex);
            index = nextIndex++;
        }
        return index;
    }
}

// End FloydWarshallShortestPaths.java
