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
 * CloneTest.java
 * ----------------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  John V. Sichi
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 06-Oct-2003 : Initial revision (JVS);
 *
 */
package org._3pq.jgrapht.graph;

import org._3pq.jgrapht.EnhancedTestCase;

/**
 * A unit test for a cloning bug, adapted from a forum entry from Linda
 * Buisman.
 *
 * @author John V. Sichi
 *
 * @since Oct 6, 2003
 */
public class CloneTest extends EnhancedTestCase {
    /**
     * @see junit.framework.TestCase#TestCase(java.lang.String)
     */
    public CloneTest( String name ) {
        super( name );
    }

    /**
     * Test graph cloning.
     */
    public void testCloneSpecificsBug(  ) {
        SimpleGraph g1    = new SimpleGraph(  );
        String      one   = "1";
        String      two   = "2";
        String      three = "3";
        g1.addVertex( one );
        g1.addVertex( two );
        g1.addVertex( three );
        g1.addEdge( one, two );
        g1.addEdge( two, three );

        SimpleGraph g2 = (SimpleGraph) g1.clone(  );
        assertEquals( 2, g2.edgeSet(  ).size(  ) );
        assertNotNull( g2.getEdge( one, two ) );
        assertTrue( g2.removeEdge( g2.getEdge( one, two ) ) );
        assertNotNull( g2.removeEdge( "2", "3" ) );
        assertTrue( g2.edgeSet(  ).isEmpty(  ) );
    }
}
