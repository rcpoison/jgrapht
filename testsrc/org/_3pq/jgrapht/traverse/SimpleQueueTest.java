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
/* --------------------
 * SimpleQueueTest.java
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

import org._3pq.jgrapht.traverse.TraverseUtils.SimpleQueue;

/**
 * Unit test for SimpleQueue.
 *
 * @author Liviu Rau
 */
public class SimpleQueueTest extends TestCase {
    /**
     * .
     */
    public void testAdd(  ) {
        SimpleQueue sq = new SimpleQueue(  );
        sq.add( new Object(  ) );
        assertFalse( sq.isEmpty(  ) );
    }


    /**
     * .
     */
    public void testIsEmpty(  ) {
        SimpleQueue sq = new SimpleQueue(  );
        assertTrue( sq.isEmpty(  ) );
    }


    /**
     * .
     */
    public void testRemove(  ) {
        SimpleQueue sq = new SimpleQueue(  );
        Object      o = new Object(  );
        sq.add( o );
        sq.add( "string" );
        assertEquals( o, sq.remove(  ) );
        assertFalse( sq.isEmpty(  ) );
        assertEquals( "string", sq.remove(  ) );
        assertTrue( sq.isEmpty(  ) );
    }
}
