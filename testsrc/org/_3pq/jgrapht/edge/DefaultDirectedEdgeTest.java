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
/* ----------------------------
 * DefaultDirectedEdgeTest.java
 * ----------------------------
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
package org._3pq.jgrapht.edge;

import junit.framework.TestCase;

import org._3pq.jgrapht.Edge;

/**
 * Tests for the {@link DefaultDirectedEdge} class.
 *
 * @author Barak Naveh
 *
 * @since Jul 24, 2003
 */
public class DefaultDirectedEdgeTest extends TestCase {
    DefaultDirectedEdge m_e1;
    DefaultDirectedEdge m_e1Clone;
    DefaultDirectedEdge m_e3;
    String              m_source1 = "s1";
    String              m_target1 = "t1";
    String              m_target2 = "t2";

    /**
     * .
     */
    public void testClone(  ) {
        assertTrue( m_e1Clone.equals( m_e1 ) );
        assertFalse( m_e1Clone == m_e1 );
    }


    /**
     * .
     */
    public void testContainsVertex(  ) {
        assertTrue( m_e1.containsVertex( m_source1 ) );
        assertTrue( m_e1.containsVertex( m_target1 ) );

        assertFalse( m_e1.containsVertex( m_target2 ) );
    }


    /**
     * .
     */
    public void testEqualsObject(  ) {
        Edge e1 = new DefaultDirectedEdge( m_source1, m_target1 );
        Edge e2 = new DefaultDirectedEdge( m_target1, m_source1 );
        assertTrue( m_e1.equals( e1 ) );
        assertFalse( m_e1.equals( e2 ) );
        assertFalse( m_e1.equals( m_e3 ) );

        String s1 = new String( "s1" );
        Edge   e4 = new DefaultDirectedEdge( s1, "t1" );
        assertTrue( m_e1.equals( e4 ) );
        assertFalse( m_source1 == e4.getSource(  ) );
    }


    /**
     * .
     */
    public void testGetSource(  ) {
        assertTrue( m_e1.getSource(  ) == m_source1 );
        assertTrue( m_e1.getSource(  ).equals( "s1" ) );
    }


    /**
     * .
     */
    public void testGetTarget(  ) {
        assertTrue( m_e1.getTarget(  ) == m_target1 );
        assertTrue( m_e1.getTarget(  ).equals( "t1" ) );
    }


    /**
     * .
     */
    public void testHashCode(  ) {
        assertEquals( m_source1.hashCode(  ) + m_target1.hashCode(  ),
            m_e1.hashCode(  ) );
    }


    /**
     * .
     */
    public void testIsSource(  ) {
        assertEquals( m_source1, m_e1.getSource(  ) );
    }


    /**
     * .
     */
    public void testIsTarget(  ) {
        assertEquals( m_target1, m_e1.getTarget(  ) );
    }


    /**
     * .
     */
    public void testOppositeVertex(  ) {
        assertEquals( m_source1, m_e1.oppositeVertex( m_target1 ) );
        assertEquals( m_target1, m_e1.oppositeVertex( m_source1 ) );

        assertFalse( m_source1 == m_e1.oppositeVertex( m_source1 ) );
        assertFalse( m_target1 == m_e1.oppositeVertex( m_target1 ) );
    }


    /**
     * .
     */
    public void testToString(  ) {
        assertTrue( "(s1,t1)".equals( m_e1.toString(  ) ) );
        assertTrue( "(s1,t1)".equals( m_e1Clone.toString(  ) ) );
    }


    /**
     * @see TestCase#setUp()
     */
    protected void setUp(  ) throws Exception {
        super.setUp(  );
        m_e1     = new DefaultDirectedEdge( m_source1, m_target1 );
        m_e3     = new DefaultDirectedEdge( new Object(  ), new Object(  ) );

        m_e1Clone = (DefaultDirectedEdge) m_e1.clone(  );
    }


    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown(  ) throws Exception {
        super.tearDown(  );
    }
}
