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
 * DefaultUndirectedEdge.java
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
package org._3pq.jgrapht.edge;

import org._3pq.jgrapht.UndirectedEdge;

/**
 * A default implementation of UndirectedEdge.
 *
 * @author Barak Naveh
 *
 * @see org._3pq.jgrapht.UndirectedEdge
 * @since Jul 14, 2003
 */
public class DefaultUndirectedEdge extends AbstractEdge
    implements UndirectedEdge {
    /**
     * @see AbstractEdge#AbstractEdge(Object, Object)
     */
    public DefaultUndirectedEdge( Object sourceVertex, Object targetVertex ) {
        super( sourceVertex, targetVertex );
    }

    /**
     * @see UndirectedEdge#equals(Object)
     */
    public boolean equals( Object o ) {
        if( o instanceof UndirectedEdge ) {
            UndirectedEdge e = (UndirectedEdge) o;

            boolean        equalStraight =
                this.getSource(  ).equals( e.getSource(  ) )
                && this.getTarget(  ).equals( e.getTarget(  ) );

            boolean equalInverted =
                this.getSource(  ).equals( e.getTarget(  ) )
                && this.getTarget(  ).equals( e.getSource(  ) );

            return equalStraight || equalInverted;
        }

        return false;
    }


    /**
     * @see UndirectedEdge#hashCode()
     */
    public int hashCode(  ) {
        return getSource(  ).hashCode(  ) + getTarget(  ).hashCode(  );
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString(  ) {
        return "{" + getSource(  ) + "," + getTarget(  ) + "}";
    }
}
