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
/* -----------------
 * AbstractEdge.java
 * -----------------
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

/**
 * A skeletal implementation of the <tt>Edge</tt> interface, to minimize the
 * effort required to implement the interface.
 *
 * @author Barak Naveh
 *
 * @since Jul 14, 2003
 */
public abstract class AbstractEdge implements Edge, Cloneable {
    private Object m_source;
    private Object m_target;

    /**
     * Constructor for AbstractEdge.
     *
     * @param sourceVertex source vertex of the edge. 
     * @param targetVertex target vertex of the edge. 
     */
    public AbstractEdge( Object sourceVertex, Object targetVertex ) {
        m_source     = sourceVertex;
        m_target     = targetVertex;
    }

    /**
     * @see org._3pq.jgrapht.Edge#getSource()
     */
    public Object getSource(  ) {
        return m_source;
    }


    /**
     * @see org._3pq.jgrapht.Edge#getTarget()
     */
    public Object getTarget(  ) {
        return m_target;
    }


    /**
     * @see Edge#clone()
     */
    public Object clone(  ) {
        try {
            return super.clone(  );
        }
         catch( CloneNotSupportedException e ) {
            // shouldn't happen as we are Cloneable
            throw new InternalError(  );
        }
    }


    /**
     * @see org._3pq.jgrapht.Edge#containsVertex(java.lang.Object)
     */
    public boolean containsVertex( Object v ) {
        return m_source.equals( v ) || m_target.equals( v );
    }


    /**
     * @see org._3pq.jgrapht.Edge#oppositeVertex(java.lang.Object)
     */
    public Object oppositeVertex( Object v ) {
        if( v.equals( m_source ) ) {
            return m_target;
        }
        else if( v.equals( m_target ) ) {
            return m_source;
        }
        else {
            throw new IllegalArgumentException( "no such vertex" );
        }
    }
}
