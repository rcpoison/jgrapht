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
/* ----------------
 * AllAlgTests.java
 * ----------------
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
package org._3pq.jgrapht.alg;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * A TestSuite for all tests in this package.
 *
 * @author Barak Naveh
 */
public final class AllAlgTests {
    private AllAlgTests(  ) {} // ensure non-instantiability.

    /**
     * Creates a test suite for all tests in this package.
     *
     * @return a test suite for all tests in this package.
     */
    public static Test suite(  ) {
        TestSuite suite = new TestSuite(  );

        //$JUnit-BEGIN$
        suite.addTest( new TestSuite( ConnectivityInspectorTest.class ) );
        suite.addTest( new TestSuite( DijkstraShortestPathTest.class ) );
        suite.addTest( new TestSuite( VertexCoversTest.class ) );
        suite.addTest( new TestSuite( CycleDetectorTest.class ) );

        //$JUnit-END$
        return suite;
    }
}
