/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Lead:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2004, by Barak Naveh and Contributors.
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
 * DepthFirstIteratorTest.java
 * ---------------------------
 * (C) Copyright 2003, by Liviu Rau and Contributors.
 *
 * Original Author:  Liviu Rau
 * Contributor(s):   Barak Naveh
 *
 * $Id$
 *
 * Changes
 * -------
 * 30-Jul-2003 : Initial revision (LR);
 * 06-Aug-2003 : Test traversal listener & extract a shared superclass (BN);
 *
 */
package org._3pq.jgrapht.traverse;

import org._3pq.jgrapht.DirectedGraph;

/**
 * Tests for the {@link DepthFirstIteratorTest} class.
 * 
 * <p>
 * NOTE: This test uses hard-coded expected ordering isn't really guaranteed by
 * the specification of the algorithm. This could cause false failures if the
 * traversal implementation changes.
 * </p>
 *
 * @author Liviu Rau
 *
 * @since Jul 30, 2003
 */
public class DepthFirstIteratorTest extends AbstractGraphIteratorTest {
    String getExpectedStr1(  ) {
        return "1,3,6,5,7,9,4,8,2";
    }


    String getExpectedStr2(  ) {
        return "1,3,6,5,7,9,4,8,2,orphan";
    }


    AbstractGraphIterator createIterator( DirectedGraph g, Object vertex ) {
        AbstractGraphIterator i = new DepthFirstIterator( g, vertex );
        i.setCrossComponentTraversal( true );

        return i;
    }
}
