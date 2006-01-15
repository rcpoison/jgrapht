/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Lead:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2006, by Barak Naveh and Contributors.
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
 * along with this library; if not, write to the Free Software Foundation,
 * Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */
/* -------------------------
 * BellmanFordPathElement.java
 * -------------------------
 * (C) Copyright 2006, by Guillaume Boulmi and Contributors.
 *
 * Original Author:  Guillaume Boulmi and Contributors.
 * Contributor(s):   John V. Sichi
 *
 * $Id$
 *
 * Changes
 * -------
 * 05-Jan-2006 : Initial revision (GB);
 * 14-Jan-2006 : Added support for generics (JVS);
 *
 */
package org._3pq.jgrapht.alg;

import org._3pq.jgrapht.Edge;

/**
 * Helper class for {@link BellmanFordShortestPath}; not intended
 * for general use.
 */
final class BellmanFordPathElement<V,E extends Edge<V>>
    extends AbstractPathElement implements Cloneable
{
    private double cost = 0;

    private double epsilon = 0.000000001;

    /**
     * Creates a path element by concatenation of an edge to a path element.
     * 
     * @param pathElement
     * @param edge
     *            edge reaching the end vertex of the path element created.
     * @param cost
     *            total cost of the created path element.
     *  
     */
    protected BellmanFordPathElement(BellmanFordPathElement<V,E> pathElement,
            E edge, double cost) {
        super(pathElement, edge);

        this.cost = cost;
    }

    /**
     * Creates an empty path element.
     * 
     * @param vertex
     *            end vertex of the path element.
     */
    protected BellmanFordPathElement(V vertex) {
        super(vertex);

        this.cost = 0;
    }

    /**
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        BellmanFordPathElement clonedObject = null;
        try {
            clonedObject = (BellmanFordPathElement) super.clone();
        } catch (CloneNotSupportedException e) {
        }
        return clonedObject;
    }

    /**
     * Returns the total cost of the path element.
     * 
     * @return .
     */
    public double getCost() {
        return this.cost;
    }

    /**
     * Returns <code>true</code> if the path has been improved,
     * <code>false</code> otherwise. We use an "epsilon" precision to check
     * whether the cost has been improved (because of many roundings, a formula
     * equal to 0 could unfortunately be evaluated to 10^-14).
     * 
     * @param candidatePrevPathElement
     * @param candidateEdge
     * @param candidateCost
     * 
     * @return .
     */
    protected boolean improve(
        BellmanFordPathElement<V,E> candidatePrevPathElement,
        Edge candidateEdge, double candidateCost) {
        // to avoid improvement only due to rounding errors.
        if (candidateCost < getCost() - this.epsilon) {
            this.prevPathElement = candidatePrevPathElement;
            this.prevEdge = candidateEdge;
            this.cost = candidateCost;
            this.nHops = candidatePrevPathElement.getHopCount() + 1;

            return true;
        } else {
            return false;
        }
    }
}
