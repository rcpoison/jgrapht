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
/* -----------------------
 * EdgeFactoryFactory.java
 * -----------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 24-Jul-2003 : Initial revision (BN);
 *
 */
package org._3pq.jgrapht.edge;

import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.EdgeFactory;

/**
 * A factory of edge factories.
 *
 * @author Barak Naveh
 *
 * @since Jul 16, 2003
 */
public class EdgeFactoryFactory {
    /**
     * Returns an edge factory producing directed edges.
     *
     * @return an edge factory producing directed edges.
     */
    public EdgeFactory createDirectedEdgeFactory(  ) {
        return new DirectedEdgeFactory(  );
    }


    /**
     * Returns an edge factory producing directed weighted edges.
     *
     * @return an edge factory producing directed weighted edges.
     */
    public EdgeFactory createDirectedWeightedEdgeFactory(  ) {
        return new DirectedWeightedEdgeFactory(  );
    }


    /**
     * Returns an edge factory producing undirected edges.
     *
     * @return an edge factory producing undirected edges.
     */
    public EdgeFactory createUndirectedEdgeFactory(  ) {
        return new UndirectedEdgeFactory(  );
    }


    /**
     * Returns an edge factory producing undirected weighted edges.
     *
     * @return an edge factory producing undirected weighted edges.
     */
    public EdgeFactory createUndirectedWeightedEdgeFactory(  ) {
        return new UndirectedWeightedEdgeFactory(  );
    }

    /**
     * An EdgeFactory for producing directed edges.
     *
     * @author Barak Naveh
     *
     * @since Jul 14, 2003
     */
    public static class DirectedEdgeFactory implements EdgeFactory {
        /**
         * @see EdgeFactory#createEdge(Object, Object)
         */
        public Edge createEdge( Object source, Object target ) {
            return new DefaultDirectedEdge( source, target );
        }
    }


    /**
     * An EdgeFactory for producing directed edges with weights.
     *
     * @author Barak Naveh
     *
     * @since Jul 14, 2003
     */
    public static class DirectedWeightedEdgeFactory implements EdgeFactory {
        /**
         * @see EdgeFactory#createEdge(Object, Object)
         */
        public Edge createEdge( Object source, Object target ) {
            return new DefaultDirectedWeightedEdge( source, target );
        }
    }


    /**
     * An EdgeFactory for producing undirected edges.
     *
     * @author Barak Naveh
     *
     * @since Jul 14, 2003
     */
    public static class UndirectedEdgeFactory implements EdgeFactory {
        /**
         * @see EdgeFactory#createEdge(Object, Object)
         */
        public Edge createEdge( Object source, Object target ) {
            return new DefaultUndirectedEdge( source, target );
        }
    }


    /**
     * An EdgeFactory for producing undirected edges with weights.
     *
     * @author Barak Naveh
     *
     * @since Jul 14, 2003
     */
    public static class UndirectedWeightedEdgeFactory implements EdgeFactory {
        /**
         * @see EdgeFactory#createEdge(Object, Object)
         */
        public Edge createEdge( Object source, Object target ) {
            return new DefaultUndirectedWeightedEdge( source, target );
        }
    }
}
