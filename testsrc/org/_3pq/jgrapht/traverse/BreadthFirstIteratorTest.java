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
/* -----------------------------
 * BreadthFirstIteratorTest.java
 * -----------------------------
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

import org._3pq.jgrapht.Graph;

/**
 * Tests for the {@link BreadthFirstIterator} class.
 *
 * @author Liviu Rau
 *
 * @since Jul 30, 2003
 */
public class BreadthFirstIteratorTest extends AbstractGraphIteratorTest {
    String getExpectedStr1(  ) {
        return "1,2,3,4,5,6,7,8,9";
    }


    String getExpectedStr2(  ) {
        return "1,2,3,4,5,6,7,8,9,orphan";
    }


    AbstractGraphIterator createIterator( Graph g, Object vertex,
        boolean crossComponent ) {
        return new BreadthFirstIterator( g, vertex, crossComponent, false );
    }
}
