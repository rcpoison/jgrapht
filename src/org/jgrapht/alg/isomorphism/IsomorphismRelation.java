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
 * IsomorphismRelation.java
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
import org._3pq.jgrapht.graph.*;
import org._3pq.jgrapht.util.*;


/**
 * Holds an isomorphism relation for two graphs. It contains a mapping between
 * the two graphs.
 *
 * <p>Usage:
 *
 * <ol>
 * <li>use <code>getCorrespondence()</code> to get the mapped object in the
 * other graph.
 * <li>use <code>getGraph1vertexArray(), getGraph2vertexArray()</code> to see
 * the full vertex arrays order , in which the i-th vertex in the 1st array is
 * the isomorphic eqv. of the i-th in 2nd array.
 * </ol>
 *
 * <p>
 * <p>It consists of two vertexes array , the i-th vertex in the 1st array is
 * the isomorphic eqv. of the i-th in 2nd array. Note that the getters are
 * unsafe (they return the array and not a copy of it).
 *
 * @author Assaf
 * @since May 27, 2005
 */
public class IsomorphismRelation implements GraphMapping
{

    //~ Instance fields -------------------------------------------------------

    private Object [] graph1vertexArray;
    private Object [] graph2vertexArray;

    private GraphMapping graphMapping = null;

    private Graph graph1;
    private Graph graph2;

    //~ Constructors ----------------------------------------------------------

    /**
     */
    public IsomorphismRelation(
        Object [] aGraph1vertexArray,
        Object [] aGraph2vertexArray,
        Graph g1,
        Graph g2)
    {
        this.graph1vertexArray = aGraph1vertexArray;
        this.graph2vertexArray = aGraph2vertexArray;
        this.graph1 = g1;
        this.graph2 = g2;
    }

    //~ Methods ---------------------------------------------------------------

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("graph1vertexArray: ").append(
            ArrayUtil.toString(this.graph1vertexArray));
        sb.append("\tgraph2vertexArray: ").append(
            ArrayUtil.toString(this.graph2vertexArray));
        return sb.toString();
    }

    public Object [] getGraph1vertexArray()
    {
        return graph1vertexArray;
    }

    public Object [] getGraph2vertexArray()
    {
        return graph2vertexArray;
    }

    /**
     */

    /**
     * Assumption: if vertexOrEdge should be treated as vertex, it may not be
     * of the class "org._3pq.jgrapht.Edge". (It will fail in cases the
     * org._3pq.jgrapht.Edge is the vertex type).
     *
     * @see org._3pq.jgrapht.GraphMapping#getCorrespondence(java.lang.Object,
     *      boolean)
     */
    public Object getCorrespondence(Object vertexOrEdge, boolean forward)
    {
        // lazy initiliazer for graphMapping
        if (graphMapping == null) {
            initGraphMapping();
        }

        Object resultObject =
            this.graphMapping.getCorrespondence(vertexOrEdge, forward);
        return resultObject;
    }

    /**
     * We currently have the vertexes array. From them we will construct two
     * maps: g1ToG2 and g2ToG1, using the array elements with the same index.
     */
    private void initGraphMapping()
    {
        int mapSize = graph1vertexArray.length;
        Map g1ToG2 = new HashMap(mapSize);
        Map g2ToG1 = new HashMap(mapSize);

        for (int i = 0; i < mapSize; i++) {
            Object source = this.graph1vertexArray[i];
            Object target = this.graph2vertexArray[i];
            g1ToG2.put(source, target);
            g2ToG1.put(target, source);
        }
        this.graphMapping =
            new DefaultGraphMapping(g1ToG2, g2ToG1, this.graph1, this.graph2);
    }
}
