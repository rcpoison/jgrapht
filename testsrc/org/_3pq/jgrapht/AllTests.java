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
/* -------------
 * AllTests.java
 * -------------
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
package org._3pq.jgrapht;

import java.util.Enumeration;

import junit.framework.Test;
import junit.framework.TestSuite;

import org._3pq.jgrapht.alg.AllAlgTests;
import org._3pq.jgrapht.edge.AllEdgeTests;
import org._3pq.jgrapht.graph.AllGraphTests;

/**
 * Runs all unit tests of the JGraphT library.
 *
 * @author Barak Naveh
 */
public final class AllTests {
    /*
     * We don't want instances of this class.
     */
    private AllTests(  ) {}

    /**
     * Creates a test suite that includes all JGraphT tests.
     *
     * @return a test suite that includes all JGraphT tests.
     */
    public static Test suite(  ) {
        ExpandableTestSuite suite =
            new ExpandableTestSuite( "All tests of JGraphT" );

        suite.addTestSuit( (TestSuite) AllEdgeTests.suite(  ) );
        suite.addTestSuit( (TestSuite) AllGraphTests.suite(  ) );
        suite.addTestSuit( (TestSuite) AllAlgTests.suite(  ) );

        return suite;
    }

    private static class ExpandableTestSuite extends TestSuite {
        /**
         * @see TestSuite#TestSuite()
         */
        public ExpandableTestSuite(  ) {
            super(  );
        }


        /**
         * @see TestSuite#TestSuite(java.lang.String)
         */
        public ExpandableTestSuite( String name ) {
            super( name );
        }

        /**
         * Adds all the test from the specified suite into this suite.
         *
         * @param suite
         */
        public void addTestSuit( TestSuite suite ) {
            for( Enumeration enum = suite.tests(  ); enum.hasMoreElements(  ); ) {
                Test t = (Test) enum.nextElement(  );
                this.addTest( t );
            }
        }
    }
}
