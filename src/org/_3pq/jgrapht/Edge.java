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
/* ---------
 * Edge.java
 * ---------
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
 * An edge used with graph objects. This is the root interface in the edge
 * hierarchy.
 *
 * @author Barak Naveh
 *
 * @since Jul 14, 2003
 */
public interface Edge extends Cloneable {
    /** The default weight for an edge. */
    public static double DEFAULT_EDGE_WEIGHT = 1.0;

    /**
     * Returns the source vertex of this edge.
     *
     * @return the source vertex of this edge.
     */
    public Object getSource(  );


    /**
     * Returns the target vertex of this edge.
     *
     * @return the target vertex of this edge.
     */
    public Object getTarget(  );


    /**
     * Sets the weight of this edge. If this edge is unweighted an
     * <code>UnsupportedOperationException</code> is thrown.
     *
     * @param weight new weight.
     *
     * @throws UnsupportedOperationException if this edge is unweighted.
     */
    public void setWeight( double weight );


    /**
     * Returns the weight of this edge. If this edge is unweighted the value
     * <code>1,0</code> is returned.
     *
     * @return the weight of this element.
     */
    public double getWeight(  );


    /**
     * Creates and returns a shallow copy of this edge. The vertices of this
     * edge are <i>not</i> cloned.
     *
     * @return a shallow copy of this edge.
     *
     * @see Cloneable
     * @see java.lang.Object#clone()
     */
    public Object clone(  );


    /**
     * Returns <tt>true</tt> if this edge contains the specified vertex.  More
     * formally, returns <tt>true</tt> if and only if the following condition
     * holds:
     * <pre>
     *      this.getSource().equals(v) || this.getTarget().equals(v)
     * </pre>
     *
     * @param v vertex whose presence in this edge is to be tested.
     *
     * @return <tt>true</tt> if this edge contains the specified vertex.
     */
    public boolean containsVertex( Object v );


    /**
     * Returns the vertex opposite to the specified vertex.
     *
     * @param v the vertex whose opposite is required.
     *
     * @return the vertex opposite to the specified vertex.
     *
     * @throws IllegalArgumentException if v is neither the source nor the
     *         target vertices of this edge.
     * @throws NullPointerException if v is <code>null</code>.
     */
    public Object oppositeVertex( Object v );
}
