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
/* ------------------------------------
 * DefaultDirectedWeightedEdgeTest.java
 * ------------------------------------
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

import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.edge.DefaultDirectedWeightedEdge;
import org._3pq.jgrapht.edge.DirectedWeightedEdge;

/**
 * Tests for the {@link DefaultDirectedWeightedEdge} class.
 *
 * @author Barak Naveh
 *
 * @since Jul 24, 2003
 */
public class DefaultDirectedWeightedEdgeTest extends DefaultDirectedEdgeTest {
    /**
     * Tests the equals() method.
     */
    public void testEqualsObject(  ) {
        Edge e1 = new DefaultDirectedWeightedEdge( m_source1, m_target1 );
        Edge e2 = new DefaultDirectedWeightedEdge( m_target1, m_source1 );
        assertTrue( m_e1.equals( e1 ) );
        assertFalse( m_e1.equals( e2 ) );
        assertFalse( m_e1.equals( m_e3 ) );

        String s1 = new String( "s1" );
        Edge   e4 = new DefaultDirectedWeightedEdge( s1, "t1" );
        assertTrue( m_e1.equals( e4 ) );
        assertFalse( m_source1 == e4.getSource(  ) );

        DefaultDirectedWeightedEdge e5 =
            new DefaultDirectedWeightedEdge( m_source1, m_target1, 2.0 );
        assertFalse( e5.equals( e1 ) );
        e5.setWeight( DirectedWeightedEdge.DEFAULT_EDGE_WEIGHT );
        assertTrue( e5.equals( e1 ) );
    }


    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp(  ) throws Exception {
        super.setUp(  );
        m_e1     = new DefaultDirectedWeightedEdge( m_source1, m_target1 );
        m_e3 =
            new DefaultDirectedWeightedEdge( new Object(  ), new Object(  ) );

        m_e1Clone = (DefaultDirectedWeightedEdge) m_e1.clone(  );
    }
}
