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
/* -----------------
 * AllGraphTests.java
 * -----------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 03-Aug-2003 : Initial revision (BN);
 *
 */
package org._3pq.jgrapht.graph;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * A TestSuite for all graph tests.
 *
 * @author Barak Naveh
 *
 * @since Aug 3, 2003
 */
public final class AllGraphTests {
    /*
     * We don't want instances of this class.
     */
    private AllGraphTests(  ) {}

    /**
     * Creates a test suite for all graph tests.
     *
     * @return a test suite for all graph tests.
     */
    public static Test suite(  ) {
        TestSuite suite = new TestSuite( "TestSuite for all graph tests" );

        //$JUnit-BEGIN$
        suite.addTest( new TestSuite( SimpleDirectedGraphTest.class ) );
        suite.addTest( new TestSuite( ListenableGraphTest.class ) );

        //$JUnit-END$
        return suite;
    }
}
