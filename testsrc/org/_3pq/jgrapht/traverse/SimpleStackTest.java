/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Lead:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
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
/* --------------------
 * SimpleStackTest.java
 * --------------------
 * (C) Copyright 2003, by Liviu Rau and Contributors.
 *
 * Original Author:  Liviu Rau
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 30-Jul-2003 : Initial revision (LR);
 *
 */
package org._3pq.jgrapht.traverse;

import junit.framework.TestCase;

import org._3pq.jgrapht.traverse.DepthFirstIterator.SimpleStack;

/**
 * Unit test for SimpleStack.
 *
 * @author Liviu Rau
 */
public class SimpleStackTest extends TestCase {
    /**
     * .
     */
    public void testAdd(  ) {
        SimpleStack ss = new SimpleStack(  );
        ss.add( new Object(  ) );
        assertFalse( ss.isEmpty(  ) );
    }


    /**
     * .
     */
    public void testIsEmpty(  ) {
        SimpleStack ss = new SimpleStack(  );
        assertTrue( ss.isEmpty(  ) );
    }


    /**
     * .
     */
    public void testRemove(  ) {
        SimpleStack ss = new SimpleStack(  );
        Object      o = new Object(  );
        ss.add( o );
        ss.add( "string" );
        assertEquals( "string", ss.remove(  ) );
        assertFalse( ss.isEmpty(  ) );
        assertEquals( o, ss.remove(  ) );
        assertTrue( ss.isEmpty(  ) );
    }
}
