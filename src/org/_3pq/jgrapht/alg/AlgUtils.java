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
/* -------------------------
 * BreadthFirstIterator.java
 * -------------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   Liviu Rau
 *
 * $Id$
 *
 * Changes
 * -------
 * 24-Jul-2003 : Initial revision (BN);
 *
 */
package org._3pq.jgrapht.alg;

import java.util.LinkedList;
import java.util.List;

import org._3pq.jgrapht.DirectedGraph;
import org._3pq.jgrapht.UndirectedGraph;

/**
 * A collection of utilities used for implementing algorithms.
 *
 * @author Barak Naveh
 *
 * @since Jul 31, 2003
 */
public final class AlgUtils {
    private AlgUtils(  ) {} // ensure non-instantiability.

    /**
     * A simple queue structure.
     *
     * @author Liviu Rau
     *
     * @since Jul 29, 2003
     */
    public static class SimpleQueue {
        private LinkedList m_elementList = new LinkedList(  );

        /**
         * Tests if this queue is empty.
         *
         * @return <code>true</code> if empty, otherwise <code>false</code>.
         */
        public boolean isEmpty(  ) {
            return m_elementList.size(  ) == 0;
        }


        /**
         * Adds the specified object to the tail of the queue.
         *
         * @param o the object to be added.
         */
        public void add( Object o ) {
            m_elementList.addLast( o );
        }


        /**
         * Remove the object at the head of the queue and return it.
         *
         * @return the object and the head of the queue.
         */
        public Object remove(  ) {
            return m_elementList.removeFirst(  );
        }
    }


    /**
     * Provides unified interface for operations that are different in directed
     * graphs and in undirected graphs.
     *
     * @author Barak Naveh
     *
     * @since Jul 28, 2003
     */
    public abstract static class Specifics {
        /**
         * Returns the edges outgoing from the specified vertex in case of
         * directed graph, and the edge touching the specified vertex in case
         * of undirected graph.
         *
         * @param vertex the vertex whose outgoing edges are to be returned.
         *
         * @return the edges outgoing from the specified vertex in case of
         *         directed graph, and the edge touching the specified vertex
         *         in case of undirected graph.
         */
        public abstract List edgesOf( Object vertex );
    }


    /**
     * An implementation of {@link Specifics} for a directed graph.
     *
     * @author Barak Naveh
     *
     * @since Jul 28, 2003
     */
    public static class DirectedSpecifics extends Specifics {
        private DirectedGraph m_graph;

        /**
         * Creates a new DirectedSpecifics object.
         *
         * @param g the graph for which this specifics object to be created.
         */
        public DirectedSpecifics( DirectedGraph g ) {
            m_graph = g;
        }

        /**
         * @see BreadthFirstIterator.Specifics#edgesOf(Object)
         */
        public List edgesOf( Object vertex ) {
            return m_graph.outgoingEdgesOf( vertex );
        }
    }


    /**
     * A simple queue structure.
     *
     * @author Liviu Rau
     */
    public static class SimpleStack {
        private LinkedList m_elementList = new LinkedList(  );

        /**
         * Tests if this queue is empty.
         *
         * @return <code>true</code> if empty, otherwise <code>false</code>.
         */
        public boolean isEmpty(  ) {
            return m_elementList.size(  ) == 0;
        }


        /**
         * Adds the specified object to the tail of the queue.
         *
         * @param o the object to be added.
         */
        public void add( Object o ) {
            m_elementList.addLast( o );
        }


        /**
         * Remove the object at the head of the queue and return it.
         *
         * @return the object and the head of the queue.
         */
        public Object remove(  ) {
            return m_elementList.removeLast(  );
        }
    }


    /**
     * An implementation of {@link Specifics} for an undirected graph.
     *
     * @author Barak Naveh
     *
     * @since Jul 28, 2003
     */
    public static class UndirectedSpecifics extends Specifics {
        private UndirectedGraph m_graph;

        /**
         * Creates a new DirectedSpecifics object.
         *
         * @param g the graph for which this specifics object to be created.
         */
        public UndirectedSpecifics( UndirectedGraph g ) {
            m_graph = g;
        }

        /**
         * @see BreadthFirstIterator.Specifics#edgesOf(Object)
         */
        public List edgesOf( Object vertex ) {
            return m_graph.edgesOf( vertex );
        }
    }
}
