/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Lead:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2005, by Barak Naveh and Contributors.
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
/* -----------------
 * GraphOrdering.java
 * -----------------
 * (C) Copyright 2005, by Assaf Lehr and Contributors.
 *
 * Original Author:  Assaf Lehr
 * Contributor(s):   -
 *
 * Changes
 * -------
 */
package org._3pq.jgrapht.alg.isomorphism;

import java.util.*;

import org._3pq.jgrapht.*;
import org._3pq.jgrapht.edge.*;
import org._3pq.jgrapht.util.*;


/**
 * Holds graph information as int labels only. vertexes: 1,2,3,4 edges:1->2 ,
 * 3->4 ,1->1. Implementation as imutable graph by int[] for vetexes and
 * LabelsEdge[] for edges. The current algorithms do not support graph with
 * multiple edges (Multigraph / Pseudograph). For the maintaner: The reason for
 * it is the use of edges sets of LabelsEdge in which the equals checks for
 * source and target vertexes. Thus there cannot be two LabelsEdge with the
 * same source and target in the same Set.
 *
 * @author Assaf
 * @since May 20, 2005
 */
public class GraphOrdering
{

    //~ Instance fields -------------------------------------------------------

    /**
     * Holds a mapping between key=Object(vertex) and value=Integer(vertex
     * order). It can be used for identifying the order of regular vertex/edge.
     */
    private Map mapVertexToOrder;

    /**
     * Holds a HashSet of all LabelsGraph of the graph.
     */
    private Set labelsEdgesSet;

    //~ Constructors ----------------------------------------------------------

    /**
     * Creates a new labels graph according to the regular graph. After its
     * creation they will no longer be linked, thus changes to one will not
     * affect the other.
     *
     * @param regularGraph
     */
    public GraphOrdering(Graph regularGraph)
    {
        Set vertexSet = regularGraph.vertexSet();
        Set edgeSet = regularGraph.edgeSet();

        init(vertexSet, edgeSet);
    }

    /**
     * Creates a new labels graph according to the regular graph. After its
     * creation they will no longer be linked, thus changes to one will not
     * affect the other.
     *
     * @param vertexSet
     * @param edgeSet
     */
    public GraphOrdering(Set vertexSet, Set edgeSet)
    {
        init(vertexSet, edgeSet);
    }

    //~ Methods ---------------------------------------------------------------

    private void init(Set vertexSet, Set edgeSet)
    {
        // create a map between vertex value to its order(1st,2nd,etc)
        // "CAT"=1 "DOG"=2 "RHINO"=3

        this.mapVertexToOrder = new HashMap(vertexSet.size());

        int counter = 0;
        for (Iterator iter = vertexSet.iterator(); iter.hasNext();) {
            Object vertex = iter.next();
            mapVertexToOrder.put(vertex, new Integer(counter));
            counter++;
        }

        // create a friendlier representation of an edge
        // by order, like 2nd->3rd instead of B->A
        // use the map to convert vertex to order
        // on directed graph, edge A->B must be (A,B)
        // on undirected graph, edge A-B can be (A,B) or (B,A)

        this.labelsEdgesSet = new HashSet(edgeSet.size());
        for (Iterator iter = edgeSet.iterator(); iter.hasNext();) {
            Edge edge = (Edge) iter.next();

            Object sourceVertex = edge.getSource();
            Integer sourceOrder = (Integer) mapVertexToOrder.get(sourceVertex);
            int sourceLabel = sourceOrder.intValue();
            int targetLabel =
                ((Integer) (mapVertexToOrder.get(edge.getTarget())))
                .intValue();

            LabelsEdge lablesEdge = new LabelsEdge(sourceLabel, targetLabel);
            this.labelsEdgesSet.add(lablesEdge);

            if (edge instanceof UndirectedEdge) {
                LabelsEdge oppositeEdge =
                    new LabelsEdge(targetLabel, sourceLabel);
                this.labelsEdgesSet.add(oppositeEdge);
            }
        }
    }

    /**
     * Tests equality by order of edges
     */
    public boolean equalsByEdgeOrder(GraphOrdering otherGraph)
    {
        boolean result =
            this.getLabelsEdgesSet().equals(otherGraph.getLabelsEdgesSet());

        return result;
    }

    public Set getLabelsEdgesSet()
    {
        return labelsEdgesSet;
    }

    /**
     * This is the format example:
     *
     * <pre> 
       mapVertexToOrder=        labelsOrder=
     * </pre>
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("mapVertexToOrder=");

        // vertex will be printed in their order
        int numberOfVertexInMap = this.mapVertexToOrder.size();
        Object [] vertexArray = new Object [this.mapVertexToOrder.size()];
        Set keySet = this.mapVertexToOrder.keySet();
        for (Iterator iter = keySet.iterator(); iter.hasNext();) {
            Object currVertex = (Object) iter.next();
            Integer index = (Integer) this.mapVertexToOrder.get(currVertex);
            vertexArray[index.intValue()] = currVertex;
        }
        sb.append(ArrayUtil.toString(vertexArray));
        sb.append("labelsOrder=").append(this.labelsEdgesSet.toString());
        return sb.toString();
    }

    //~ Inner Classes ---------------------------------------------------------

    private class LabelsEdge
    {
        int source;
        int target;
        int hashCode;

        public LabelsEdge(int aSource, int aTarget)
        {
            this.source = aSource;
            this.target = aTarget;
            this.hashCode =
                new String(this.source + "" + this.target).hashCode();
        }

        /**
         * Checks both source and target.  Does not check class type to be
         * fast, so it may throw ClassCastException. Careful!
         *
         * @see java.lang.Object#equals(java.lang.Object)
         */
        public boolean equals(Object obj)
        {
            LabelsEdge otherEdge = (LabelsEdge) obj;
            if ((this.source == otherEdge.source)
                && (this.target == otherEdge.target)) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        public int hashCode()
        {
            return this.hashCode; // filled on constructor
        }

        public String toString()
        {
            return this.source + "->" + this.target;
        }
    }
}
