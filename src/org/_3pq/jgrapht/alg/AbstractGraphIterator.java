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
/* --------------------------
 * AbstractGraphIterator.java
 * --------------------------
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
package org._3pq.jgrapht.alg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org._3pq.jgrapht.Edge;

/**
 * An empty implementation of a graph iterator to minimize the effort required
 * to implement graph iterators.
 *
 * @author Barak Naveh
 *
 * @since Jul 19, 2003
 */
public abstract class AbstractGraphIterator implements Iterator {
    private List    m_traversalListeners      = new ArrayList(  );
    private boolean m_crossComponentTraversal = true;

    /**
     * Sets the cross component traversal flag - indicates whether to traverse
     * the graph across connected components.
     *
     * @param crossComponentTraversal if <code>true</code> traverses across
     *        connected components.
     */
    public void setCrossComponentTraversal( boolean crossComponentTraversal ) {
        m_crossComponentTraversal = crossComponentTraversal;
    }


    /**
     * Test whether this iterator is set to traverse the grpah across connected
     * components.
     *
     * @return <code>true</code> if traverses across connected components,
     *         otherwise <code>false</code>.
     */
    public boolean isCrossComponentTraversal(  ) {
        return m_crossComponentTraversal;
    }


    /**
     * Adds the specified traversal listener to this iterator.
     *
     * @param l the traversal listener to be added.
     */
    public void addTraversalListener( TraversalListener l ) {
        if( !m_traversalListeners.contains( l ) ) {
            m_traversalListeners.add( l );
        }
    }


    /**
     * Unsupported.
     *
     * @throws UnsupportedOperationException
     */
    public void remove(  ) {
        throw new UnsupportedOperationException(  );
    }


    /**
     * Removes the specified traversal listener from this iterator.
     *
     * @param l the traversal listener to be removed.
     */
    public void removeTraversalListener( TraversalListener l ) {
        m_traversalListeners.remove( l );
    }


    /**
     * Informs all listeners that the traversal of the current connected
     * component finished.
     */
    protected void fireConnectedComponentFinished(  ) {
        int len = m_traversalListeners.size(  );

        for( int i = 0; i < len; i++ ) {
            TraversalListener l =
                (TraversalListener) m_traversalListeners.get( i );
            l.connectedComponentFinished(  );
        }
    }


    /**
     * Informs all listeners that a traversal of a new connected component has
     * started.
     */
    protected void fireConnectedComponentStarted(  ) {
        int len = m_traversalListeners.size(  );

        for( int i = 0; i < len; i++ ) {
            TraversalListener l =
                (TraversalListener) m_traversalListeners.get( i );
            l.connectedComponentStarted(  );
        }
    }


    /**
     * Informs all listeners that a the specifed edge was visited.
     *
     * @param edge the visited edge.
     */
    protected void fireEdgeVisited( Edge edge ) {
        int len = m_traversalListeners.size(  );

        for( int i = 0; i < len; i++ ) {
            TraversalListener l =
                (TraversalListener) m_traversalListeners.get( i );
            l.edgeVisited( edge );
        }
    }


    /**
     * Informs all listeners that a the specifed vertex was visited.
     *
     * @param vertex the visited vertex.
     */
    protected void fireVertexVisited( Object vertex ) {
        int len = m_traversalListeners.size(  );

        for( int i = 0; i < len; i++ ) {
            TraversalListener l =
                (TraversalListener) m_traversalListeners.get( i );
            l.vertexVisited( vertex );
        }
    }
}
