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
/* ------------------------
 * DefaultDirectedEdge.java
 * ------------------------
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

import org._3pq.jgrapht.DirectedEdge;

/**
 * A default implementation of DirectedEdge.
 *
 * @author Barak Naveh
 *
 * @see org._3pq.jgrapht.DirectedEdge
 * @since Jul 14, 2003
 */
public class DefaultDirectedEdge extends AbstractEdge implements DirectedEdge {
    /**
     * @see AbstractEdge#AbstractEdge(Object, Object)
     */
    public DefaultDirectedEdge( Object sourceVertex, Object targetVertex ) {
        super( sourceVertex, targetVertex );
    }

    /**
     * @see DirectedEdge#isSource(Object)
     */
    public boolean isSource( Object vertex ) {
        return vertex.equals( this.getSource(  ) );
    }


    /**
     * @see DirectedEdge#isTarget(Object)
     */
    public boolean isTarget( Object vertex ) {
        return vertex.equals( this.getTarget(  ) );
    }


    /**
     * @see DirectedEdge#equals(Object)
     */
    public boolean equals( Object o ) {
        if( o instanceof DirectedEdge ) {
            DirectedEdge e = (DirectedEdge) o;

            return this.getSource(  ).equals( e.getSource(  ) )
            && this.getTarget(  ).equals( e.getTarget(  ) );
        }

        return false;
    }


    /**
     * @see DirectedEdge#hashCode()
     */
    public int hashCode(  ) {
        return getSource(  ).hashCode(  ) + getTarget(  ).hashCode(  );
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString(  ) {
        return "(" + getSource(  ) + "," + getTarget(  ) + ")";
    }
}
