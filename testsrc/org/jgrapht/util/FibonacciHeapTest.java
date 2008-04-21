/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Creator:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2008, by Barak Naveh and Contributors.
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
/* -----------------
 * FibonacciHeapTest.java
 * -----------------
 * (C) Copyright 2008-2008, by John V. Sichi and Contributors.
 *
 * Original Author:  John V. Sichi
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 20-Apr-2008 : Initial revision (JVS);
 */
package org.jgrapht.util;

import junit.framework.*;

public class FibonacciHeapTest
    extends TestCase
{
    // in honor of sf.net bug #1845376
    public void testAddRemoveOne()
    {
        String s = "A";
        FibonacciHeapNode<String> n =
            new FibonacciHeapNode<String>(s, 1.0);
        FibonacciHeap<String> h = new FibonacciHeap<String>();
        assertTrue(h.isEmpty());
        h.insert(n, n.getKey());
        assertFalse(h.isEmpty());
        FibonacciHeapNode<String> n2 = h.removeMin();
        assertEquals(s, n2.getData());
        assertTrue(h.isEmpty());
    }
}

// End FibonacciHeapTest.java
