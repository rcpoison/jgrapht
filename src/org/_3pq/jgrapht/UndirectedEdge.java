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
/* -------------------
 * UndirectedEdge.java
 * -------------------
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
 * A undirected edge connecting two vertices: "source" and "target". An
 * undirected  edge is not assigned a direction so source and target can be
 * swapped without changing  the meaning of the edge.
 *
 * @author Barak Naveh
 *
 * @since Jul 14, 2003
 */
public interface UndirectedEdge extends Edge {
    /**
     * Compares the specified object with this undirected edge for equality.
     * Returns <tt>true</tt> if the specified object is also an undirected
     * edge, the two edges has equal weight, and either the source and target
     * of the two edges are equal, or their source and target are inverted.
     * 
     * <p>
     * The last condition is more formally expressed by:
     * <pre>
     *    ( this.getSource().equals(o.getSource()) && 
     *      this.getTarget().equals(o.getTarget()) )  ||
     *    ( this.getSource().equals(o.getTarget()) && 
     *      this.getTarget().equals(o.getSource()) )
     * </pre>
     * </p>
     *
     * @param o Object to be compared for equality with this edge.
     *
     * @return <tt>true</tt> if the specified Object is equal to this edge.
     */
    public boolean equals( Object o );


    /**
     * Returns the hash code value for this undirected edge. The hash code of
     * an undirected edge is defined to be the sum of the hash codes of its
     * source and the target vertices.  This ensures that
     * <code>e1.equals(e2)</code> implies that
     * <code>e1.hashCode()==e2.hashCode()</code> for any two undirected edges
     * <code>e1</code> and <code>e2</code>, as required by the general
     * contract of the <tt>Object.hashCode</tt> method.
     *
     * @return the hash code value for this edge.
     *
     * @see Object#hashCode()
     * @see Object#equals(Object)
     * @see UndirectedEdge#equals(Object)
     */
    public int hashCode(  );
}
