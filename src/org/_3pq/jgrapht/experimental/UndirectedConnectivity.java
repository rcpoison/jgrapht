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
/* ---------------------------
 * UndirectedConnectivity.java
 * ---------------------------
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
package org._3pq.jgrapht.experimental;

import java.util.Iterator;
import java.util.List;

import org._3pq.jgrapht.UndirectedGraph;

/**
 * Algorithms for deciding various connectivity aspects of undirected graphs.
 *
 * @author Barak Naveh
 *
 * @since Jul 18, 2003
 */
public abstract class UndirectedConnectivity {
    private UndirectedGraph m_graph;

    /**
     * Constructor for UndirectedConnectivity.
     *
     * @param g
     */
    public UndirectedConnectivity( UndirectedGraph g ) {
        m_graph = g;
    }

    /**
     * Test if the graph is connected.
     *
     * @return
     */
    public abstract boolean isGraphConnected(  );


    /**
     * Retuns an iterator on all connected components of this graph.
     *
     * @return
     */
    public abstract Iterator connectedComponentIterator(  );


    /**
     * Retuns a list of all connected components of the graph.
     *
     * @return
     */
    public abstract List connectedComponents(  );
}
