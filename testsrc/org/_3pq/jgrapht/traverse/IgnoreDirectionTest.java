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
 * IgnoreDirectionTest.java
 * ---------------------------
 * (C) Copyright 2003, by John V. Sichi and Contributors.
 *
 * Original Author:  John V. Sichi
 *
 * $Id$
 *
 * Changes
 * -------
 * 8-Aug-2003 : Initial revision (JVS);
 *
 */
package org._3pq.jgrapht.traverse;

import org._3pq.jgrapht.Graph;

/**
 * Tests for the ignoreDirection parameter to XXFirstIterator.
 * 
 * <p>
 * NOTE: This test uses hard-coded expected ordering isn't really guaranteed by
 * the specification of the algorithm. This could cause false failures if the
 * traversal implementation changes.
 * </p>
 *
 * @author John V. Sichi
 *
 * @since Aug 8, 2003
 */
public class IgnoreDirectionTest extends AbstractGraphIteratorTest {
    String getExpectedStr1(  ) {
        return "4,9,7,8,5,6,1,3,2";
    }


    String getExpectedStr2(  ) {
        return "4,9,7,8,5,6,1,3,2,orphan";
    }


    AbstractGraphIterator createIterator( Graph g, Object vertex ) {
        // ignore the passed in vertex and always start from v4, since that's
        // the only vertex without out-edges
        AbstractGraphIterator i = new DepthFirstIterator( g, "4", true );
        i.setCrossComponentTraversal( true );

        return i;
    }
}
