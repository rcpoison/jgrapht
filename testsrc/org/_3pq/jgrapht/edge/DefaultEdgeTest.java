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
 * along with this library; if not, write to the Free Software Foundation,
 * Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */
/* ---------------------
 * DefaultEdgeTest.java
 * ---------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 09-Aug-2003 : Initial revision (BN);
 * 10-Aug-2003 : General edge refactoring (BN);
 *
 */
package org._3pq.jgrapht.edge;

import junit.framework.*;

import org._3pq.jgrapht.*;


/**
 * Tests for the {@link org._3pq.jgrapht.edge.DefaultEdge} class.
 *
 * @author Barak Naveh
 * @since Jul 24, 2003
 */
public abstract class DefaultEdgeTest extends TestCase
{

    //~ Instance fields -------------------------------------------------------

    Edge m_e1;
    Edge m_e1Clone;
    Edge m_e3;
    String m_source1 = "s1";
    String m_target1 = "t1";
    String m_target2 = "t2";

    //~ Methods ---------------------------------------------------------------

    /**
     * .
     */
    public void testClone()
    {
        assertEquals(m_e1Clone.getSource(), m_e1.getSource());
        assertEquals(m_e1Clone.getTarget(), m_e1.getTarget());
    }

    /**
     * .
     */
    public void testContainsVertex()
    {
        assertTrue(m_e1.containsVertex(m_source1));
        assertTrue(m_e1.containsVertex(m_target1));

        assertFalse(m_e1.containsVertex(m_target2));
    }

    /**
     * .
     */
    public void testGetSource()
    {
        assertTrue(m_e1.getSource() == m_source1);
        assertTrue(m_e1.getSource().equals("s1"));
    }

    /**
     * .
     */
    public void testGetTarget()
    {
        assertTrue(m_e1.getTarget() == m_target1);
        assertTrue(m_e1.getTarget().equals("t1"));
    }

    /**
     * .
     */
    public void testOppositeVertex()
    {
        assertEquals(m_source1, m_e1.oppositeVertex(m_target1));
        assertEquals(m_target1, m_e1.oppositeVertex(m_source1));

        assertFalse(m_source1 == m_e1.oppositeVertex(m_source1));
        assertFalse(m_target1 == m_e1.oppositeVertex(m_target1));
    }

    /**
     * @see TestCase#setUp()
     */
    protected abstract void setUp()
        throws Exception;
}
