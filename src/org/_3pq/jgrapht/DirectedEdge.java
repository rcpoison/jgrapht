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
 * DirectedEdge.java
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
package org._3pq.jgrapht;

/**
 * A edge going from its "source" vertex to its "target" vertex where this
 * direction from source to target is significant.
 *
 * @author Barak Naveh
 *
 * @since Jul 14, 2003
 */
public interface DirectedEdge extends Edge {
    /**
     * Returns <tt>true</tt> if the specified vertex is the source of this
     * edge.  More formally, returns <tt>true</tt> if and only if
     * <code>v.equals(this.getSource())</code>.
     *
     * @param vertex the vertex tested to be the source of this edge.
     *
     * @return <tt>true</tt> if the specified vertex is the source of this
     *         edge.
     */
    public boolean isSource( Object vertex );


    /**
     * Returns <tt>true</tt> if the specified vertex is the target of this
     * edge.  More formally, returns <tt>true</tt> if and only if
     * <code>v.equals(this.getTarget())</code>.
     *
     * @param vertex the vertex tested to be the target of this edge.
     *
     * @return <tt>true</tt> if the specified vertex is the target of this
     *         edge.
     */
    public boolean isTarget( Object vertex );


    /**
     * Compares the specified object with this directed edge for equality.
     * Returns <tt>true</tt> if the specified object is also a directed edge,
     * the two edges has equal source, target, and weight
     *
     * @param o Object to be compared for equality with this edge.
     *
     * @return <tt>true</tt> if the specified Object is equal to this edge.
     */
    public boolean equals( Object o );


    /**
     * Returns the hash code value for this directed edge. The hash code of a
     * directed edge is defined to be the sum of the hash codes of its source
     * and the target vertices.  This ensures that <code>e1.equals(e2)</code>
     * implies that <code>e1.hashCode()==e2.hashCode()</code> for any two
     * directed edges <code>e1</code> and <code>e2</code>, as required by the
     * general contract of the <tt>Object.hashCode</tt> method.
     *
     * @return the hash code value for this edge.
     *
     * @see Object#hashCode()
     * @see Object#equals(Object)
     * @see DirectedEdge#equals(Object)
     */
    public int hashCode(  );
}
