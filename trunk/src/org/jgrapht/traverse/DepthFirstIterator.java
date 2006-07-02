/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Creator:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2006, by Barak Naveh and Contributors.
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
/* -----------------------
 * DepthFirstIterator.java
 * -----------------------
 * (C) Copyright 2003-2006, by Liviu Rau and Contributors.
 *
 * Original Author:  Liviu Rau
 * Contributor(s):   Barak Naveh
 *                   Christian Hammer
 *
 * $Id$
 *
 * Changes
 * -------
 * 29-Jul-2003 : Initial revision (LR);
 * 31-Jul-2003 : Fixed traversal across connected components (BN);
 * 06-Aug-2003 : Extracted common logic to TraverseUtils.XXFirstIterator (BN);
 * 31-Jan-2004 : Reparented and changed interface to parent class (BN);
 * 04-May-2004 : Made generic (CH)
 *
 */
package org.jgrapht.traverse;

import java.util.*;

import org.jgrapht.*;


/**
 * A depth-first iterator for a directed and an undirected graph. For this
 * iterator to work correctly the graph must not be modified during iteration.
 * Currently there are no means to ensure that, nor to fail-fast. The results
 * of such modifications are undefined.
 *
 * @author Liviu Rau
 * @author Barak Naveh
 * @since Jul 29, 2003
 */
public class DepthFirstIterator<V, E>
    extends CrossComponentIterator<V, E, Object>
{

    //~ Instance fields -------------------------------------------------------

    private List<V> stack = new ArrayList<V>();

    //~ Constructors ----------------------------------------------------------

    /**
     * Creates a new depth-first iterator for the specified graph.
     *
     * @param g the graph to be iterated.
     */
    public DepthFirstIterator(Graph<V, E> g)
    {
        this(g, null);
    }

    /**
     * Creates a new depth-first iterator for the specified graph. Iteration
     * will start at the specified start vertex and will be limited to the
     * connected component that includes that vertex. If the specified start
     * vertex is <code>null</code>, iteration will start at an arbitrary vertex
     * and will not be limited, that is, will be able to traverse all the
     * graph.
     *
     * @param g the graph to be iterated.
     * @param startVertex the vertex iteration to be started.
     */
    public DepthFirstIterator(Graph<V, E> g, V startVertex)
    {
        super(g, startVertex);
    }

    //~ Methods ---------------------------------------------------------------

    /**
     * @see CrossComponentIterator#isConnectedComponentExhausted()
     */
    protected boolean isConnectedComponentExhausted()
    {
        return stack.isEmpty();
    }

    /**
     * @see CrossComponentIterator#encounterVertex(Object, Object)
     */
    protected void encounterVertex(V vertex, E edge)
    {
        putSeenData(vertex, null);
        stack.add(vertex);
    }

    /**
     * @see CrossComponentIterator#encounterVertexAgain(Object, Object)
     */
    protected void encounterVertexAgain(V vertex, E edge)
    {
    }

    /**
     * @see CrossComponentIterator#provideNextVertex()
     */
    protected V provideNextVertex()
    {
        return stack.remove(stack.size() - 1);
    }
}
